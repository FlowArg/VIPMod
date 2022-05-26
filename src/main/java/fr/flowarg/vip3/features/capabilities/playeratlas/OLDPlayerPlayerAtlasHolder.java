package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.OLDAtlasData;
import fr.flowarg.vip3.network.OLDPlayerAtlasPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

@Deprecated
public class OLDPlayerPlayerAtlasHolder extends OLDPlayerAtlasHolder
{
    private final ServerPlayer serverPlayer;

    public OLDPlayerPlayerAtlasHolder(ServerPlayer serverPlayer)
    {
        this.serverPlayer = serverPlayer;
    }

    @Override
    public void setAtlasData(OLDAtlasData data)
    {
        super.setAtlasData(data);
        if(this.serverPlayer.connection != null)
        {
            this.serverPlayer.getCapability(OLDPlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY)
                    .ifPresent(capa -> VNetwork.SYNC_CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> this.serverPlayer),
                            new OLDPlayerAtlasPacket(capa))
                    );
        }
    }
}
