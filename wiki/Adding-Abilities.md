# Adding Abilities

Lanterns in Horrifying Lanterns use a modular ability system. Each lantern can register multiple abilities mapped to an `AbilityType`.

## The Ability System

To add an ability to your custom lantern:

1.  **Create a class** that extends `me.axlerogue.horrifyinglanterns.api.ability.BaseAbility`.
2.  **Implement the `execute` method** with your custom logic.
3.  **Register the ability** in your lantern's constructor using `registerAbility()`.

### Example: Custom Ability Class

```java
public class EmeraldLuckAbility extends BaseAbility {
    public EmeraldLuckAbility() {
        super("emerald_luck", 1200); // ID and cooldown (60s)
    }

    @Override
    public void execute(Player player, ItemStack stack) {
        // Apply Luck effect to player
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 1));
        
        // Play a sound
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), 
            SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
```

### Registering in your Lantern

```java
public class EmeraldLanternItem extends LanternBaseItem {
    public EmeraldLanternItem(Properties properties) {
        super(properties);
        registerAbility(AbilityType.BURST, new EmeraldLuckAbility());
    }
}
```

## Ability Types

The API provides four standard ability types, each with its own default keybinding:
- `BURST` (Key: V)
- `LEECH` (Key: B)
- `SUMMON` (Key: V - used by Dark Skies style lanterns)
- `WRATH` (Key: B - used by Dark Skies style lanterns)

## Tips for Abilities
- **Base Logic**: `BaseAbility` automatically handles cooldown management and checking if the lantern is enabled.
- **Action Bar Messages**: All feedback (cooldowns, usage) is automatically sent to the player's action bar.
- **Server Side**: The `execute` method is called on the server via the mod's networking system.
