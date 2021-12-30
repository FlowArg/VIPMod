package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.AtlasData;

public class PlayerAtlasHolder implements PlayerAtlas
{
    private AtlasData atlasData;
    private boolean enabled;

    @Override
    public AtlasData atlasData()
    {
        return this.atlasData;
    }

    @Override
    public boolean enabled()
    {
        return this.enabled;
    }

    @Override
    public void setAtlasData(AtlasData data)
    {
        this.atlasData = data;
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public String toString()
    {
        return "PlayerAtlasHolder{" + "atlasData=" + this.atlasData + '}';
    }
}
