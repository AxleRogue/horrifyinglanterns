# Creating a Custom Lantern

To create a new lantern, you must extend the `LanternBaseItem` class provided by the API.

## Basic Implementation

Here is an example of a simple lantern that emits a green light.

```java
package com.example.myaddon.item;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.world.item.Item;

public class EmeraldLanternItem extends LanternBaseItem {
    public EmeraldLanternItem() {
        super(new Item.Properties().stacksTo(1));
        // Set light color in ARGB (Green: 0x00FF00)
        this.lightColor = 0x00FF00; 
    }
}
```

## Registration

You need to register your item using Forge's `DeferredRegister`.

```java
public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "myaddon");

public static final RegistryObject<Item> EMERALD_LANTERN = ITEMS.register("emerald_lantern", 
    () -> new EmeraldLanternItem());
```

## What's included?
By extending `LanternBaseItem`, your lantern automatically gets:
- **Dynamic Lighting**: Emits real light at the player's feet when held and toggled ON.
- **Atmospheric Tinting**: Spawns colored particles that tint the ground and nearby entities based on your `lightColor`.
- **Custom Arm Pose**: The player holds it with a unique animation managed by `PlayerAnimationHandler`.
- **Toggle Support**: Works with the global lantern toggle keybind.
