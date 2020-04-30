package travellersgear.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This interface can be implemented by any armor, bauble, Traveller's Gear, Mariculture jewelry and Tinker's Construct
 * gloves and knapsacks.
 *
 * They will then be eligible for the "Active Ability" menu in-game.
 *
 * @author BluSunrize
 */
public interface IActiveAbility {

    boolean canActivate(EntityPlayer player, ItemStack stack, boolean isInHand);

    void activate(EntityPlayer player, ItemStack stack);
}
