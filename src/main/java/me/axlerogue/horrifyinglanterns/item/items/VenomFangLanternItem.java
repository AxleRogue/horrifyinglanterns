package me.axlerogue.horrifyinglanterns.item.items;

import me.axlerogue.horrifyinglanterns.api.LanternBaseItem;
import me.axlerogue.horrifyinglanterns.api.ability.AbilityType;
import me.axlerogue.horrifyinglanterns.item.ability.PoisonAOEAbility;
import me.axlerogue.horrifyinglanterns.item.ability.SpiderPacificationAbility;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VenomFangLanternItem extends LanternBaseItem {
    public VenomFangLanternItem(Properties properties) {
        super(properties);
        this.lightColor = 0x00FF00; // Green light for poison
        
        this.abilities.put(AbilityType.POISON_AOE, new PoisonAOEAbility());
        this.abilities.put(AbilityType.PACIFY_SPIDERS, new SpiderPacificationAbility());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.venom_fang_info_1").withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.venom_fang_info_2").withStyle(ChatFormatting.DARK_GREEN));
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.venom_fang_info_3").withStyle(ChatFormatting.DARK_GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.horrifyinglanterns.press_shift").withStyle(ChatFormatting.GREEN));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
