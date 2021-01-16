package fr.flowarg.vipium.common.network;

import fr.flowarg.vipium.VIPMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class VIPNetwork
{
    private static final String PROTOCOL_VERSION = String.valueOf(1);

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(VIPMod.MODID, "vip_channel"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void registerPackets()
    {
        CHANNEL.messageBuilder(SendConfigPacket.class, 0)
                .encoder(SendConfigPacket::encode)
                .decoder(SendConfigPacket::decode)
                .consumer(SendConfigPacket::handle)
                .add();
    }
}
