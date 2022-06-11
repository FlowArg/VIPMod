package fr.flowarg.vip3.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VMutableWidgetPos extends VWidgetPos
{
    private final VWidgetOrientation orientation;

    public VMutableWidgetPos(int u, int v, int width, int height, VWidgetOrientation orientation)
    {
        super(u, v, width, height);
        this.orientation = orientation;
    }

    public VMutableWidgetPos copy()
    {
        return new VMutableWidgetPos(this.u, this.v, this.width, this.height, this.orientation);
    }

    public VMutableWidgetPos disabled()
    {
        return this.var(2);
    }

    public VMutableWidgetPos hovered()
    {
        return this.var(1);
    }

    public VMutableWidgetPos var(int k)
    {
        final var copy = this.copy();

        switch (orientation)
        {
            case HORIZONTAL -> copy.u += copy.width * k;
            case VERTICAL -> copy.v += copy.height * k;
        }

        return copy;
    }
}
