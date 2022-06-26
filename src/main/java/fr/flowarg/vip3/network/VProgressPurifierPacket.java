package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.purifier.VPurifierEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record VProgressPurifierPacket(BlockPos blockPos)
{
    public static void encode(@NotNull VProgressPurifierPacket packet, @NotNull FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
    }

    @Contract("_ -> new")
    public static @NotNull VProgressPurifierPacket decode(@NotNull FriendlyByteBuf buffer)
    {
        return new VProgressPurifierPacket(buffer.readBlockPos());
    }

    public static void handle(VProgressPurifierPacket packet, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final var player = ctx.get().getSender();

            if (player != null && player.level.getBlockEntity(packet.blockPos) instanceof VPurifierEntity purifierEntity)
            {
                if(!purifierEntity.stillValid(player)) return;

                purifierEntity.increaseProgress();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString()
    {
        return "VStartStopCrusherPacket{" + "blockPos=" + this.blockPos + '}';
    }
}

