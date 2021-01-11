package fr.flowarg.vipium.client;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.client.screens.CustomInGameMenuScreen;
import fr.flowarg.vipium.client.screens.CustomMainMenuScreen;
import fr.flowarg.vipium.client.screens.VipiumArmorEffectsScreen;
import fr.flowarg.vipium.common.core.VipiumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = VIPMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventHandler
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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onKeyInput(final KeyInputEvent event)
    {
        if(VIPMod.clientManager != null && Minecraft.getInstance().world != null)
        {
            if(VIPMod.clientManager.getKeyBindings().getVipiumPureArmorEffectsGUIBinding().isKeyDown())
                Minecraft.getInstance().displayGuiScreen(new VipiumArmorEffectsScreen());
        }
    }
}
