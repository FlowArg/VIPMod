package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.altar.data.AtlasData;
import fr.flowarg.vip3.features.altar.data.Serialization;
import fr.flowarg.vip3.features.capabilities.playeratlas.PlayerAtlas;
import fr.flowarg.vip3.features.capabilities.playeratlas.PlayerAtlasCapability;
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

public class PlayerAtlasPacket
{
    private final AtlasData data;
    private final boolean enabled;

    public PlayerAtlasPacket(AtlasData data, boolean enabled)
    {
        this.data = data;
        this.enabled = enabled;
    }

    public PlayerAtlasPacket(@NotNull PlayerAtlas atlas)
    {
        this.data = atlas.atlasData();
        this.enabled = atlas.enabled();
    }

    public static void encode(@NotNull PlayerAtlasPacket packet, @NotNull FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(packet.enabled);
        final var tag = new CompoundTag();
        if(packet.enabled)
            Serialization.serializeAtlas(packet.data, tag);
        buffer.writeNbt(tag);
    }

    @Contract("_ -> new")
    public static @NotNull PlayerAtlasPacket decode(@NotNull FriendlyByteBuf buffer)
    {
        final var enabled = buffer.readBoolean();
        final var atlasData = enabled ? Serialization.deserializeAtlas(buffer.readAnySizeNbt()) : PlayerAtlas.EMPTY;

        return new PlayerAtlasPacket(atlasData, enabled);
    }

    public static void handle(PlayerAtlasPacket pck, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        if(ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
            ctx.get().enqueueWork(() -> handleClientUpdate(pck));
        else ctx.get().enqueueWork(() -> handleServerUpdate(pck, Objects.requireNonNull(ctx.get().getSender())));
        ctx.get().setPacketHandled(true);
    }

    private static void update(PlayerAtlasPacket pck, @NotNull Player player)
    {
        player.getCapability(PlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY).ifPresent(playerAtlas -> {
            playerAtlas.setAtlasData(pck.data);
            playerAtlas.setEnabled(pck.enabled);
        });
    }

    private static void handleServerUpdate(PlayerAtlasPacket pck, ServerPlayer serverPlayer)
    {
        update(pck, serverPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientUpdate(PlayerAtlasPacket pck)
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
                AtomicReference<PlayerAtlas> atlas = new AtomicReference<>(null);
                player.getCapability(PlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY).ifPresent(atlas::set);
                final var get = atlas.get();

                if(get != null)
                    VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PlayerAtlasPacket(get));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
