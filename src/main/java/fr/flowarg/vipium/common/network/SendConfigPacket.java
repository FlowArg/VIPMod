package fr.flowarg.vipium.common.network;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class SendConfigPacket
{
    private final boolean[] config;

    public SendConfigPacket(boolean[] config)
    {
        this.config = config;
    }
    
    public static void encode(SendConfigPacket pck, PacketBuffer buf)
    {
        final int configSize = pck.config.length;
        buf.writeInt(configSize);
        for (boolean config : pck.config)
            buf.writeBoolean(config);
    }

    public static SendConfigPacket decode(PacketBuffer buf)
    {
        final int configSize = buf.readInt();
        final boolean[] config = new boolean[configSize];
        for (int i = 0; i < configSize; i++)
            config[i] = buf.readBoolean();
        return new SendConfigPacket(config);
    }

    public static void handle(SendConfigPacket pck, Supplier<NetworkEvent.Context> ctx)
    {
        if (VIPMod.side == Dist.DEDICATED_SERVER)
            ctx.get().enqueueWork(() -> RegistryHandler.config = pck.config);
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "SendConfigPacket{" + "config=" + Arrays.toString(config) + '}';
    }
}
