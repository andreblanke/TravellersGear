package travellersgear.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import org.jetbrains.annotations.NotNull;

public final class TGSaveData extends WorldSavedData {

	private static TGSaveData INSTANCE;

	/** This is for the server! */
	public final Map<UUID, NBTTagCompound> playerData = new HashMap<>();

	/** This is for the client! */
	public static final Map<UUID, NBTTagCompound> clientData = new HashMap<>();

	public static final String dataName = "TG-SaveData";

	public TGSaveData(final String s) {
		super(s);
	}

	@Override
	public void readFromNBT(@NotNull final NBTTagCompound nbt) {
		final NBTTagList tagList = nbt.getTagList("playerList", 10);

		for (int i = 0; i < tagList.tagCount(); ++i) {
			final NBTTagCompound tag = tagList.getCompoundTagAt(i);
			final UUID uuid          = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));

			playerData.put(uuid, tag);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		final NBTTagList tagList = new NBTTagList();

		for (Entry<UUID, NBTTagCompound> entry : playerData.entrySet())
		{
			if (entry.getKey() != null && entry.getValue() != null)
			{
				NBTTagCompound tag = entry.getValue();
				tag.setLong("UUIDMost", entry.getKey().getMostSignificantBits());
				tag.setLong("UUIDLeast", entry.getKey().getLeastSignificantBits());
				tagList.appendTag(tag);
			}
		}
		nbt.setTag("playerList", tagList);
	}

	public static NBTTagCompound getPlayerData(@NotNull final EntityPlayer player) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
		    return (INSTANCE != null)
                ? INSTANCE.playerData.get(player.getPersistentID())
                : new NBTTagCompound();
		} else {
            return clientData.get(player.getPersistentID());
        }
	}

	public static void setPlayerData(EntityPlayer player, NBTTagCompound tag)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			if (INSTANCE != null)
				INSTANCE.playerData.put(player.getPersistentID(),tag);
		}
		else
        {
            clientData.put(player.getPersistentID(),tag);
        }
	}

	public static void setDirty()
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			if (INSTANCE != null)
				INSTANCE.markDirty();
	}

	public static void setInstance(TGSaveData in)
	{
		if (FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
			INSTANCE = in;
	}
}
