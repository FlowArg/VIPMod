package fr.flowarg.vip3.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class VChatClearPacket
{
    public static final VChatClearPacket INSTANCE = new VChatClearPacket();

    public static void handle(VChatClearPacket ignored, @NotNull Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
                Minecraft.getInstance().gui.getChat().clearMessages(true);
        });
        ctx.get().setPacketHandled(true);
    }
}
