package fr.flowarg.vip3.features.altar;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Altar
{
    private final String id;
    private final String owner;
    private final AltarPos pos;
    private String name;
    private Map<String, ConnectedAtlas> connectedAtlases;
    private boolean canLink;

    public Altar(String id, String owner, AltarPos pos, String name, Map<String, ConnectedAtlas> connectedAtlases, boolean canLink)
    {
        this.id = id;
        this.owner = owner;
        this.pos = pos;
        this.name = name;
        this.connectedAtlases = connectedAtlases;
        this.canLink = canLink;
    }

    public void copyPaste(@NotNull Altar altar)
    {
        this.name = altar.name;
        this.connectedAtlases = altar.connectedAtlases;
        this.canLink = altar.canLink;
    }

    public String getId()
    {
        return this.id;
    }

    public String getOwner()
    {
        return this.owner;
    }

    public AltarPos getPos()
    {
        return this.pos;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Map<String, ConnectedAtlas> getConnectedAtlases()
    {
        return this.connectedAtlases;
    }

    public void setConnectedAtlases(Map<String, ConnectedAtlas> connectedAtlases)
    {
        this.connectedAtlases = connectedAtlases;
    }

    public boolean canLink()
    {
        return this.canLink;
    }

    public void setCanLink(boolean canLink)
    {
        this.canLink = canLink;
    }
}
