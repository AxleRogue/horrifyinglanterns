package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class CreeperScareHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Creeper creeper) {
            // Add goal to avoid players holding lit lanterns
            creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, Player.class, 8.0F, 1.0D, 1.2D, (livingEntity) -> {
                if (livingEntity instanceof Player player) {
                    boolean mainHandLit = player.getMainHandItem().getItem() instanceof LanternBaseItem && LanternBaseItem.isLit(player.getMainHandItem());
                    boolean offHandLit = player.getOffhandItem().getItem() instanceof LanternBaseItem && LanternBaseItem.isLit(player.getOffhandItem());
                    return mainHandLit || offHandLit;
                }
                return false;
            }));
        }
    }
}
