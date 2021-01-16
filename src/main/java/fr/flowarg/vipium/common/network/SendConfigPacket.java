package fr.flowarg.vipium.common.network;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class SendConfigPacket
{
    private final String playerName;
    private final boolean[] config;

    public SendConfigPacket(String playerName, boolean[] config)
    {
        this.playerName = playerName;
        this.config = config;
    }
    
    public static void encode(SendConfigPacket pck, PacketBuffer buf)
    {
        buf.writeString(pck.playerName);
        final int configSize = pck.config.length;
        buf.writeInt(configSize);
        for (boolean config : pck.config)
            buf.writeBoolean(config);
    }

    public static SendConfigPacket decode(PacketBuffer buf)
    {
        final String playerName = buf.readString();
        final int configSize = buf.readInt();
        final boolean[] config = new boolean[configSize];
        for (int i = 0; i < configSize; i++)
            config[i] = buf.readBoolean();
        return new SendConfigPacket(playerName, config);
    }

    public static void handle(SendConfigPacket pck, Supplier<NetworkEvent.Context> ctx)
    {
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> ctx.get().enqueueWork(() -> RegistryHandler.CONFIG_BY_PLAYER.put(pck.playerName, pck.config)));
        ctx.get().setPacketHandled(true);
    }

    @Override
    public String toString()
    {
        return "SendConfigPacket{" + "config=" + Arrays.toString(config) + '}';
    }
}
