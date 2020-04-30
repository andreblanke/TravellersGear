package travellersgear.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import travellersgear.api.TravellersGearAPI;
import travellersgear.common.util.ModCompatibility;
import travellersgear.common.util.Utils;

public final class TravellersGearCreativeTabs implements IInventory {

	private Container container;
	public ItemStack[] stackList = new ItemStack[4];
	public EntityPlayer player;
	boolean allowEvents = true;

	public TravellersGearCreativeTabs(Container par1Container, EntityPlayer p) {
		this.container = par1Container;
		this.player = p;
	}

	@Override
	public int getSizeInventory() {
		return this.stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i >= this.getSizeInventory())
		    return null;
		return this.stackList[i];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.stackList[i] != null) {
			ItemStack itemstack = this.stackList[i];
			this.stackList[i] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (stackList[i] != null) {
			ItemStack itemstack;

			if (this.stackList[i].stackSize <= j) {
				itemstack = this.stackList[i];
				stackList[i] = null;
				if(itemstack != null && ModCompatibility.getTravellersGearSlot(itemstack)>=0)
					Utils.unequipTravGear(player, itemstack);

				markDirty();
				return itemstack;
			}
			itemstack = this.stackList[i].splitStack(j);

			if (stackList[i].stackSize == 0)
				stackList[i] = null;

			if (ModCompatibility.getTravellersGearSlot(itemstack) >= 0)
				Utils.unequipTravGear(player, itemstack);

			this.container.onCraftMatrixChanged(this);

			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack) {
		if (allowEvents && this.stackList[i] != null && ModCompatibility.getTravellersGearSlot(this.stackList[i]) >= 0)
			Utils.unequipTravGear(player, this.stackList[i]);

		stackList[i] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();

		if(allowEvents && stack != null && ModCompatibility.getTravellersGearSlot(stack)>=0)
			Utils.equipTravGear(player, stack);

		this.container.onCraftMatrixChanged(this);
	}

	@Override
	public String getInventoryName() {
		return "container.TravelersGear";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		if (player.worldObj.isRemote)
			TravellersGearAPI.setExtendedInventory(this.player, this.stackList);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openInventory() {
    }

	@Override
	public void closeInventory() {
    }

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
}
