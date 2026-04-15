package com.leclowndu93150.leaderboards.mixin;

import dev.ftb.mods.ftblibrary.client.gui.widget.SimpleButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "dev.ftb.mods.ftblibrary.client.gui.screens.AbstractThreePanelScreen$TopPanel")
public interface TopPanelAccessor {
    @Accessor("closeButton")
    SimpleButton leaderboards$getCloseButton();
}
