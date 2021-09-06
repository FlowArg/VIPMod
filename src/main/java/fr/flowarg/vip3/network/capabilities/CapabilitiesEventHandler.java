package fr.flowarg.vip3.network.capabilities;

import fr.flowarg.vip3.VIP3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class CapabilitiesEventHandler
{
    @CapabilityInject(ArmorConfiguration.class)
    public static final Capability<ArmorConfiguration> ARMOR_CONFIGURATION_CAPABILITY = null;

    public static final ResourceLocation ARMOR_CONFIGURATION_CAP_KEY = new ResourceLocation(VIP3.MOD_ID, "armor_configuration");
    private static final Map<Player, ArmorConfiguration> INVALIDATED_CAPS = new WeakHashMap<>();

    public static void registerCapabilities(@NotNull RegisterCapabilitiesEvent event)
    {
        event.register(ArmorConfiguration.class);
    }

    @SubscribeEvent
    public static void attachToEntities(@NotNull AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player player)
        {
            ArmorConfigurationWrapper wrapper;
            if(player instanceof ServerPlayer serverPlayer)
                wrapper = new ServerArmorConfigurationWrapper(serverPlayer);
            else wrapper = new ArmorConfigurationWrapper();

            event.addCapability(ARMOR_CONFIGURATION_CAP_KEY, wrapper);
            event.addListener(() -> wrapper.getCapability(ARMOR_CONFIGURATION_CAPABILITY).ifPresent(cap -> INVALIDATED_CAPS.put(player, cap)));
        }
    }

    @SubscribeEvent
    public static void copyCapabilities(PlayerEvent.@NotNull Clone event)
    {
        if(!event.isWasDeath()) return;

        event.getPlayer().getCapability(ARMOR_CONFIGURATION_CAPABILITY).ifPresent(newCapa -> {
            if(!INVALIDATED_CAPS.containsKey(event.getOriginal())) return;

            final var nbt = INVALIDATED_CAPS.get(event.getOriginal()).serializeNBT();
            newCapa.deserializeNBT(nbt);
        });
    }

}
