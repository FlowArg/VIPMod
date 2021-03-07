package fr.flowarg.vipium;

import fr.flowarg.vipium.client.ClientManager;
import fr.flowarg.vipium.client.renderer.VIPChestRenderer;
import fr.flowarg.vipium.client.screens.VIPChestScreen;
import fr.flowarg.vipium.client.screens.VipiumPurifierScreen;
import fr.flowarg.vipium.common.core.RegistryHandler;
import fr.flowarg.vipium.common.core.VIPException;
import fr.flowarg.vipium.common.core.VipiumConfig;
import fr.flowarg.vipium.common.creativetabs.BlocksGroup;
import fr.flowarg.vipium.common.creativetabs.ItemsGroup;
import fr.flowarg.vipium.common.world.OreGeneration;
import fr.flowarg.vipium.server.ServerManager;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(VIPMod.MODID)
public class VIPMod
{
    public static final String MODID = "vipium";
    public static final Marker MARKER = MarkerManager.getMarker("V.I.P");
    public static final Logger LOGGER = LogManager.getLogger("V.I.P");

    public static final ItemGroup BLOCK_GROUP = new BlocksGroup();
    public static final ItemGroup ITEM_GROUP = new ItemsGroup();

    @OnlyIn(Dist.CLIENT)
    public static ClientManager clientManager;

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static ServerManager serverManager;

    @OnlyIn(Dist.CLIENT)
    private static boolean isStopping;

    public VIPMod()
    {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        modBus.addListener(this::setupCommon);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            setIsStopping(false);
            modBus.addListener(this::setupClient);
            clientManager = new ClientManager();
            forgeBus.register(clientManager);
            clientManager.getKeyBindings().registerKeyBindings();
        });
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            try
            {
                serverManager = new ServerManager();
                forgeBus.register(serverManager);
            } catch (VIPException e)
            {
                LOGGER.error(MARKER, "The ServerManager initialization encountered a problem.", e);
            }
        });
        RegistryHandler.init(modBus);
        ModLoadingContext.get().registerConfig(Type.CLIENT, VipiumConfig.CLIENT_SPECS);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isStopping()
    {
        return isStopping;
    }

    @OnlyIn(Dist.CLIENT)
    public static void setIsStopping(boolean isStopping)
    {
        VIPMod.isStopping = isStopping;
    }

    private void setupCommon(final FMLCommonSetupEvent event)
    {
        LOGGER.info(MARKER, "FMLSetup is loading V.I.P Mod...");

        final OreGeneration oreGeneration = new OreGeneration();
        oreGeneration.setupVipiumOreGeneration();
        oreGeneration.setupVipiumBlockGeneration();
        oreGeneration.setupVipiumPureBlockGeneration();
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        LOGGER.info(MARKER, "FMLSetup is loading V.I.P Mod (Client Side)...");

        clientManager.getStartTask().test(event::getMinecraftSupplier);
        ScreenManager.registerFactory(RegistryHandler.VIPIUM_PURIFIER_CONTAINER.get(), VipiumPurifierScreen::new);
        ScreenManager.registerFactory(RegistryHandler.VIP_CHEST_CONTAINER.get(), VIPChestScreen::new);
        ClientRegistry.bindTileEntityRenderer(RegistryHandler.VIP_CHEST_TILE_ENTITY.get(), VIPChestRenderer::new);
    }
}
