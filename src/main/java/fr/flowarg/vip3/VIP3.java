package fr.flowarg.vip3;

import fr.flowarg.vip3.client.ClientManager;
import fr.flowarg.vip3.features.OreGeneration;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(VIP3.MOD_ID)
public class VIP3
{
    public static final String MOD_ID = "vip3";
    public static final Logger LOGGER = LogManager.getLogger();

    public VIP3()
    {
        LOGGER.info("Loading VIPMod 3...");
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        VObjects.register(modBus);
        modBus.addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VIPConfig.CLIENT_SPECS);
        MinecraftForge.EVENT_BUS.register(new OreGeneration());
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientManager::new);
        this.editArmor();
    }

    public void setup(FMLCommonSetupEvent event)
    {
        VNetwork.registerPackets();
    }

    private void editArmor()
    {
        try {
            final var rangedAttribute = RangedAttribute.class;
            final var maxValue = rangedAttribute.getDeclaredField("maxValue");
            maxValue.setAccessible(true);
            maxValue.setDouble(Attributes.ARMOR, 60D);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
