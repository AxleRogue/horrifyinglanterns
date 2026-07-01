package me.axlerogue.horrifyinglanterns.api.entity.goal;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

public class ServantFindLanternGoal extends Goal {
    private final ServantOfTheDarkHeartEntity servant;

    public ServantFindLanternGoal(ServantOfTheDarkHeartEntity servant) {
        this.servant = servant;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (!this.servant.getLantern().isEmpty()) return false;
        
        Player owner = (Player) this.servant.getOwner();
        if (owner == null || owner.isSpectator()) return false;
        
        int freeSlots = getFreeSlots(owner);
        if (freeSlots > 0) return false;
        
        List<ItemEntity> items = this.servant.level().getEntitiesOfClass(ItemEntity.class, this.servant.getBoundingBox().inflate(16.0D), e -> true);
        for (ItemEntity item : items) {
            if (item.getItem().getItem() instanceof LanternBaseItem) {
                if (LanternBaseItem.isOwner(item.getItem(), owner)) {
                    this.servant.targetLanternItem = item;
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
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
