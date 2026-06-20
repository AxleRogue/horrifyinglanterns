# Horrifying Lanterns API

Welcome to the Horrifying Lanterns API! This API allows you to create your own custom lanterns and add them to the mod.

## Getting Started

To create a custom lantern, follow these steps:

### 1. Create your Lantern Class

Your lantern class must extend `me.axlerogue.horrifyinglanterns.api.LanternBaseItem`.

```java
public class MyCustomLanternItem extends LanternBaseItem {
    public MyCustomLanternItem(Properties properties) {
        super(properties);
        this.lightColor = 0x00FF00; // Set your lantern's light color (ARGB)
    }

    // You can override methods like appendHoverText to add tooltips
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.literal("My Custom Lantern Info").withStyle(ChatFormatting.GREEN));
    }
}
```

### 2. Register your Lantern

Register your lantern using standard Forge `DeferredRegister`.

```java
public static final RegistryObject<Item> MY_CUSTOM_LANTERN = ITEMS.register("my_custom_lantern", 
    () -> new MyCustomLanternItem(new Item.Properties().stacksTo(1)));
```

### 3. Register Client-side Properties (Optional)

To make your lantern visually change when toggled on/off, you should register the `lit` property in your mod's client setup.

```java
ItemProperties.register(MY_CUSTOM_LANTERN.get(),
    new ResourceLocation("horrifyinglanterns", "lit"),
    (stack, level, entity, seed) -> LanternBaseItem.isLit(stack) ? 1.0F : 0.0F);
```

## Features provided by the API

- **Light Emission**: Any item extending `LanternBaseItem` will automatically emit light when held and "lit". Handled by `me.axlerogue.horrifyinglanterns.api.handler.LightHandler`.
- **Toggle Mechanism**: Use the default keybind (standard is 'L') to toggle your lantern. Key mappings are available in `me.axlerogue.horrifyinglanterns.api.client.LanternKeyMappings`.
- **Abilities**: Custom lanterns can trigger abilities using `me.axlerogue.horrifyinglanterns.api.ability.AbilityType` (BURST, LEECH). Cooldowns and action bar messages are handled automatically.
- **Custom Light Color**: Define the `lightColor` in your lantern's constructor. Handled by `me.axlerogue.horrifyinglanterns.api.handler.LightColorHandler`.
- **Arm Animation**: The player's arm will automatically use the custom lantern holding animation. Handled by `me.axlerogue.horrifyinglanterns.api.handler.PlayerAnimationHandler`.
- **Entities**: Base entity classes like `DarkOnesEntity` and `BlueLightningBolt` are available in `me.axlerogue.horrifyinglanterns.api.entity`.
- **Entity Tinting**: The API automatically handles tinting nearby mobs and animals with colored particles when a player holds a lit lantern.

## Advanced Integration

### Key Mappings
You can access the mod's key mappings via `LanternKeyMappings`:
- `TOGGLE_LANTERN`
- `LANTERN_BURST`
- `LANTERN_LEECH`

### Handlers
The API provides several handlers that you can use or reference:
- `LightHandler`: Manages block-based dynamic lighting and entity particle effects.
- `LightColorHandler`: Manages colored lighting effects and provides color/intensity data.
- `LanternInteractionHandler`: Manages player interactions and restrictions while holding lanterns.
- `PlayerAnimationHandler`: Manages custom arm animations.
- `SanguineEffectHandler`: Manages special effects for sanguine-themed items.

## Notes

- Make sure to add `horrifyinglanterns` as a dependency in your `mods.toml`.
- The `lit` property refers to the `isLit` NBT tag, which is handled automatically by the `LanternBaseItem.toggle()` method.
