package fr.flowarg.vip3;

import fr.flowarg.vip3.client.ClientManager;
import fr.flowarg.vip3.features.OreGeneration;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.features.capabilities.armorconfiguration.ArmorConfiguration;
import fr.flowarg.vip3.features.capabilities.atlas.Atlas;
import fr.flowarg.vip3.features.commands.DontExecuteThisCommand;
import fr.flowarg.vip3.features.commands.VCommands;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.server.ServerManager;
import fr.flowarg.vip3.utils.SidedManager;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(VIP3.MOD_ID)
public final class VIP3
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
        this.loadFMlStuff();
        this.loadForgeStuff();
        this.setupManager();
        this.editArmor();
    }

    private void loadFMlStuff()
    {
        final var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        VObjects.register(modBus);
        modBus.addListener(this::setup);
        modBus.addListener(this::registerCapabilities);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, VIPConfig.CLIENT_SPECS);
    }

    private void loadForgeStuff()
    {
        final var forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(new OreGeneration());
        forgeBus.addListener(VCommands::registerCommands);
        forgeBus.addListener(this::onAxeCrafted);
        forgeBus.addListener(this::onRightClickOnSign);
    }

    private void setupManager()
    {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> clientManager = new ClientManager());
        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> serverManager = new ServerManager());
        currentManager = DistExecutor.unsafeRunForDist(() -> VIP3::getClientManager, () -> VIP3::getServerManager);
        getCurrentManager().init();
    }

    public void setup(FMLCommonSetupEvent event)
    {
        VNetwork.registerPackets();
    }

    public void registerCapabilities(@NotNull RegisterCapabilitiesEvent event)
    {
        event.register(ArmorConfiguration.class);
        event.register(Atlas.class);
    }

    public void onAxeCrafted(@NotNull PlayerEvent.ItemCraftedEvent event)
    {
        if (event.getCrafting().getItem() instanceof AxeItem)
            event.getPlayer().playSound(VObjects.BUCHERON_SOUND_EVENT.get(), 1F, 1F);
    }

    public void onRightClickOnSign(PlayerInteractEvent.@NotNull RightClickBlock event)
    {
        final var world = event.getWorld();
        final var block = world.getBlockEntity(event.getPos());

        if(!(block instanceof SignBlockEntity sign))
            return;

        for (int i = 0; i < 3; i++)
        {
            final var text = sign.getMessage(i, false);
            if(text.getString().contains("Don't click here"))
            {
                DontExecuteThisCommand.applyDontExecuteThisCommand(event.getPlayer());
                return;
            }
        }
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
            final var maxValue = ObfuscationReflectionHelper.findField(RangedAttribute.class, "f_22308_");
            maxValue.setDouble(Attributes.ARMOR, 60D);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
