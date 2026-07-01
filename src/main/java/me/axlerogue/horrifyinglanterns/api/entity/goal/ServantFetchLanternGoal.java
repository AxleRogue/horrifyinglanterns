package me.axlerogue.horrifyinglanterns.api.entity.goal;

import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class ServantFetchLanternGoal extends Goal {
    private final ServantOfTheDarkHeartEntity servant;

    public ServantFetchLanternGoal(ServantOfTheDarkHeartEntity servant) {
        this.servant = servant;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.servant.getLantern().isEmpty()) return false;
        ItemEntity target = this.servant.targetLanternItem;
        return target != null && target.isAlive() && !target.getItem().isEmpty();
    }

    @Override
    public void tick() {
        ItemEntity target = this.servant.targetLanternItem;
        if (target != null && target.isAlive()) {
            this.servant.getNavigation().moveTo(target, 1.2D);
            if (this.servant.distanceToSqr(target) < 4.0D) {
                ItemStack stack = target.getItem().copy();
                this.servant.setLantern(stack);
                target.discard();
                this.servant.targetLanternItem = null;
            }
        } else {
            this.servant.targetLanternItem = null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
}
