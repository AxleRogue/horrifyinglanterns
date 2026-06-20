# Models and Textures

The mod uses a property predicate to switch between "lit" and "extinguished" models.

## Item Model JSON

Your base item model should look like this (e.g., `emerald_lantern.json`):

```json
{
  "parent": "myaddon:item/emerald_lantern_extinguished",
  "overrides": [
    {
      "predicate": {
        "horrifyinglanterns:lit": 1
      },
      "model": "myaddon:item/emerald_lantern_lit"
    }
  ]
}
```

## Predicate Registration

In your Client Setup event, you must register the `lit` property for your item so the model knows when to switch.

```java
@SubscribeEvent
public static void onClientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(() -> {
        ItemProperties.register(ModItems.EMERALD_LANTERN.get(),
            new ResourceLocation("horrifyinglanterns", "lit"),
            (stack, level, entity, seed) -> LanternBaseItem.isLit(stack) ? 1.0F : 0.0F);
    });
}
```

## Texture Recommendations
- **Extinguished**: A dull or dark version of the lantern.
- **Lit**: A bright, glowing version. You may want to use an emissive layer for the flame/glow part.
