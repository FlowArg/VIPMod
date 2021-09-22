package fr.flowarg.vip3;

import fr.flowarg.vip3.client.ClientManager;
import fr.flowarg.vip3.features.OreGeneration;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.capabilities.ArmorConfiguration;
import fr.flowarg.vip3.network.capabilities.ArmorConfigurationCapability;
import fr.flowarg.vip3.server.ServerManager;
import fr.flowarg.vip3.utils.SidedManager;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(VIP3.MOD_ID)
public class VIP3
{
    public static final String MOD_ID = "vip3";
    public static final Logger LOGGER = LogManager.getLogger();

    @OnlyIn(Dist.CLIENT)
    private static ClientManager clientManager;

    @OnlyIn(Dist.DEDICATED_SERVER)
    private static ServerManager serverManager;

    private static SidedManager currentManager;

    public VIP3()
    {
        LOGGER.info("Loading VIP 3...");
        final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        VObjects.register(modBus);
        modBus.addListener(this::setup);
        modBus.addListener(this::registerCapabilities);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VIPConfig.CLIENT_SPECS);
        MinecraftForge.EVENT_BUS.register(new OreGeneration());
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> clientManager = new ClientManager());
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> serverManager = new ServerManager());
        currentManager = DistExecutor.unsafeRunForDist(() -> VIP3::getClientManager, () -> VIP3::getServerManager);
        getCurrentManager().init();
        this.editArmor();
    }

    public void setup(FMLCommonSetupEvent event)
    {
        VNetwork.registerPackets();
    }

    public void registerCapabilities(@NotNull RegisterCapabilitiesEvent event)
    {
        event.register(ArmorConfiguration.class);
    }


    @OnlyIn(Dist.CLIENT)
    public static ClientManager getClientManager()
    {
        return clientManager;
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static ServerManager getServerManager()
    {
        return serverManager;
    }

    public static SidedManager getCurrentManager()
    {
        return currentManager;
    }

    private void editArmor()
    {
        try {
            final var rangedAttribute = RangedAttribute.class;
            final var maxValue = rangedAttribute.getDeclaredField(FMLEnvironment.production ? "f_22308_" : "maxValue");
            maxValue.setAccessible(true);
            maxValue.setDouble(Attributes.ARMOR, 60D);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
