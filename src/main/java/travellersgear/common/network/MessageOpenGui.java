package travellersgear.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import io.netty.buffer.ByteBuf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import travellersgear.TravellersGear;

public final class MessageOpenGui implements IMessage {

    private int guiId;
	private int dim;
	private int playerId;

	@SuppressWarnings("unused")
	public MessageOpenGui() {
    }

	public MessageOpenGui(@NotNull final EntityPlayer player, final int guiId) {
        this.guiId = guiId;

		dim      = player.worldObj.provider.dimensionId;
		playerId = player.getEntityId();
	}

	// <editor-fold desc="IMessage">
	@Override
	public void toBytes(@NotNull final ByteBuf buffer) {
		buffer.writeInt(dim);
		buffer.writeInt(playerId);
		buffer.writeInt(guiId);
	}

	@Override
	public void fromBytes(@NotNull final ByteBuf buffer) {
		dim      = buffer.readInt();
		playerId = buffer.readInt();
		guiId    = buffer.readInt();
	}
	// </editor-fold>

	public static final class HandlerClient implements IMessageHandler<MessageOpenGui, IMessage> {

		@Nullable
        @Override
		public IMessage onMessage(@NotNull final MessageOpenGui message, final MessageContext context) {
		    final EntityPlayer player = Minecraft.getMinecraft().thePlayer;

			player.openGui(
			    TravellersGear.instance,
                message.guiId,
                player.worldObj,
                MathHelper.floor_double(player.posX),
                MathHelper.floor_double(player.posY),
                MathHelper.floor_double(player.posZ)
            );
			return null;
		}
	}

	public static final class HandlerServer implements IMessageHandler<MessageOpenGui, IMessage> {

		@Nullable
        @Override
		public IMessage onMessage(@NotNull final MessageOpenGui message, final MessageContext context) {
			final World world = DimensionManager.getWorld(message.dim);
			if (world == null)
				return null;

			final EntityPlayer player = context.getServerHandler().playerEntity;
			if (player == null)
				return null;

            player.addStat(AchievementList.openInventory, 1);

			TravellersGear.packetHandler.sendToAll(new MessageNBTSync(player));
			boolean hasServerGui =
                TravellersGear.proxy.getServerGuiElement(
                    message.guiId,
                    player,
                    world,
                    (int) player.posX,
                    (int) player.posY,
                    (int) player.posZ
                ) != null;

			player.openGui(
			    TravellersGear.instance,
                message.guiId,
                player.worldObj,
                MathHelper.floor_double(player.posX),
                MathHelper.floor_double(player.posY),
                MathHelper.floor_double(player.posZ)
            );
			return hasServerGui ? null : message;
		}
	}
}
