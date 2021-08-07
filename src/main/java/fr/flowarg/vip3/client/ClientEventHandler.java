package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler
{
    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(RenderGameOverlayEvent.PreLayer event)
    {
        if(event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT)
        {
            final var minecraft = Minecraft.getInstance();
            final var gui = (ForgeIngameGui)minecraft.gui;

            if(minecraft.options.hideGui || !gui.shouldDrawSurvivalElements()) return;

            gui.setupOverlayRenderState(true, false);
            this.renderBar(event, gui, minecraft);
            event.setCanceled(true);
        }
    }

    private void renderBar(RenderGameOverlayEvent.PreLayer event, ForgeIngameGui gui, Minecraft minecraft)
    {
        RenderSystem.enableBlend();

        final var guiScaledWidth = event.getWindow().getGuiScaledWidth();
        var left = guiScaledWidth / 2 - 91;
        final var guiScaledHeight = event.getWindow().getGuiScaledHeight();
        final var firstBar = guiScaledHeight - gui.left_height;
        final var secondBar = guiScaledHeight - gui.left_height - 10;
        final var thirdBar = guiScaledHeight - gui.left_height - 20;
        final var mStack = event.getMatrixStack();

        final var level = minecraft.player.getArmorValue();

        for (int i = 1; level > 0 && i < (level <= 20 ? 20 : level <= 40 ? 40 : 60); i += 2)
        {
            final var usedBar = i < 20 ? firstBar : i < 40 ? secondBar : thirdBar;

            if(i == 21 || i == 41) left = guiScaledWidth / 2 - 91;

            if (i < level) gui.blit(mStack, left, usedBar, 34, 9, 9, 9);
            else if (i == level) gui.blit(mStack, left, usedBar, 25, 9, 9, 9);
            else gui.blit(mStack, left, usedBar, 16, 9, 9, 9);

            left += 8;
        }

        gui.left_height += 10;

        RenderSystem.disableBlend();
    }
}
