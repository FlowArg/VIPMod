package fr.flowarg.vip3.network;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.client.AltarScreen;
import fr.flowarg.vip3.features.altar.Altar;
import fr.flowarg.vip3.features.altar.AltarData;
import fr.flowarg.vip3.features.altar.AltarSerialization;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class VSendAltarPacket
{
    private final Altar altar;
    private final boolean openGUI;

    public VSendAltarPacket(Altar altar, boolean openGUI)
    {
        this.altar = altar;
        this.openGUI = openGUI;
    }

    public static void encode(@NotNull VSendAltarPacket packet, @NotNull FriendlyByteBuf byteBuf)
    {
        byteBuf.writeNbt(AltarSerialization.serializeAltar(packet.altar));
        byteBuf.writeBoolean(packet.openGUI);
    }

    public static @Nullable VSendAltarPacket decode(@NotNull FriendlyByteBuf byteBuf)
    {
        final var nbt = byteBuf.readNbt();

        if(nbt == null)
        {
            VIP3.LOGGER.error("nbt null.");
            return null;
        }

        return new VSendAltarPacket(AltarSerialization.deserializeAltar(nbt), byteBuf.readBoolean());
    }

    public static void handle(VSendAltarPacket packet, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER)
            {
                final var altarData = AltarData.getOrCreate(ctx.get().getSender().getLevel());
                for (Altar other : altarData.altars())
                {
                    if(other.getId().equals(packet.altar.getId()))
                    {
                        other.copyPaste(packet.altar);
                        altarData.setDirty();
                        break;
                    }
                }
            }
            else updateClient(packet);
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void updateClient(@NotNull VSendAltarPacket packet)
    {
        if(packet.openGUI)
            Minecraft.getInstance().setScreen(new AltarScreen(packet.altar));
    }
}
