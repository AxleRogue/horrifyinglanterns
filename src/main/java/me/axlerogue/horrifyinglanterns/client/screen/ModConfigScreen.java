package me.axlerogue.horrifyinglanterns.client.screen;

import me.axlerogue.horrifyinglanterns.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ModConfigScreen extends Screen {
    private final Screen lastScreen;

    public ModConfigScreen(Screen lastScreen) {
        super(Component.translatable("screen.horrifyinglanterns.config.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        super.init();

        int y = this.height / 4 + 24;
        
        // Light Level Button
        this.addRenderableWidget(Button.builder(Component.translatable("screen.horrifyinglanterns.config.light_level", Config.LIGHT_LEVEL.get()), (button) -> {
            int current = Config.LIGHT_LEVEL.get();
            int next = (current + 1) % 16;
            Config.LIGHT_LEVEL.set(next);
            Config.lightLevel = next; // Update cached value
            button.setMessage(Component.translatable("screen.horrifyinglanterns.config.light_level", next));
        }).pos(this.width / 2 - 100, y).size(200, 20).build());

        // Light Radius Button
        this.addRenderableWidget(Button.builder(Component.translatable("screen.horrifyinglanterns.config.light_radius", Config.LIGHT_RADIUS.get()), (button) -> {
            double current = Config.LIGHT_RADIUS.get();
            double next = current + 1.0;
            if (next > 16.0) next = 1.0;
            Config.LIGHT_RADIUS.set(next);
            Config.lightRadius = next; // Update cached value
            button.setMessage(Component.translatable("screen.horrifyinglanterns.config.light_radius", next));
        }).pos(this.width / 2 - 100, y + 25).size(200, 20).build());

        // Auto-Extinguish Button
        this.addRenderableWidget(Button.builder(Component.translatable("screen.horrifyinglanterns.config.auto_extinguish", Config.AUTO_EXTINGUISH_MINUTES.get()), (button) -> {
            int current = Config.AUTO_EXTINGUISH_MINUTES.get();
            int next = current + 1;
            if (next > 60) next = 1;
            Config.AUTO_EXTINGUISH_MINUTES.set(next);
            Config.autoExtinguishMinutes = next;
            button.setMessage(Component.translatable("screen.horrifyinglanterns.config.auto_extinguish", next));
        }).pos(this.width / 2 - 100, y + 50).size(200, 20).build());

        // Done button
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), (button) -> {
            Config.SPEC.save();
            this.minecraft.setScreen(this.lastScreen);
        }).pos(this.width / 2 - 100, y + 75).size(200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Config.SPEC.save();
        this.minecraft.setScreen(this.lastScreen);
    }
}
