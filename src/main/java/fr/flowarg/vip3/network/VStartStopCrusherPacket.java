package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.crusher.VCrusherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
            final var player = ctx.get().getSender();

            if (player != null && player.level.getBlockEntity(packet.blockPos) instanceof VCrusherEntity crusherEntity)
            {
                if(!crusherEntity.stillValid(player)) return;

                crusherEntity.setStarted(packet.start);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "VStartStopCrusherPacket{" + "blockPos=" + this.blockPos + ", start=" + this.start + '}';
    }
}
