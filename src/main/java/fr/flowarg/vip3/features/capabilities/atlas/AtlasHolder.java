package fr.flowarg.vip3.features.capabilities.atlas;

import java.util.List;

public class AtlasHolder implements Atlas
{
    private List<String> connectedAltars;

    @Override
    public List<String> connectedAltars()
    {
        return this.connectedAltars;
    }

    @Override
    public void connectedAltars(List<String> connectedAltars)
    {
        this.connectedAltars = connectedAltars;
    }

    @Override
    public String toString()
    {
        return "AtlasHolder{" + "connectedAltars=" + this.connectedAltars + '}';
    }
}
