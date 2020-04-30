package travellersgear.common.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import travellersgear.TravellersGear;

public final class TGCreativeTab extends CreativeTabs {

	public TGCreativeTab() {
		super("travellersgear");
	}

    @Override
    @Nullable
    @Contract(pure = true)
    @SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return null;
	}

    @Override
    @NotNull
    @Contract(value = " -> new", pure = true)
    @SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		return new ItemStack(TravellersGear.simpleGear, 1, 6);
	}
}
