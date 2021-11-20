package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.AtlasData;

public class PlayerAtlasHolder implements PlayerAtlas
{
    private AtlasData atlasData;

    @Override
    public AtlasData atlasData()
    {
        return this.atlasData;
    }

    @Override
    public void setAtlasData(AtlasData data)
    {
        this.atlasData = data;
    }

    @Override
    public String toString()
    {
        return "PlayerAtlasHolder{" + "atlasData=" + this.atlasData + '}';
    }
}
