package fr.flowarg.vip3.network.capabilities;

import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class ServerArmorConfigurationWrapper extends ArmorConfigurationWrapper
{
    private final ServerPlayer player;

    public ServerArmorConfigurationWrapper(ServerPlayer player)
    {
        this.player = player;
    }

    @Override
    public void notifyChange()
    {
        this.player.getCapability(CapabilitiesEventHandler.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> this.player), new VArmorConfigurationPacket(armorConfiguration)));
    }
}
