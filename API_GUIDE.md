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

- **Light Emission**: Any item extending `LanternBaseItem` will automatically emit light when held and "lit".
- **Toggle Mechanism**: Use the default keybind (standard is 'G') to toggle your lantern.
- **Custom Light Color**: Define the `lightColor` in your lantern's constructor.
- **Arm Animation**: The player's arm will automatically use the custom lantern holding animation when holding your item.

## Notes

- Make sure to add `horrifyinglanterns` as a dependency in your `mods.toml`.
- The `lit` property refers to the `isLit` NBT tag, which is handled automatically by the `LanternBaseItem.toggle()` method.
