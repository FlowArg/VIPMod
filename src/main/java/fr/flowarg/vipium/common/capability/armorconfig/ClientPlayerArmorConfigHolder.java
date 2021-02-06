package fr.flowarg.vipium.common.capability.armorconfig;

import fr.flowarg.vipium.common.network.SyncConfigPacket;
import fr.flowarg.vipium.common.network.VIPNetwork;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class ClientPlayerArmorConfigHolder extends DefaultArmorConfigHolder
{
    private final ClientPlayerEntity player;

    public ClientPlayerArmorConfigHolder(ClientPlayerEntity player)
    {
        this.player = player;
    }

    @Override
    public void setArmorConfig(int[] armorConfig)
    {
        super.setArmorConfig(armorConfig);
        if(this.player.connection != null)
            this.player.getCapability(ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY).ifPresent(armorConfigUsable -> VIPNetwork.CHANNEL.send(PacketDistributor.SERVER.noArg(), new SyncConfigPacket(armorConfigUsable)));
    }
}
