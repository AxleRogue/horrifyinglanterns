# Adding Abilities

Lanterns in Horrifying Lanterns can have two types of active abilities: **Burst** and **Leech**.

## Overriding Ability Methods

To add custom logic when a player presses the ability keys (default 'B' for both lanterns in the core mod, but customizable), you can implement custom methods in your lantern class and trigger them via your own network packets or by hooking into the mod's `LanternAbilityPacket`.

*Note: Since these keys are handled by the main mod's network system, you should check for your item specifically in a capability or event, OR override the usage behavior.*

### Example: Custom Burst Logic

```java
public void performEmeraldBurst(ServerPlayer player, ItemStack stack) {
    if (!isLit(stack)) return;

    // Apply Luck effect to player
    player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 1));
    
    // Play a sound
    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), 
        SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.0F);

    // Add a cooldown
    player.getCooldowns().addCooldown(this, 1200); // 60 seconds
}
```

## Tips for Abilities
- **Check `isLit`**: Always ensure the lantern is enabled before allowing an ability to trigger.
- **Server Side**: Abilities should always be executed on the server side to ensure synchronization.
- **Cooldowns**: Use `player.getCooldowns().addCooldown()` to prevent spamming. The core mod uses 200 ticks (10s) for Burst and 100 ticks (5s) for Leech.
- **User Feedback**: Use `player.displayClientMessage()` to send action bar notifications about ability usage or cooldown status.
