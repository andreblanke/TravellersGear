package travellersgear.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public interface IEventGear {

    void onUserDamaged(LivingHurtEvent event, ItemStack stack);

    void onUserAttacking(AttackEntityEvent event, ItemStack stack);

    void onUserJump(LivingJumpEvent event, ItemStack stack);

    void onUserFall(LivingFallEvent event, ItemStack stack);

    void onUserTargeted(LivingSetAttackTargetEvent event, ItemStack stack);
}
