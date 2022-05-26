package fr.flowarg.vip3.network;

import fr.flowarg.vip3.VIP3;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class VNetwork
{
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(VIP3.MOD_ID, "vip3_exchange");
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.ChannelBuilder
            .named(CHANNEL_NAME)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void registerPackets()
    {
        var index = -1;
        SYNC_CHANNEL.registerMessage(++index, VStartStopCrusherPacket.class, VStartStopCrusherPacket::encode, VStartStopCrusherPacket::decode, VStartStopCrusherPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        SYNC_CHANNEL.registerMessage(++index, VSwapSlotCrusherPacket.class, VSwapSlotCrusherPacket::encode, VSwapSlotCrusherPacket::decode, VSwapSlotCrusherPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        SYNC_CHANNEL.registerMessage(++index, VResetCrusherDataPacket.class, VResetCrusherDataPacket::encode, VResetCrusherDataPacket::decode, VResetCrusherDataPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        SYNC_CHANNEL.registerMessage(++index, VArmorConfigurationPacket.class, VArmorConfigurationPacket::encode, VArmorConfigurationPacket::decode, VArmorConfigurationPacket::handle);
        SYNC_CHANNEL.registerMessage(++index, VArmorConfigurationPacket.VRequestArmorConfiguration.class, (unused, _0) -> {}, unused -> VArmorConfigurationPacket.REQUEST_PACKET_INSTANCE, VArmorConfigurationPacket.VRequestArmorConfiguration::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        //SYNC_CHANNEL.registerMessage(++index, OLDPlayerAtlasPacket.class, OLDPlayerAtlasPacket::encode, OLDPlayerAtlasPacket::decode, OLDPlayerAtlasPacket::handle);
        //SYNC_CHANNEL.registerMessage(++index, OLDPlayerAtlasPacket.RequestPlayerAtlas.class, OLDPlayerAtlasPacket.RequestPlayerAtlas::encode, OLDPlayerAtlasPacket.RequestPlayerAtlas::decode, OLDPlayerAtlasPacket.RequestPlayerAtlas::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
