package travellersgear.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import travellersgear.common.util.ModCompatibility;

import static net.minecraftforge.common.util.Constants.*;

public final class TravellersGearAPI {

	/**
	 * @return The NBTTagCompound under which all TRPG data is saved
	 */
	private static NBTTagCompound getTravellersNBTData(@NotNull final EntityPlayer player) {
		if (TGSaveData.getPlayerData(player) == null) {
			final NBTTagCompound tag;
			if (player.getEntityData().hasKey("TravellersRPG")) {
				tag = player.getEntityData().getCompoundTag("TravellersRPG");
				player.getEntityData().removeTag("TravellersRPG");
			}  else  {
                tag = new NBTTagCompound();
            }

			tag.setLong("UUIDMost", player.getPersistentID().getMostSignificantBits());
			tag.setLong("UUIDLeast", player.getPersistentID().getLeastSignificantBits());
			TGSaveData.setPlayerData(player, tag);
			TGSaveData.setDirty();
		}
		if (player.getEntityData().hasKey("TravellersRPG")) {
			NBTTagCompound tag = player.getEntityData().getCompoundTag("TravellersRPG");
			player.getEntityData().removeTag("TravellersRPG");
			tag.setLong("UUIDMost", player.getPersistentID().getMostSignificantBits());
			tag.setLong("UUIDLeast", player.getPersistentID().getLeastSignificantBits());
			TGSaveData.setPlayerData(player, tag);
			TGSaveData.setDirty();
		}
		return TGSaveData.getPlayerData(player);
	}

	/**
	 * This method will prioritize using the ITravellersGear interface, but will fall back to
     * {@link ModCompatibility#isStackPseudoTravellersGear(ItemStack)} to check whether the
     * item was registered via IMC.
     *
     * @return {@code true} if the provided {@link ItemStack} is valid for insertion into a Traveller's Gear inventory
     *         slot, otherwise {@code false}.
	 */
	@SuppressWarnings("unused")
	public static boolean isTravellersGear(ItemStack stack) {
		if (stack == null)
			return false;
		if (stack.getItem() instanceof ITravellersGear)
			return true;
        return ModCompatibility.isStackPseudoTravellersGear(stack);
	}

	/**
     * @param player The targeted player
     *
	 * @return The TRPG Extended inventory, consisting of Cloak(0), Shoulders(1), Vambraces(2), TitleScroll(3)
	 */
	@NotNull
    public static ItemStack[] getExtendedInventory(@NotNull final EntityPlayer player) {
		final ItemStack[] extendedInventoryContents = new ItemStack[4];

		final NBTTagList inv = getTravellersNBTData(player).getTagList("Inventory", NBT.TAG_COMPOUND);
		if (inv == null)
		    return extendedInventoryContents;

        for (int i = 0; i < inv.tagCount(); ++i) {
            final NBTTagCompound nbtTagCompound = inv.getCompoundTagAt(i);

            final int slot            = nbtTagCompound.getByte("Slot") & 0xFF;
            final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbtTagCompound);

            if ((itemstack != null) && (slot < extendedInventoryContents.length))
                extendedInventoryContents[slot] = itemstack;
        }
		return extendedInventoryContents;
	}

	/**
     * @param player The targeted player
     *
	 * @param inv The TRPG Extended inventory, consisting of Cloak(0), Shoulders(1), Vambraces(2), TitleScroll(3)
	 */
	public static void setExtendedInventory(final EntityPlayer player, final ItemStack[] inv) {
		if (player == null || inv == null)
			return;

		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < inv.length; ++i) {
            if (inv[i] != null) {
                NBTTagCompound invSlot = new NBTTagCompound();
                invSlot.setByte("Slot", (byte) i);
                inv[i].writeToNBT(invSlot);
                list.appendTag(invSlot);
            }
        }
		getTravellersNBTData(player).setTag("Inventory", list);
		TGSaveData.setDirty();
	}

	/**
     * @param player The targeted player
     *
	 * @return The tile currently equipped on the player
	 */
	@Nullable
    public static String getTitleForPlayer(final EntityPlayer player) {
		final ItemStack scroll = getExtendedInventory(player)[3];

        //noinspection ConstantConditions
        if (scroll == null)
            return getSpecialTitleForPlayer(player);

        if (scroll.hasTagCompound() && scroll.getTagCompound().hasKey("title"))
            return scroll.getTagCompound().getString("title");

        if (scroll.hasTagCompound() && scroll.getTagCompound().hasKey("display") && scroll.getTagCompound().getCompoundTag("display").hasKey("Lore")) {
            final NBTTagList loreList = scroll.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
            if (loreList.tagCount() > 0)
                return loreList.getStringTagAt(0);
        }
        if (scroll.hasDisplayName())
            return scroll.getDisplayName();
		return null;
	}

	@Nullable
    private static String getSpecialTitleForPlayer(@NotNull final EntityPlayer player) {
        switch (player.getCommandSenderName()) {
        case "blusunrize":
        case "Polaroid":
            return "Developer";
        default:
            return null;
        }
    }

	/**
     * @param player The targeted player.
     *
	 * @return An {@link NBTTagList} containing {@link NBTTagCompound}s which store data on tools to be displayed on
     *         the player.
	 */
	public static NBTTagList getDisplayTools(EntityPlayer player) {
		return getTravellersNBTData(player).getTagList("toolDisplay", 10);
	}

	/**
     * @param player The targeted player
     *
	 * @param list the taglist
	 */
	public static void setDisplayTools(EntityPlayer player, NBTTagList list) {
		getTravellersNBTData(player).setTag("toolDisplay", list);
		TGSaveData.setDirty();
	}
}
