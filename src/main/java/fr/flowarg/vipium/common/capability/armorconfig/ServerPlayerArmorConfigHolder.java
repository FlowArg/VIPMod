package fr.flowarg.vipium.common.capability.armorconfig;

import fr.flowarg.vipium.common.network.SyncConfigPacket;
import fr.flowarg.vipium.common.network.VIPNetwork;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class ServerPlayerArmorConfigHolder extends DefaultArmorConfigHolder
{
    private final ServerPlayerEntity player;

    public ServerPlayerArmorConfigHolder(ServerPlayerEntity player)
    {
        this.player = player;
    }

    @Override
    public void setArmorConfig(int[] armorConfig)
    {
        super.setArmorConfig(armorConfig);
        if(this.player.connection != null)
            this.player.getCapability(ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY).ifPresent(armorConfigUsable -> VIPNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> this.player), new SyncConfigPacket(armorConfigUsable)));
    }
}
