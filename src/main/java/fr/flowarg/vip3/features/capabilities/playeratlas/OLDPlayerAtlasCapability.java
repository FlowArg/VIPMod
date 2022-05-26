package fr.flowarg.vip3.features.capabilities.playeratlas;

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

@Deprecated
//@Mod.EventBusSubscriber(modid = VIP3.MOD_ID)
public class OLDPlayerAtlasCapability
{
    public static final ResourceLocation PLAYER_ATLAS_CAP_KEY = new ResourceLocation(VIP3.MOD_ID, "player_atlas");
    public static final Capability<OLDPlayerAtlas> PLAYER_ATLAS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private static final Map<Player, OLDPlayerAtlas> INVALIDATED_CAPS = new WeakHashMap<>();

    @SubscribeEvent
    public static void attachToEntities(@NotNull AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof Player player)
        {
            OLDPlayerAtlas holder;
            if(player instanceof ServerPlayer serverPlayer)
                holder = new OLDPlayerPlayerAtlasHolder(serverPlayer);
            else holder = new OLDPlayerAtlasHolder();

            final OLDPlayerPlayerAtlasWrapper wrapper = new OLDPlayerPlayerAtlasWrapper(holder);
            event.addCapability(PLAYER_ATLAS_CAP_KEY, wrapper);
            event.addListener(() -> wrapper.getCapability(PLAYER_ATLAS_CAPABILITY).ifPresent(cap -> INVALIDATED_CAPS.put(player, cap)));
        }
    }

    @SubscribeEvent
    public static void copyCapabilities(PlayerEvent.@NotNull Clone event)
    {
        if(!event.isWasDeath()) return;

        event.getPlayer().getCapability(PLAYER_ATLAS_CAPABILITY).ifPresent(newCapa -> {
            if(!INVALIDATED_CAPS.containsKey(event.getOriginal())) return;
            OLDPlayerAtlas.deserializeNBT(OLDPlayerAtlas.serializeNBT(INVALIDATED_CAPS.get(event.getOriginal())), newCapa);
        });
    }
}