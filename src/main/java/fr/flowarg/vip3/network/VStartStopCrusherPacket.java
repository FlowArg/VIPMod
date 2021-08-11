package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.VCrusherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record VStartStopCrusherPacket(BlockPos blockPos, boolean start)
{
    public static void encode(VStartStopCrusherPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeBoolean(packet.start);
    }

    public static VStartStopCrusherPacket decode(FriendlyByteBuf buffer)
    {
        return new VStartStopCrusherPacket(buffer.readBlockPos(), buffer.readBoolean());
    }

    public static void handle(VStartStopCrusherPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            if (Objects.requireNonNull(ctx.get().getSender()).level.getBlockEntity(packet.blockPos) instanceof VCrusherEntity crusherEntity)
                crusherEntity.setStarted(packet.start);
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "VStartStopCrusherPacket{" + "blockPos=" + this.blockPos + ", start=" + this.start + '}';
    }
}
