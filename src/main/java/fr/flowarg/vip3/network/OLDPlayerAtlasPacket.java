package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.altar.data.OLDAtlasData;
import fr.flowarg.vip3.features.altar.data.OLDSerialization;
import fr.flowarg.vip3.features.capabilities.playeratlas.OLDPlayerAtlas;
import fr.flowarg.vip3.features.capabilities.playeratlas.OLDPlayerAtlasCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Deprecated
public class OLDPlayerAtlasPacket
{
    private final OLDAtlasData data;
    private final boolean enabled;

    public OLDPlayerAtlasPacket(OLDAtlasData data, boolean enabled)
    {
        this.data = data;
        this.enabled = enabled;
    }

    public OLDPlayerAtlasPacket(@NotNull OLDPlayerAtlas atlas)
    {
        this.data = atlas.atlasData();
        this.enabled = atlas.enabled();
    }

    public static void encode(@NotNull OLDPlayerAtlasPacket packet, @NotNull FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(packet.enabled);
        final var tag = new CompoundTag();
        if(packet.enabled)
            OLDSerialization.serializeAtlas(packet.data, tag);
        buffer.writeNbt(tag);
    }

    @Contract("_ -> new")
    public static @NotNull OLDPlayerAtlasPacket decode(@NotNull FriendlyByteBuf buffer)
    {
        final var enabled = buffer.readBoolean();
        final var atlasData = enabled ? OLDSerialization.deserializeAtlas(buffer.readAnySizeNbt()) : OLDPlayerAtlas.EMPTY;

        return new OLDPlayerAtlasPacket(atlasData, enabled);
    }

    public static void handle(OLDPlayerAtlasPacket pck, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        if(ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
            ctx.get().enqueueWork(() -> handleClientUpdate(pck));
        else ctx.get().enqueueWork(() -> handleServerUpdate(pck, Objects.requireNonNull(ctx.get().getSender())));
        ctx.get().setPacketHandled(true);
    }

    private static void update(OLDPlayerAtlasPacket pck, @NotNull Player player)
    {
        player.getCapability(OLDPlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY).ifPresent(playerAtlas -> {
            playerAtlas.setAtlasData(pck.data);
            playerAtlas.setEnabled(pck.enabled);
        });
    }

    private static void handleServerUpdate(OLDPlayerAtlasPacket pck, ServerPlayer serverPlayer)
    {
        update(pck, serverPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientUpdate(OLDPlayerAtlasPacket pck)
    {
        final var player = Minecraft.getInstance().player;
        assert player != null;
        update(pck, player);
    }

    public static class RequestPlayerAtlas
    {
        public static void encode(@NotNull RequestPlayerAtlas pck, @NotNull FriendlyByteBuf byteBuf) {}

        public static @NotNull RequestPlayerAtlas decode(@NotNull FriendlyByteBuf byteBuf)
        {
            return new RequestPlayerAtlas();
        }

        public static void handle(RequestPlayerAtlas pck, @NotNull Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                final var player = ctx.get().getSender();
                AtomicReference<OLDPlayerAtlas> atlas = new AtomicReference<>(null);
                player.getCapability(OLDPlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY).ifPresent(atlas::set);
                final var get = atlas.get();

                if(get != null)
                    VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new OLDPlayerAtlasPacket(get));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
