package travellersgear.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;

import travellersgear.common.blocks.TileEntityArmorStand;
import travellersgear.common.inventory.ContainerArmorStand;
import travellersgear.common.inventory.ContainerTravellersInv;

public class CommonProxy implements IGuiHandler
{
	public static final Map<String, boolean[]> HIDDEN_SLOTS = new HashMap<>();

	// <editor-fold desc="IGuiHandler">
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case 0:
                return new ContainerTravellersInv(player.inventory);
            case 1:
                return new ContainerArmorStand(player.inventory, (TileEntityArmorStand) world.getTileEntity(x, y, z));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
    // </editor-fold>

	public void preInit(FMLPreInitializationEvent event) {
	}

	public void init() {
	}

	public World getClientWorld() {
		return null;
	}
}
