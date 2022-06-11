package fr.flowarg.vip3.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VWidgetPos
{
    protected int u;
    protected int v;
    protected final int width, height;

    public VWidgetPos(int u, int v, int width, int height)
    {
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
    }

    public int getU()
    {
        return this.u;
    }

    public int getV()
    {
        return this.v;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }
}
