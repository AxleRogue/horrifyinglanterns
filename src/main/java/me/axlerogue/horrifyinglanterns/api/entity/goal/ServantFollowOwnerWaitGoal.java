package me.axlerogue.horrifyinglanterns.api.entity.goal;

import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ServantFollowOwnerWaitGoal extends Goal {
    private final ServantOfTheDarkHeartEntity servant;
    private Player owner;
    private int timeToRecalcPath;

    public ServantFollowOwnerWaitGoal(ServantOfTheDarkHeartEntity servant) {
        this.servant = servant;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.servant.getLantern().isEmpty()) return false;
        Player p = (Player) this.servant.getOwner();
        if (p == null || p.isSpectator()) return false;
        
        int freeSlots = getFreeSlots(p);
        if (freeSlots > 0) return false;
        
        if (this.servant.distanceToSqr(p) < 9.0D) return false;
        
        this.owner = p;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.servant.getNavigation().isDone() || this.servant.distanceToSqr(this.owner) <= 4.0D) return false;
        return getFreeSlots(this.owner) == 0;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        this.servant.getLookControl().setLookAt(this.owner, 10.0F, (float)this.servant.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.servant.getNavigation().moveTo(this.owner, 1.0D);
        }
    }

    private int getFreeSlots(Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            if (player.getInventory().items.get(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }
}
