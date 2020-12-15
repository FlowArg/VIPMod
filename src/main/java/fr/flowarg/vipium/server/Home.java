package fr.flowarg.vipium.server;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class Home
{
    private final String name;
    private final int dimension;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public Home(String name, int dimension, double x, double y, double z, float yaw, float pitch)
    {
        this.name = name;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getName()
    {
        return this.name;
    }

    public int getDimension()
    {
        return this.dimension;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }
}
