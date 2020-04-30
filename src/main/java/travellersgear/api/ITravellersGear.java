package travellersgear.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This interface can be implemented by any item, which makes it eligible to be equipped as Traveller's Gear.
 *
 * @author BluSunrize
 */
public interface ITravellersGear {

    /**
     * @return The equipment slot, or -1 if it isn't supposed to go into one.<br>
     * 0: Cloak<br>1: Shoulders<br>2: Vambraces<br>3: Title
     */
    int getSlot(ItemStack stack);

    void onTravelGearTick(EntityPlayer player, ItemStack stack);

    void onTravelGearEquip(EntityPlayer player, ItemStack stack);
    void onTravelGearUnequip(EntityPlayer player, ItemStack stack);
}
