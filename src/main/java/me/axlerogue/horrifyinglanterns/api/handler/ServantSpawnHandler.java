package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import me.axlerogue.horrifyinglanterns.item.client.ModEntities;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class ServantSpawnHandler {

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        // Runs on both Client and Server to satisfy the "both ways" requirement
        if (event.phase == TickEvent.Phase.END && event.level.getGameTime() % 40 == 0) { // Check every 2 seconds
            Level level = event.level;
            
            List<? extends Player> players = level.players();
            Map<Player, List<ItemEntity>> playerLanterns = new HashMap<>();
            
            // 1. Collect all dropped lanterns for players with full inventories
            for (Player player : players) {
                if (getFreeSlots(player) == 0) {
                    List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(32.0D));
                    List<ItemEntity> ownedLanterns = new ArrayList<>();
                    
                    for (ItemEntity item : items) {
                        if (item.getItem().getItem() instanceof LanternBaseItem) {
                            if (LanternBaseItem.isOwner(item.getItem(), player)) {
                                boolean hasServant = false;
                                List<ServantOfTheDarkHeartEntity> servants = level.getEntitiesOfClass(ServantOfTheDarkHeartEntity.class, item.getBoundingBox().inflate(32.0D));
                                for (ServantOfTheDarkHeartEntity servant : servants) {
                                    if (servant.targetLanternItem == item) {
                                        hasServant = true;
                                        break;
                                    }
                                }
                                if (!hasServant) {
                                    ownedLanterns.add(item);
                                }
                            }
                        }
                    }
                    if (!ownedLanterns.isEmpty()) {
                        playerLanterns.put(player, ownedLanterns);
                    }
                }
            }
            
            // 2. Prioritize players based on the amount of dropped lanterns
            List<Player> sortedPlayers = new ArrayList<>(playerLanterns.keySet());
            sortedPlayers.sort((p1, p2) -> Integer.compare(playerLanterns.get(p2).size(), playerLanterns.get(p1).size()));
            
            // 3. Serve the players according to priority
            for (Player player : sortedPlayers) {
                List<ItemEntity> lanterns = playerLanterns.get(player);
                for (ItemEntity lantern : lanterns) {
                    if (!level.isClientSide) {
                        // Server side: Spawn the entity matching the lantern amount
                        ServantOfTheDarkHeartEntity servant = ModEntities.SERVANT_OF_THE_DARK_HEART.get().create(level);
                        if (servant != null) {
                            // Spawn near the player who owns them
                            servant.setPos(player.getX(), player.getY() + 1.0, player.getZ());
                            servant.setOwnerUUID(player.getUUID());
                            servant.setTame(true);
                            servant.targetLanternItem = lantern;
                            level.addFreshEntity(servant);
                        }
                    } else {
                        // Client side: Visual effect to indicate a servant is inbound for this lantern
                        level.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, lantern.getX(), lantern.getY() + 0.5, lantern.getZ(), 0, 0.1, 0);
                    }
                }
            }
        }
    }

    private static int getFreeSlots(Player player) {
        int count = 0;
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            if (player.getInventory().items.get(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }
}
