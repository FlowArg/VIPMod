package fr.flowarg.vip3.features.capabilities.armorconfiguration;

import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class PlayerArmorConfigurationHolder extends ArmorConfigurationHolder
{
    private final ServerPlayer serverPlayer;

    public PlayerArmorConfigurationHolder(ServerPlayer serverPlayer)
    {
        this.serverPlayer = serverPlayer;
    }

    @Override
    public void defineConfig(boolean @NotNull [] config)
    {
        super.defineConfig(config);
        if(this.serverPlayer.connection != null)
        {
            this.serverPlayer.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY)
                    .ifPresent(capa -> VNetwork.SYNC_CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> this.serverPlayer),
                            new VArmorConfigurationPacket(capa))
                    );
        }
    }
}
