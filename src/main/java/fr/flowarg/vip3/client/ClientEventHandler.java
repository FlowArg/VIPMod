package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler
{
    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(RenderGameOverlayEvent.@NotNull PreLayer event)
    {
        if(event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT)
        {
            final var minecraft = Minecraft.getInstance();
            final var gui = (ForgeIngameGui)minecraft.gui;

            if(minecraft.options.hideGui || !gui.shouldDrawSurvivalElements()) return;

            this.renderBar(event, gui, minecraft);
            event.setCanceled(true);
        }
    }

    private void renderBar(RenderGameOverlayEvent.PreLayer event, ForgeIngameGui gui, @NotNull Minecraft minecraft)
    {
        if(minecraft.player == null) return;

        gui.setupOverlayRenderState(true, false);

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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKeyInput(final InputEvent.KeyInputEvent event)
    {
        if(VIP3.getClientManager() != null && Minecraft.getInstance().level != null)
        {
            if(VIP3.getClientManager().getConfigureEffectsKey().isDown())
                Minecraft.getInstance().setScreen(new ConfigureEffectsScreen());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT)
            VNetwork.SYNC_CHANNEL.sendToServer(new VArmorConfigurationPacket.VRequestArmorConfiguration());
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(VObjects.VIPIUM_CRUSHER_MENU.get(), VCrusherScreen::new);
        ClientRegistry.registerKeyBinding(VIP3.getClientManager().getConfigureEffectsKey());
    }
}
