package me.axlerogue.horrifyinglanterns.api.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public abstract class BaseAbility {
    private final String id;
    private final int cooldownTicks;

    public BaseAbility(String id, int cooldownTicks) {
        this.id = id;
        this.cooldownTicks = cooldownTicks;
    }

    public String getId() {
        return id;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public abstract void execute(ServerPlayer player, ItemStack stack);

    public String getDisplayName() {
        return Component.translatable("ability.horrifyinglanterns." + id).getString();
    }

    protected boolean isOnCooldown(ServerPlayer player, Item item) {
        return player.getCooldowns().isOnCooldown(item);
    }

    protected void startCooldown(ServerPlayer player, Item item) {
        player.getCooldowns().addCooldown(item, cooldownTicks);
        player.displayClientMessage(Component.translatable("message.horrifyinglanterns.ability_cast", player.getName().getString(), getDisplayName()), true);
    }
}
