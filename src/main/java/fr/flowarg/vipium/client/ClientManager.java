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
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Predicate;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientManager
{
    private final RPCManager rpcManager;
    private final VIPKeyBindings keyBindings;
    private final Predicate<Supplier<Supplier<Minecraft>>> startTask;

    public ClientManager()
    {
        this.rpcManager = new RPCManager();
        this.keyBindings = new VIPKeyBindings();
        this.startTask = supplier -> this.rpcManager.startRpc(supplier.get());
    }

    public RPCManager getRpcManager()
    {
        return this.rpcManager;
    }

    public VIPKeyBindings getKeyBindings()
    {
        return this.keyBindings;
    }

    public Predicate<Supplier<Supplier<Minecraft>>> getStartTask()
    {
        return this.startTask;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onGuiOpenedEvent(final GuiOpenEvent event)
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
    public void onKeyInput(final InputEvent.KeyInputEvent event)
    {
        if(VIPMod.clientManager != null && Minecraft.getInstance().world != null)
        {
            if(VIPMod.clientManager.getKeyBindings().getVipiumPureArmorEffectsGUIBinding().isKeyDown())
                Minecraft.getInstance().displayGuiScreen(new VipiumArmorEffectsScreen());
        }
    }
}
