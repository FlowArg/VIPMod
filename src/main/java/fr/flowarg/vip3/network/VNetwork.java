package fr.flowarg.vip3.network;

import fr.flowarg.vip3.VIP3;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class VNetwork
{
    private static final ResourceLocation CHANNEL_NAME = new ResourceLocation(VIP3.MOD_ID, "vipium_crusher_exchange");
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel SYNC_CHANNEL = NetworkRegistry.ChannelBuilder
            .named(CHANNEL_NAME)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void registerPackets()
    {
        SYNC_CHANNEL.registerMessage(0, VStartStopCrusherPacket.class, VStartStopCrusherPacket::encode, VStartStopCrusherPacket::decode, VStartStopCrusherPacket::handle);
        SYNC_CHANNEL.registerMessage(1, VLockSlotCrusherPacket.class, VLockSlotCrusherPacket::encode, VLockSlotCrusherPacket::decode, VLockSlotCrusherPacket::handle);
        SYNC_CHANNEL.registerMessage(2, VInputSlotCrusherPacket.class, VInputSlotCrusherPacket::encode, VInputSlotCrusherPacket::decode, VInputSlotCrusherPacket::handle);
    }
}
