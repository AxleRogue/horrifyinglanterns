# Horrifying Lanterns API

Welcome to the Horrifying Lanterns API! This API allows you to create your own custom lanterns and add them to the mod.

## Getting Started

To create a custom lantern, follow these steps:

### 1. Create your Lantern Class

Your lantern class must extend `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`. You should register your lantern's abilities in the constructor using `registerAbility()`.

```java
public class MyCustomLanternItem extends LanternBaseItem {
    public MyCustomLanternItem(Properties properties) {
        super(properties);
        this.lightColor = 0x00FF00; // Set your lantern's light color (ARGB)
        
        // Register your abilities
        registerAbility(AbilityType.BURST, new MyCustomBurstAbility());
    }
}
```

### 2. Create your Ability Class

Extend `me.axlerogue.horrifyinglanterns.api.ability.BaseAbility` to create custom functionality.

```java
public class MyCustomBurstAbility extends BaseAbility {
    public MyCustomBurstAbility() {
        super("my_custom_burst", 200); // ID and cooldown in ticks (10s)
    }

    @Override
    public void execute(Player player, ItemStack stack) {
        // Your logic here
        player.sendSystemMessage(Component.literal("Custom Ability Used!"));
    }
}
```

### 3. Register your Lantern

Register your lantern using standard Forge `DeferredRegister`.

```java
public static final RegistryObject<Item> MY_CUSTOM_LANTERN = ITEMS.register("my_custom_lantern", 
    () -> new MyCustomLanternItem(new Item.Properties().stacksTo(1)));
```

## Features provided by the API

- **Light Emission**: Any item extending `LanternBaseItem` will automatically emit light when held and "lit".
- **Ability System**: Use `BaseAbility` and `AbilityType` (BURST, LEECH, SUMMON, WRATH) to add complex behaviors. Cooldowns and action bar messages are managed by the base classes.
- **Base Entities**: Extend `BaseEntity` or `BaseLivingEntity` for custom lantern-related entities.
- **Base Renderers**: Extend `BaseEntityRenderer` or `BaseLivingEntityRenderer` for your entity's visuals.
- **Base KeyMappings**: Use `BaseKeyMapping` for custom input handling.
- **Toggle Mechanism**: Use the default keybind (standard is 'L') to toggle your lantern.
- **Ownership System**: Automatically binds the lantern to the first player who uses it, restricting abilities and toggling strictly to that owner.
- **Arm Animation**: The player's arm (and humanoid entities like `ServantOfTheDarkHeartEntity`) will automatically use the custom lantern holding animation when carrying a `LanternBaseItem`.
- **Entity Tinting**: The API automatically handles tinting nearby mobs with colored particles.
- **Servant Entities**: A custom spectral servant system `ServantOfTheDarkHeartEntity` is retained to interact with dropped items dynamically. Spawning is handled automatically by `ServantSpawnHandler`.
- **Minion Alliance**: Entities extending `BaseLivingEntity` override `isAlliedTo` and `hurt` to ensure minions do not attack each other and owners cannot accidentally deal friendly fire.
- **Re-equip Animation Fix**: The `LanternBaseItem` overrides `shouldCauseReequipAnimation` to prevent the item from constantly bobbing when its background NBT data updates (like the auto-extinguish tick counter).

## Advanced Integration

### Key Mappings
You can access the mod's key mappings via `LanternKeyMappings`:
- `TOGGLE_LANTERN`
- `LANTERN_BURST`
- `LANTERN_LEECH`
- `LANTERN_SUMMON`
- `LANTERN_WRATH`

### Base Classes for Extension
- `BaseAbility`: Logic for lantern powers.
- `BaseEntity` / `BaseLivingEntity`: Custom entities.
- `BaseEntityRenderer` / `BaseLivingEntityRenderer`: Entity rendering.
- `BaseKeyMapping`: Custom keybinds.

## Notes

- Make sure to add `horrifyinglanterns` as a dependency in your `mods.toml`.
- The `lit` property refers to the `isLit` NBT tag, which is handled automatically by the `LanternBaseItem.toggle()` method.
