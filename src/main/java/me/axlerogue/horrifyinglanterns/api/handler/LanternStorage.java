package me.axlerogue.horrifyinglanterns.api.handler;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LanternStorage extends SimpleContainer {
    private final Player player;

    public LanternStorage(Player player) {
        super(54); // Double chest size for storage
        this.player = player;
        load();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof LanternBaseItem;
    }

    public void load() {
        CompoundTag tag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        if (tag.contains("TetheredHeart", Tag.TAG_LIST)) {
            ListTag list = tag.getList("TetheredHeart", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                int slot = itemTag.getInt("Slot");
                if (slot >= 0 && slot < getContainerSize()) {
                    setItem(slot, ItemStack.of(itemTag));
                }
            }
        }
    }

    public void save() {
        CompoundTag persistedTag = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        ListTag list = new ListTag();
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                stack.save(itemTag);
                list.add(itemTag);
            }
        }
        persistedTag.put("TetheredHeart", list);
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        save();
    }
}
