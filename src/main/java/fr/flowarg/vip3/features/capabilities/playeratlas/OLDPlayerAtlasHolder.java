package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.OLDAtlasData;

@Deprecated
public class OLDPlayerAtlasHolder implements OLDPlayerAtlas
{
    private OLDAtlasData atlasData;
    private boolean enabled;

    @Override
    public OLDAtlasData atlasData()
    {
        return this.atlasData;
    }

    @Override
    public boolean enabled()
    {
        return this.enabled;
    }

    @Override
    public void setAtlasData(OLDAtlasData data)
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
