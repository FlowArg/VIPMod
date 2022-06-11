package fr.flowarg.vip3.features.capabilities.atlas;

import fr.flowarg.vip3.network.AtlasPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class PlayerAtlasHolder extends AtlasHolder
{
    private final ServerPlayer serverPlayer;

    public PlayerAtlasHolder(ServerPlayer serverPlayer)
    {
        this.serverPlayer = serverPlayer;
    }

    @Override
    public void connectedAltars(List<String> connectedAltars)
    {
        super.connectedAltars(connectedAltars);
        if(this.serverPlayer.connection != null)
        {
            this.serverPlayer.getCapability(AtlasCapability.ATLAS_CAPABILITY)
                    .ifPresent(capa -> VNetwork.SYNC_CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> this.serverPlayer),
                            new AtlasPacket(capa))
                    );
        }
    }
}
