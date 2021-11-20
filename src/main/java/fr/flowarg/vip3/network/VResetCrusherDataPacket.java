package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.crusher.VCrusherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record VResetCrusherDataPacket(BlockPos blockPos)
{
    public static void encode(@NotNull VResetCrusherDataPacket packet, @NotNull FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
    }

    @Contract("_ -> new")
    public static @NotNull VResetCrusherDataPacket decode(@NotNull FriendlyByteBuf buffer)
    {
        return new VResetCrusherDataPacket(buffer.readBlockPos());
    }

    public static void handle(VResetCrusherDataPacket packet, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final var player = ctx.get().getSender();

            if (player != null && player.level.getBlockEntity(packet.blockPos) instanceof VCrusherEntity crusherEntity)
            {
                if(!crusherEntity.stillValid(player)) return;

                crusherEntity.setFragmentsResult(0);
                crusherEntity.setCrushedIngots(0);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "VResetCrusherDataPacket{" + "blockPos=" + this.blockPos + '}';
    }
}
