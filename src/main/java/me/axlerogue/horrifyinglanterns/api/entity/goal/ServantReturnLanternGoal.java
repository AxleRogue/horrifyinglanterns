package me.axlerogue.horrifyinglanterns.api.entity.goal;

import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class ServantReturnLanternGoal extends Goal {
    private final ServantOfTheDarkHeartEntity servant;
    private Player owner;

    public ServantReturnLanternGoal(ServantOfTheDarkHeartEntity servant) {
        this.servant = servant;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.servant.getLantern().isEmpty()) return false;
        
        Player p = (Player) this.servant.getOwner();
        if (p == null || p.isSpectator()) return false;
        
        int freeSlots = getFreeSlots(p);
        if (freeSlots > 0) {
            this.owner = p;
            return true;
        }
        
        return false;
    }

    @Override
    public void tick() {
        if (this.owner != null) {
            if (this.servant.distanceToSqr(this.owner) > 4.0D) {
                this.servant.getNavigation().moveTo(this.owner, 1.2D);
                this.servant.getLookControl().setLookAt(this.owner, 10.0F, (float)this.servant.getMaxHeadXRot());
            } else {
                ItemStack lantern = this.servant.getLantern().copy();
                if (this.owner.getInventory().add(lantern)) {
                    this.servant.setLantern(ItemStack.EMPTY);
                    if (this.servant.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.POOF, this.servant.getX(), this.servant.getY() + 0.5, this.servant.getZ(), 15, 0.2, 0.2, 0.2, 0.05);
                    }
                    this.servant.discard();
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && this.servant.isAlive() && !this.servant.getLantern().isEmpty();
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
