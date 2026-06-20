package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class LanternInteractionHandler {

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        // We only cancel attack if they are NOT holding a Sanguine Moon Lantern 
        // OR if it's a general lantern. 
        // Actually, the user wants "Life Steal" on right click. Attack is left click.
        // The user said "not be able to attack while holding them in hand".
        // Life steal is a "use interaction also known as a right click".
        // So left click should still be disabled.
        if (shouldRestrictInteraction(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (shouldRestrictInteraction(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getItemStack().getItem() instanceof LanternBaseItem) {
            // Cancel on client to prevent hand swing, but allow on server to process logic
            if (event.getSide() == LogicalSide.CLIENT) {
                event.setCancellationResult(net.minecraft.world.InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onInput(InputEvent.InteractionKeyMappingTriggered event) {
            if (event.isAttack() || event.isUseItem()) {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (shouldRestrictInteraction(mc.player)) {
                    if (event.isAttack()) {
                        event.setCanceled(true);
                        event.setSwingHand(false);
                    } else if (mc.player.getItemInHand(event.getHand()).getItem() instanceof LanternBaseItem) {
                        // Only disable swing for the lantern itself when used
                        // Note: SanguineMoonLanternItem already handles this in 'use', but this is a backup
                        event.setSwingHand(false);
                    }
                }
            }
        }
    }

    public static boolean shouldRestrictInteraction(Player player) {
        if (player == null) return false;
        
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
        
        boolean holdingInMain = mainHand.getItem() instanceof LanternBaseItem;
        boolean holdingInOff = offHand.getItem() instanceof LanternBaseItem;
        
        // Restriction: holding lantern in hand OR sneaking while holding lantern in hand
        // User said: "not be able to attack while holding them in hand or while sneaking in hand"
        // This effectively means if you hold it, you can't attack.
        return holdingInMain || holdingInOff;
    }
}
