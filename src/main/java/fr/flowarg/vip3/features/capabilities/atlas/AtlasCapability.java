package fr.flowarg.vip3.features.capabilities.atlas;

import fr.flowarg.vip3.VIP3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = VIP3.MOD_ID)
public class AtlasCapability
{
    public static final ResourceLocation ATLAS_CAP_KEY = new ResourceLocation(VIP3.MOD_ID, "atlas");
    public static final Capability<Atlas> ATLAS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private static final Map<Player, Atlas> INVALIDATED_CAPS = new WeakHashMap<>();

    @SubscribeEvent
    public static void attachToEntities(@NotNull AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player player)
        {
            Atlas holder;
            if(player instanceof ServerPlayer serverPlayer)
                holder = new PlayerAtlasHolder(serverPlayer);
            else holder = new AtlasHolder();

            final var wrapper = new PlayerAtlasWrapper(holder);
            event.addCapability(ATLAS_CAP_KEY, wrapper);
            event.addListener(() -> wrapper.getCapability(ATLAS_CAPABILITY).ifPresent(cap -> INVALIDATED_CAPS.put(player, cap)));
        }
    }

    @SubscribeEvent
    public static void copyCapabilities(PlayerEvent.@NotNull Clone event)
    {
        if(!event.isWasDeath()) return;

        event.getPlayer().getCapability(ATLAS_CAPABILITY).ifPresent(newCapa -> {
            if(!INVALIDATED_CAPS.containsKey(event.getOriginal())) return;
            Atlas.deserializeNBT(Atlas.serializeNBT(INVALIDATED_CAPS.get(event.getOriginal())), newCapa);
        });
    }
}
