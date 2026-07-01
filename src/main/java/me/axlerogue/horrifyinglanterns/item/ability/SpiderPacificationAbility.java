package me.axlerogue.horrifyinglanterns.item.ability;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.ability.BaseAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SpiderPacificationAbility extends BaseAbility {

    public SpiderPacificationAbility() {
        super("pacify_spiders", 1200); // 60 seconds cooldown
    }

    @Override
    public void execute(ServerPlayer player, ItemStack stack) {
        if (!LanternBaseItem.isLit(stack)) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_disabled"), true);
            return;
        }

        if (isOnCooldown(player, stack.getItem())) {
            player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_on_cooldown", getDisplayName()), true);
            return;
        }

        Level level = player.level();
        AABB area = player.getBoundingBox().inflate(16.0);
        List<Spider> targets = level.getEntitiesOfClass(Spider.class, area);

        boolean anyPacified = false;
        for (Spider target : targets) {
            target.setTarget(null);
            target.setLastHurtByMob(null);
            
            if (!target.getTags().contains("venom_pacified")) {
                target.getTags().add("venom_pacified");
                target.goalSelector.addGoal(1, new AvoidEntityGoal<>(target, Player.class, 16.0F, 1.0D, 1.2D));
            }
            anyPacified = true;
        }

        if (anyPacified || !targets.isEmpty()) {
            startCooldown(player, stack.getItem());
            player.displayClientMessage(Component.literal("Spiders pacified."), true);
        }
    }
}
