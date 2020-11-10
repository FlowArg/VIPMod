package fr.flowarg.vipium.common.handlers;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.client.renderer.VipiumChestRenderer;
import fr.flowarg.vipium.client.screens.CustomInGameMenuScreen;
import fr.flowarg.vipium.client.screens.CustomMainMenuScreen;
import fr.flowarg.vipium.common.utils.VipiumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventHandler
{
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiOpenedEvent(final GuiOpenEvent event)
    {
        if (event.getGui() != null)
        {
            if(!VipiumConfig.CLIENT.canShowRealms().get() && event.getGui().getClass() == MainMenuScreen.class)
                event.setGui(new CustomMainMenuScreen(true));
            else if(!VipiumConfig.CLIENT.canShowUselessOptions().get() && event.getGui().getClass() == IngameMenuScreen.class)
                event.setGui(new CustomInGameMenuScreen(!Minecraft.getInstance().isIntegratedServerRunning()));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event)
    {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS))
            return;

        event.addSprite(VipiumChestRenderer.CHEST_TEXTURE_LOCATION);
    }
}
