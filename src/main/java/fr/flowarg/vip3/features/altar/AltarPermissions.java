package fr.flowarg.vip3.features.altar;

public class AltarPermissions
{
    private boolean canTeleport;
    private boolean canManagePermissions;
    private boolean canAddAtlases;

    public AltarPermissions(boolean canTeleport, boolean canManagePermissions, boolean canAddAtlases)
    {
        this.canTeleport = canTeleport;
        this.canManagePermissions = canManagePermissions;
        this.canAddAtlases = canAddAtlases;
    }

    public boolean canTeleport()
    {
        return this.canTeleport;
    }

    public void setCanTeleport(boolean canTeleport)
    {
        this.canTeleport = canTeleport;
    }

    public boolean canManagePermissions()
    {
        return this.canManagePermissions;
    }

    public void setCanManagePermissions(boolean canManagePermissions)
    {
        this.canManagePermissions = canManagePermissions;
    }

    public boolean canAddAtlases()
    {
        return this.canAddAtlases;
    }

    public void setCanAddAtlases(boolean canAddAtlases)
    {
        this.canAddAtlases = canAddAtlases;
    }
}
