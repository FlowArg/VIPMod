package fr.flowarg.vipium;

import fr.flowarg.vipium.client.creativetabs.BlocksGroup;
import fr.flowarg.vipium.client.creativetabs.ItemsGroup;
import fr.flowarg.vipium.client.screens.VipiumPurifierScreen;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import fr.flowarg.vipium.common.utils.VipiumConfig;
import fr.flowarg.vipium.common.world.OreGeneration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "vipium";
    private static Main instance;
    private final Marker marker = MarkerManager.getMarker("Vipium");
    private final Logger logger = LogManager.getLogger("Vipium");

    public static final ItemGroup BLOCK_GROUP = new BlocksGroup();
    public static final ItemGroup ITEM_GROUP = new ItemsGroup();

    public Main()
    {
        instance = this;
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupCommon);
        eventBus.addListener(this::setupClient);
        RegistryHandler.init(eventBus);
        ModLoadingContext.get().registerConfig(Type.CLIENT, VipiumConfig.CLIENT_SPECS);
        ModLoadingContext.get().registerConfig(Type.SERVER, VipiumConfig.SERVER_SPECS);
    }

    private void setupCommon(final FMLCommonSetupEvent event)
    {
        this.logger.info(this.marker, "FMLSetup is loading Vipium Mod...");
        this.logger.info(this.marker, "Adding new ores to world generation...");
        OreGeneration.setupGeneration();
    }

    private void setupClient(final FMLClientSetupEvent event)
    {
        this.logger.info(this.marker, "FMLSetup is loading Vipium Mod (Client Side)...");
        //DataGenerators.registerDomain(MODID);
        //DataGenerators.addModelGenerator(new ModelGeneratorImpl());
        this.logger.info(this.marker, "Registering a new screen factory : Vipium Purifier Screen.");
        ScreenManager.registerFactory(RegistryHandler.VIPIUM_PURIFIER_CONTAINER.get(), VipiumPurifierScreen::new);
    }

    public Logger getLogger() { return this.logger; }

    public Marker getMarker()
    {
        return this.marker;
    }

    public static Main getInstance() { return instance; }
}
