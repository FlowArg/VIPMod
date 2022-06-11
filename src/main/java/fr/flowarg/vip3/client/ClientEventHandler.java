package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.client.ass.ASSSound;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.network.AtlasPacket;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler
{
    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(RenderGameOverlayEvent.@NotNull PreLayer event)
    {
        if(event.getOverlay() != ForgeIngameGui.ARMOR_LEVEL_ELEMENT)
            return;

        final var minecraft = Minecraft.getInstance();
        final var gui = (ForgeIngameGui)minecraft.gui;

        if(minecraft.options.hideGui || !gui.shouldDrawSurvivalElements()) return;

        this.renderBar(event, gui, minecraft);
        event.setCanceled(true);
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
        if(VIP3.getClientManager() == null || Minecraft.getInstance().level == null) return;

        if(VIP3.getClientManager().getConfigureEffectsKey().isDown())
            Minecraft.getInstance().setScreen(new ConfigureEffectsScreen());
    }

    @SubscribeEvent
    public void onTick(TickEvent.@NotNull PlayerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT)
        {
            VNetwork.SYNC_CHANNEL.sendToServer(VArmorConfigurationPacket.REQUEST_PACKET_INSTANCE);
            VNetwork.SYNC_CHANNEL.sendToServer(AtlasPacket.CLIENT_REQUEST_ATLAS);
        }
    }

    public void clientSetup(FMLClientSetupEvent event)
    {
        ItemProperties.register(VObjects.VIPIUM_BOW.get(), new ResourceLocation(VIP3.MOD_ID, "pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) return 0.0F;
            else return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration() - p_174637_.getUseItemRemainingTicks()) / 20.0F;
        });
        ItemProperties.register(VObjects.VIPIUM_BOW.get(), new ResourceLocation(VIP3.MOD_ID, "pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F);
        MenuScreens.register(VObjects.VIPIUM_CRUSHER_MENU.get(), VCrusherScreen::new);
        ClientRegistry.registerKeyBinding(VIP3.getClientManager().getConfigureEffectsKey());
    }

    @SubscribeEvent
    public void editFov(FOVModifierEvent event)
    {
        final var player = event.getEntity();
        float f = 1.0F;

        if (player.getAbilities().flying)
            f *= 1.1F;

        f *= ((float)player.getAttributeValue(Attributes.MOVEMENT_SPEED) / player.getAbilities().getWalkingSpeed() + 1.0F) / 2.0F;

        if (player.getAbilities().getWalkingSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f))
            f = 1.0F;

        final var itemStack = player.getUseItem();

        if (!player.isUsingItem())
        {
            event.setNewfov(f);
            return;
        }

        if (itemStack.is(Items.BOW) || itemStack.is(VObjects.VIPIUM_BOW.get()))
        {
            int i = player.getTicksUsingItem();
            float f1 = (float)i / 20.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 *= f1;
            }

            f *= 1.0F - f1 * 0.15F;
            event.setNewfov(f);
            return;
        }
        else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping())
        {
            event.setNewfov(0.1F);
            return;
        }

        event.setNewfov(f);
    }
}
