package fr.flowarg.vip3.client;

import fr.flowarg.vip3.client.ass.ASSEngine;
import fr.flowarg.vip3.client.ass.ASSSoundBufferLibrary;
import fr.flowarg.vip3.utils.SidedManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;

@OnlyIn(Dist.CLIENT)
public class ClientManager implements SidedManager
{
    private RPCManager rpcManager;
    private KeyMapping pureVipiumArmorKey;

    @Override
    public void init()
    {
        final var minecraft = Minecraft.getInstance();
        final var clientEventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(clientEventHandler);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(clientEventHandler::clientSetup);
        if(FMLEnvironment.production)
        {
            this.rpcManager = new RPCManager();
            this.rpcManager.startRpc(Minecraft.getInstance());
        }
        this.pureVipiumArmorKey = new KeyMapping("key.configure_effects", 325, "key.categories.vip3");

        try
        {
            if(Files.notExists(ASSSoundBufferLibrary.VIP_SOUNDS))
                Files.createDirectories(ASSSoundBufferLibrary.VIP_SOUNDS);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public RPCManager getRpcManager()
    {
        return this.rpcManager;
    }

    public KeyMapping getConfigureEffectsKey()
    {
        return this.pureVipiumArmorKey;
    }
}
