# Recipes and Advancements

Integrating your lantern with vanilla systems like crafting and advancements is straightforward.

## Crafting Recipe

The main mod uses shapeless recipes for lanterns. Here is an example for the Emerald Lantern.

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    { "item": "minecraft:lantern" },
    { "item": "minecraft:emerald_block" },
    { "item": "minecraft:ghast_tear" }
  ],
  "result": {
    "item": "myaddon:emerald_lantern"
  }
}
```

## Advancement Trigger

To create an advancement that unlocks when a player obtains your lantern, use the `minecraft:inventory_changed` trigger.

```json
{
  "display": {
    "icon": {
      "item": "myaddon:emerald_lantern"
    },
    "title": {
      "text": "Emerald Enlightenment"
    },
    "description": {
      "text": "Obtain an Emerald Lantern"
    },
    "frame": "task",
    "show_toast": true,
    "announce_to_chat": true,
    "hidden": false
  },
  "criteria": {
    "has_lantern": {
      "trigger": "minecraft:inventory_changed",
      "conditions": {
        "items": [
          {
            "items": [
              "myaddon:emerald_lantern"
            ]
          }
        ]
      }
    }
  }
}
```
