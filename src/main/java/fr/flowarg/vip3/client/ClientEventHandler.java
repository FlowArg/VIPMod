package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.network.AtlasPacket;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientEventHandler
{
    public static boolean musicState = false;
    public static final List<String> SOUND_FILE_ERROR = new ArrayList<>();

    @SubscribeEvent
    public void onPreRenderGameOverlayEvent(@NotNull RenderGameOverlayEvent.PreLayer event)
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
    public void onKeyInput(final InputEvent.@NotNull KeyInputEvent event)
    {
        final var minecraft = Minecraft.getInstance();

        if(minecraft.player == null &&
                event.getAction() == GLFW.GLFW_PRESS &&
                event.getModifiers() == 7 &&
                event.getKey() == GLFW.GLFW_KEY_S)
            minecraft.setScreen(new VSetupScreen());

        if(event.getAction() == GLFW.GLFW_RELEASE)
        {
            if(event.getScanCode() == -1)
                return;

            if(minecraft.screen instanceof PauseScreen && minecraft.hasSingleplayerServer() && !minecraft.getSingleplayerServer().isPublished())
                return;

            if(event.getScanCode() == VIPConfig.CLIENT.getPauseMediaKey().get())
            {
                if (!musicState) minecraft.getSoundManager().pause();
                else minecraft.getSoundManager().resume();

                musicState = !musicState;
                return;
            }

            if(event.getScanCode() == VIPConfig.CLIENT.getStopMediaKey().get())
            {
                minecraft.getSoundManager().stop();
                musicState = true;
                return;
            }

            if(event.getScanCode() == VIPConfig.CLIENT.getSkipMediaKey().get())
            {
                minecraft.getSoundManager().stop();
                return;
            }
        }

        final var clientManager = VIP3.getClientManager();

        if(clientManager == null) return;
        if(minecraft.level == null) return;

        if(clientManager.getConfigureEffectsKey().isDown())
            minecraft.setScreen(new VConfigureEffectsScreen());
    }

    @SubscribeEvent
    public void onTick(TickEvent.@NotNull PlayerTickEvent event)
    {
        if(event.side == LogicalSide.CLIENT)
        {
            VNetwork.SYNC_CHANNEL.sendToServer(VArmorConfigurationPacket.REQUEST_PACKET_INSTANCE);
            VNetwork.SYNC_CHANNEL.sendToServer(AtlasPacket.RequestAtlas.CLIENT_REQUEST_ATLAS);
        }
    }

    public void clientSetup(FMLClientSetupEvent ignored)
    {
        ItemProperties.register(VObjects.VIPIUM_BOW.get(), new ResourceLocation(VIP3.MOD_ID, "pull"), (stack, level, entity, seed) -> {
            if (entity == null) return 0.0F;
            else return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
        });
        ItemProperties.register(VObjects.VIPIUM_BOW.get(), new ResourceLocation(VIP3.MOD_ID, "pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        MenuScreens.register(VObjects.VIPIUM_CRUSHER_MENU.get(), VCrusherScreen::new);
        MenuScreens.register(VObjects.VIPIUM_PURIFIER_MENU.get(), VPurifierScreen::new);
        ClientRegistry.registerKeyBinding(VIP3.getClientManager().getConfigureEffectsKey());
    }

    @SubscribeEvent
    public void editFov(@NotNull FOVModifierEvent event)
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

    @SubscribeEvent
    public void replaceScreens(@NotNull ScreenOpenEvent event)
    {
        if(event.getScreen() == null)
            return;

        if(event.getScreen() instanceof TitleScreen)
        {
            if(VIPConfig.CLIENT.getFirstLaunch().get())
                event.setScreen(new VSetupScreen());
            else
            {
                try
                {
                    VIP3.getClientManager().checkVIPSoundsDir();
                    if (!ClientEventHandler.SOUND_FILE_ERROR.isEmpty())
                        event.setScreen(new VErrorScreen());
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }

            }
        }

        if(event.getScreen().getClass() == SoundOptionsScreen.class)
        {
            final var screen = (SoundOptionsScreen)event.getScreen();
            event.setScreen(new VSoundOptionsScreen(screen.lastScreen, screen.options));
        }
    }
}
