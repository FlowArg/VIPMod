package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.capabilities.atlas.Atlas;
import fr.flowarg.vip3.features.capabilities.atlas.AtlasCapability;
import net.minecraft.client.Minecraft;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class AtlasPacket
{
    private final List<String> connectedAltars;

    public AtlasPacket(List<String> connectedAltars)
    {
        this.connectedAltars = connectedAltars;
    }

    public AtlasPacket(@NotNull Atlas atlas)
    {
        this.connectedAltars = atlas.connectedAltars();
    }

    public static void encode(@NotNull AtlasPacket packet, @NotNull FriendlyByteBuf buffer)
    {
        buffer.writeNbt(Atlas.serializeNBT(packet.connectedAltars));
    }

    @Contract("_ -> new")
    public static @NotNull AtlasPacket decode(@NotNull FriendlyByteBuf buffer)
    {
        return new AtlasPacket(Atlas.deserializeNBT(buffer.readNbt()));
    }

    public static void handle(AtlasPacket pck, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        if(ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
            ctx.get().enqueueWork(() -> handleClientUpdate(pck));
        else ctx.get().enqueueWork(() -> handleServerUpdate(pck, Objects.requireNonNull(ctx.get().getSender())));
        ctx.get().setPacketHandled(true);
    }

    private static void update(AtlasPacket pck, @NotNull Player player)
    {
        player.getCapability(AtlasCapability.ATLAS_CAPABILITY).ifPresent(playerAtlas -> playerAtlas.connectedAltars(pck.connectedAltars));
    }

    private static void handleServerUpdate(AtlasPacket pck, ServerPlayer serverPlayer)
    {
        update(pck, serverPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientUpdate(AtlasPacket pck)
    {
        final var player = Minecraft.getInstance().player;
        assert player != null;
        update(pck, player);
    }

    public static class RequestAtlas
    {
        public static final RequestAtlas CLIENT_REQUEST_ATLAS = new RequestAtlas();

        private RequestAtlas() {}

        public static void handle(RequestAtlas pck, @NotNull Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                final var player = ctx.get().getSender();
                AtomicReference<Atlas> atlas = new AtomicReference<>(null);
                player.getCapability(AtlasCapability.ATLAS_CAPABILITY).ifPresent(atlas::set);
                final var get = atlas.get();

                if(get != null)
                {
                    if(get.connectedAltars() == null)
                        get.connectedAltars(new ArrayList<>());

                    VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new AtlasPacket(get));
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
