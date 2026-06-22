package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.HorrifyingLanterns;
import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.item.client.ModEntities;
import me.axlerogue.horrifyinglanterns.api.entity.ServantOfTheDarkHeartEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HorrifyingLanterns.MODID)
public class TetherHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            if (player.containerMenu instanceof ChestMenu chestMenu) {
                // We need to check if this ChestMenu is an Ender Chest
                // ChestMenu doesn't directly say what container it's for, 
                // but we can check the top inventory slots.
                boolean isEnderChest = false;
                try {
                    // Ender Chests use a specific container class for the top slots
                    // We can check if any slot in the top part belongs to PlayerEnderChestContainer
                    for (int i = 0; i < chestMenu.slots.size(); i++) {
                        Slot slot = chestMenu.getSlot(i);
                        if (slot.container instanceof PlayerEnderChestContainer) {
                            isEnderChest = true;
                            break;
                        }
                    }
                } catch (Exception ignored) {}

                if (isEnderChest) {
                    for (int i = 0; i < chestMenu.slots.size(); i++) {
                        Slot slot = chestMenu.getSlot(i);
                        if (slot.container instanceof PlayerEnderChestContainer) {
                            ItemStack stack = slot.getItem();
                            if (stack.getItem() instanceof LanternBaseItem) {
                                // Eject it back to player inventory or drop it
                                if (!player.getInventory().add(stack.copy())) {
                                    player.drop(stack.copy(), false);
                                }
                                slot.set(ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();
            
            for (int i = 0; i < oldPlayer.getInventory().getContainerSize(); i++) {
                ItemStack stack = oldPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof LanternBaseItem) {
                    if (LanternBaseItem.isOwner(stack, oldPlayer)) {
                        newPlayer.getInventory().add(stack.copy());
                        // We will also handle the drops event to prevent duplication
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player) {
            List<ItemEntity> toRemove = new ArrayList<>();
            for (ItemEntity itemEntity : event.getDrops()) {
                ItemStack stack = itemEntity.getItem();
                if (stack.getItem() instanceof LanternBaseItem) {
                    if (LanternBaseItem.isOwner(stack, player)) {
                        toRemove.add(itemEntity);
                    }
                }
            }
            event.getDrops().removeAll(toRemove);
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getEntity().getItem();
        if (stack.getItem() instanceof LanternBaseItem) {
            if (LanternBaseItem.isOwner(stack, player)) {
                // Return to inventory instead of tossing
                if (!player.getInventory().add(stack)) {
                    // If inventory full, summon servant
                    summonServant(player, stack);
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // This event might not be enough because it doesn't show drops.
        // Drops are usually handled by the block itself.
        // However, we can listen to EntityJoinLevelEvent for ItemEntities being added to the world.
    }

    private static void summonServant(Player player, ItemStack stack) {
        if (!player.level().isClientSide) {
            ServantOfTheDarkHeartEntity servant = ModEntities.SERVANT_OF_THE_DARK_HEART.get().create(player.level());
            if (servant != null) {
                servant.setOwnerUUID(player.getUUID());
                servant.setTame(true);
                servant.setLantern(stack.copy());
                servant.setPos(player.getX(), player.getY(), player.getZ());
                player.level().addFreshEntity(servant);
                stack.setCount(0);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(net.minecraftforge.event.entity.EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            if (stack.getItem() instanceof LanternBaseItem && stack.hasTag() && stack.getTag().contains("Owner")) {
                UUID ownerUUID = stack.getTag().getUUID("Owner");
                Player player = event.getLevel().getPlayerByUUID(ownerUUID);
                if (player != null) {
                    // Try to give to player
                    if (player.getInventory().add(stack.copy())) {
                        event.setCanceled(true);
                    } else {
                        // Inventory full, summon servant
                        summonServant(player, stack);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
