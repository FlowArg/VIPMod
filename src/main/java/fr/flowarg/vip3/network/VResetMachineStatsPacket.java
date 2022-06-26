package fr.flowarg.vip3.network;

import fr.flowarg.vip3.features.crusher.VCrusherEntity;
import fr.flowarg.vip3.features.purifier.VPurifierEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record VResetMachineStatsPacket(BlockPos blockPos, byte type)
{
    public static final byte TYPE_CRUSHER = 0;
    public static final byte TYPE_PURIFIER = 1;

    public static void encode(@NotNull VResetMachineStatsPacket packet, @NotNull FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(packet.blockPos);
        buffer.writeByte(packet.type);
    }

    @Contract("_ -> new")
    public static @NotNull VResetMachineStatsPacket decode(@NotNull FriendlyByteBuf buffer)
    {
        return new VResetMachineStatsPacket(buffer.readBlockPos(), buffer.readByte());
    }

    public static void handle(VResetMachineStatsPacket packet, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            final var player = ctx.get().getSender();

            if (player == null)
                return;

            final var blockEntity = player.level.getBlockEntity(packet.blockPos);

            if(packet.type == TYPE_CRUSHER && blockEntity instanceof VCrusherEntity crusherEntity)
            {
                if(!crusherEntity.stillValid(player)) return;

                crusherEntity.setFragmentsResult(0);
                crusherEntity.setCrushedIngots(0);
            }

            if(packet.type == TYPE_PURIFIER && blockEntity instanceof VPurifierEntity purifierEntity)
            {
                if(!purifierEntity.stillValid(player)) return;

                purifierEntity.setVipiumResult(0);
                purifierEntity.setPurifiedVipium(0);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString()
    {
        return "VResetMachineDataPacket{" + "blockPos=" + this.blockPos + ", type=" + this.type + '}';
    }
}
