package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.VCrusherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record VLockSlotCrusherPacket(BlockPos blockPos, ItemStack toLock)
{
    public static void encode(VLockSlotCrusherPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeItem(packet.toLock);
    }

    public static VLockSlotCrusherPacket decode(FriendlyByteBuf buffer)
    {
        return new VLockSlotCrusherPacket(buffer.readBlockPos(), buffer.readItem());
    }

    public static void handle(VLockSlotCrusherPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            if (Objects.requireNonNull(ctx.get().getSender()).level.getBlockEntity(packet.blockPos) instanceof VCrusherEntity crusherEntity)
            {
                if(crusherEntity.getItem(VCrusherEntity.SLOT_LOCKED).isEmpty())
                    crusherEntity.setItem(VCrusherEntity.SLOT_LOCKED, packet.toLock);
                else crusherEntity.getItem(VCrusherEntity.SLOT_LOCKED).grow(packet.toLock.getCount());
                crusherEntity.getItem(VCrusherEntity.SLOT_INPUT).shrink(1);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "VLockSlotCrusherPacket{" + "blockPos=" + this.blockPos + ", toLock=" + this.toLock + '}';
    }
}
