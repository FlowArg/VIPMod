package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.capabilities.armorconfiguration.ArmorConfiguration;
import fr.flowarg.vip3.features.capabilities.armorconfiguration.ArmorConfigurationCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class VArmorConfigurationPacket
{
    private final boolean helmetEffect;
    private final boolean chestPlateEffect;
    private final boolean leggingsEffect;
    private final boolean bootsEffect;
    private final boolean fullSet1Effect;
    private final boolean fullSet2Effect;

    public VArmorConfigurationPacket(boolean helmetEffect, boolean chestPlateEffect, boolean leggingsEffect, boolean bootsEffect, boolean fullSet1Effect, boolean fullSet2Effect)
    {
        this.helmetEffect = helmetEffect;
        this.chestPlateEffect = chestPlateEffect;
        this.leggingsEffect = leggingsEffect;
        this.bootsEffect = bootsEffect;
        this.fullSet1Effect = fullSet1Effect;
        this.fullSet2Effect = fullSet2Effect;
    }

    public VArmorConfigurationPacket(@NotNull ArmorConfiguration armorConfiguration)
    {
        this.helmetEffect = armorConfiguration.helmetEffect();
        this.chestPlateEffect = armorConfiguration.chestPlateEffect();
        this.leggingsEffect = armorConfiguration.leggingsEffect();
        this.bootsEffect = armorConfiguration.bootsEffect();
        this.fullSet1Effect = armorConfiguration.fullSet1Effect();
        this.fullSet2Effect = armorConfiguration.fullSet2Effect();
    }

    public static void encode(@NotNull VArmorConfigurationPacket pck, @NotNull FriendlyByteBuf byteBuf)
    {
        byteBuf.writeBoolean(pck.helmetEffect);
        byteBuf.writeBoolean(pck.chestPlateEffect);
        byteBuf.writeBoolean(pck.leggingsEffect);
        byteBuf.writeBoolean(pck.bootsEffect);
        byteBuf.writeBoolean(pck.fullSet1Effect);
        byteBuf.writeBoolean(pck.fullSet2Effect);
    }

    public static @NotNull VArmorConfigurationPacket decode(@NotNull FriendlyByteBuf byteBuf)
    {
        return new VArmorConfigurationPacket(byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean());
    }

    public static void handle(VArmorConfigurationPacket pck, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        if(ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
            ctx.get().enqueueWork(() -> handleClientUpdate(pck));
        else ctx.get().enqueueWork(() -> handleServerUpdate(pck, Objects.requireNonNull(ctx.get().getSender())));
        ctx.get().setPacketHandled(true);
    }

    private static void update(VArmorConfigurationPacket pck, @NotNull Player player)
    {
        player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> armorConfiguration.defineConfig(new boolean[]{pck.helmetEffect, pck.chestPlateEffect, pck.leggingsEffect, pck.bootsEffect, pck.fullSet1Effect, pck.fullSet2Effect}));
    }

    private static void handleServerUpdate(VArmorConfigurationPacket pck, ServerPlayer serverPlayer)
    {
        update(pck, serverPlayer);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientUpdate(VArmorConfigurationPacket pck)
    {
        final var player = Minecraft.getInstance().player;
        assert player != null;
        update(pck, player);
    }

    public static class VRequestArmorConfiguration
    {
        public static void encode(@NotNull VRequestArmorConfiguration pck, @NotNull FriendlyByteBuf byteBuf) {}

        public static @NotNull VRequestArmorConfiguration decode(@NotNull FriendlyByteBuf byteBuf)
        {
            return new VRequestArmorConfiguration();
        }

        public static void handle(VRequestArmorConfiguration pck, @NotNull Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                final var player = ctx.get().getSender();
                AtomicReference<ArmorConfiguration> config = new AtomicReference<>(null);
                player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(config::set);
                if(config.get() != null)
                    VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new VArmorConfigurationPacket(config.get()));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
