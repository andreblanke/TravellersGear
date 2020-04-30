package travellersgear.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;

import travellersgear.TravellersGear;
import travellersgear.client.handlers.ActiveAbilityHandler;
import travellersgear.client.handlers.CustomizableGuiHandler;
import travellersgear.common.network.MessageOpenGui;
import travellersgear.common.network.MessageSlotSync;

public final class KeyHandler
{
	public static final KeyBinding OPEN_INVENTORY =
        new KeyBinding("TG.keybind.openInv", 71, "key.categories.inventory");

	public static final KeyBinding ACTIVE_ABILITIES_WHEEL =
        new KeyBinding("TG.keybind.activeAbilities", 19, "key.categories.inventory");

	public boolean[] keyDown = { false, false };
	public static float abilityRadial;
	public static boolean abilityLock;

	public KeyHandler() {
		ClientRegistry.registerKeyBinding(OPEN_INVENTORY);
		ClientRegistry.registerKeyBinding(ACTIVE_ABILITIES_WHEEL);
	}

	@SubscribeEvent
	public void playerTick(PlayerTickEvent event)
	{
	    if (!Keyboard.isCreated()
                || event.side == Side.SERVER
                || event.phase != TickEvent.Phase.START
                || !FMLClientHandler.instance().getClient().inGameHasFocus)
	        return;

        final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null)
            return;

        if (OPEN_INVENTORY.getIsKeyPressed() && !keyDown[0]) {
            final boolean[] hidden = new boolean[CustomizableGuiHandler.movableInvElements.size()];
            for (int bme = 0; bme < hidden.length; ++bme)
                hidden[bme] = CustomizableGuiHandler.movableInvElements.get(bme).hideElement;

            TravellersGear.packetHandler.sendToServer(new MessageSlotSync(player, hidden));
            TravellersGear.packetHandler.sendToServer(new MessageOpenGui(player, 0));
            keyDown[0] = true;
        } else if (keyDown[0]) {
            keyDown[0] = false;
        }

        if (ACTIVE_ABILITIES_WHEEL.getIsKeyPressed() && !keyDown[1] && ActiveAbilityHandler.instance.buildActiveAbilityList(player).length > 0) {
            if (abilityLock) {
                abilityLock=false;
                keyDown[1] = true;
            } else {
                if (abilityRadial < 1)
                    abilityRadial += ClientProxy.activeAbilityGuiSpeed;
                if (abilityRadial > 1)
                    abilityRadial = 1.0f;

                if (abilityRadial >= 1) {
                    abilityLock = true;
                    keyDown[1] = true;
                }
            }
        } else {
            if (keyDown[1] && !ACTIVE_ABILITIES_WHEEL.getIsKeyPressed())
                keyDown[1] = false;

            if (!abilityLock) {
                if (abilityRadial > 0)
                    abilityRadial -= ClientProxy.activeAbilityGuiSpeed;
                if (abilityRadial < 0)
                    abilityRadial = 0;
            }
        }
	}
}
