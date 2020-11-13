package fr.flowarg.vipium.server;

public class Home
{
    private final String name;
    private final int dimension;
    private final long x;
    private final long y;
    private final long z;
    private final float yaw;
    private final float pitch;

    public Home(String name, int dimension, long x, long y, long z, float yaw, float pitch)
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

    public long getX()
    {
        return this.x;
    }

    public long getY()
    {
        return this.y;
    }

    public long getZ()
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
