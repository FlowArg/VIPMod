package fr.flowarg.vipium;

import fr.flowarg.vipium.client.ClientManager;
import fr.flowarg.vipium.client.creativetabs.BlocksGroup;
import fr.flowarg.vipium.client.creativetabs.ItemsGroup;
import fr.flowarg.vipium.client.renderer.VipiumChestRenderer;
import fr.flowarg.vipium.client.renderer.VipiumMinecartRenderer;
import fr.flowarg.vipium.client.screens.VipiumChestScreen;
import fr.flowarg.vipium.client.screens.VipiumPurifierScreen;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import fr.flowarg.vipium.common.utils.VipiumConfig;
import fr.flowarg.vipium.common.world.OreGeneration;
import fr.flowarg.vipium.server.ServerException;
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
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
    public static final Marker MARKER = MarkerManager.getMarker("Vipium");
    public static final Logger LOGGER = LogManager.getLogger("Vipium");

    public static final ItemGroup BLOCK_GROUP = new BlocksGroup();
    public static final ItemGroup ITEM_GROUP = new ItemsGroup();

    @OnlyIn(Dist.CLIENT)
    public static ClientManager clientManager;

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static ServerManager serverManager;

    public VIPMod()
    {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupCommon);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(this::setupClient);
            clientManager = new ClientManager();
        });
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            try
            {
                serverManager = new ServerManager();
                MinecraftForge.EVENT_BUS.register(serverManager);
            } catch (ServerException e)
            {
                LOGGER.error(MARKER, "The ServerManager initialization encountered a problem.", e);
            }
        });
        RegistryHandler.init(eventBus);
        ModLoadingContext.get().registerConfig(Type.CLIENT, VipiumConfig.CLIENT_SPECS);
    }

    private void setupCommon(final FMLCommonSetupEvent event)
    {
        LOGGER.info(MARKER, "FMLSetup is loading Vipium Mod...");
        LOGGER.info(MARKER, "Adding new ores to world generation...");
        final OreGeneration oreGeneration = new OreGeneration();
        oreGeneration.setupVipiumGeneration();
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        LOGGER.info(MARKER, "FMLSetup is loading Vipium Mod (Client Side)...");

        clientManager.getStartTask().test(event::getMinecraftSupplier);
        ScreenManager.registerFactory(RegistryHandler.VIPIUM_PURIFIER_CONTAINER.get(), VipiumPurifierScreen::new);
        ScreenManager.registerFactory(RegistryHandler.VIPIUM_CHEST_CONTAINER.get(), VipiumChestScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(RegistryHandler.VIPIUM_MINECART_ENTITY.get(), VipiumMinecartRenderer::new);
        ClientRegistry.bindTileEntityRenderer(RegistryHandler.VIPIUM_CHEST_TILE_ENTITY.get(), VipiumChestRenderer::new);
    }
}
