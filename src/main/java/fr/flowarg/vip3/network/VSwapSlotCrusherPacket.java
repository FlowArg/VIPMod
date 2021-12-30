package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.crusher.VCrusherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record VSwapSlotCrusherPacket(BlockPos blockPos, int origin, int destination)
{
    public static void encode(VSwapSlotCrusherPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeInt(packet.origin);
        buffer.writeInt(packet.destination);
    }

    public static VSwapSlotCrusherPacket decode(FriendlyByteBuf buffer)
    {
        return new VSwapSlotCrusherPacket(buffer.readBlockPos(), buffer.readInt(), buffer.readInt());
    }

    public static void handle(VSwapSlotCrusherPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final var player = ctx.get().getSender();

            if (player != null && player.level.getBlockEntity(packet.blockPos) instanceof VCrusherEntity crusherEntity)
            {
                if(!crusherEntity.stillValid(player)) return;

                final var originStack = crusherEntity.getItem(packet.origin);
                final var destinationStack = crusherEntity.getItem(packet.destination);

                if(destinationStack.isEmpty())
                {
                    crusherEntity.setItem(packet.destination, new ItemStack(originStack.getItem(), 1));
                    originStack.shrink(1);
                }
                else if(destinationStack.sameItem(originStack))
                {
                    destinationStack.grow(1);
                    originStack.shrink(1);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "VSwapSlotCrusherPacket{" + "blockPos=" + this.blockPos + ", origin=" + this.origin + ", destination=" + this.destination + '}';
    }
}
