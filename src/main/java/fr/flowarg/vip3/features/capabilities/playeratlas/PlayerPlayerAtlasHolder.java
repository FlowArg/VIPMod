package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.AtlasData;
import fr.flowarg.vip3.network.PlayerAtlasPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class PlayerPlayerAtlasHolder extends PlayerAtlasHolder
{
    private final ServerPlayer serverPlayer;

    public PlayerPlayerAtlasHolder(ServerPlayer serverPlayer)
    {
        this.serverPlayer = serverPlayer;
    }

    @Override
    public void setAtlasData(AtlasData data)
    {
        super.setAtlasData(data);
        if(this.serverPlayer.connection != null)
        {
            this.serverPlayer.getCapability(PlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY)
                    .ifPresent(capa -> VNetwork.SYNC_CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> this.serverPlayer),
                            new PlayerAtlasPacket(capa.atlasData()))
                    );
        }
    }
}
