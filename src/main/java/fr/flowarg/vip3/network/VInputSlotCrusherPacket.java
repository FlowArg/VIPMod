package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.VCrusherEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record VInputSlotCrusherPacket(BlockPos blockPos, ItemStack toUnlock)
{
    public static void encode(VInputSlotCrusherPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeItem(packet.toUnlock);
    }

    public static VInputSlotCrusherPacket decode(FriendlyByteBuf buffer)
    {
        return new VInputSlotCrusherPacket(buffer.readBlockPos(), buffer.readItem());
    }

    public static void handle(VInputSlotCrusherPacket packet, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            if (Objects.requireNonNull(ctx.get().getSender()).level.getBlockEntity(packet.blockPos) instanceof VCrusherEntity crusherEntity)
            {
                if(crusherEntity.getItem(VCrusherEntity.SLOT_INPUT).isEmpty()) crusherEntity.setItem(VCrusherEntity.SLOT_INPUT, packet.toUnlock);
                else crusherEntity.getItem(VCrusherEntity.SLOT_INPUT).grow(packet.toUnlock.getCount());
                crusherEntity.getItem(VCrusherEntity.SLOT_LOCKED).shrink(1);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "VInputSlotCrusherPacket{" + "blockPos=" + this.blockPos + ", toUnlock=" + this.toUnlock + '}';
    }
}
