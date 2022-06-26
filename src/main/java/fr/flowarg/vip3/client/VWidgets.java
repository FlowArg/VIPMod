package fr.flowarg.vip3.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public enum VWidgets
{
    CRUSHER_PLUS(new VMutableWidgetPos(0, 0, 18, 18, VWidgetOrientation.VERTICAL)),
    CRUSHER_MINUS(new VMutableWidgetPos(18, 0, 18, 18, VWidgetOrientation.VERTICAL)),
    CRUSHER_START(new VMutableWidgetPos(36, 0, 18, 18, VWidgetOrientation.VERTICAL)),
    CRUSHER_STOP(new VMutableWidgetPos(54, 0, 18, 18, VWidgetOrientation.VERTICAL)),
    STATS_RESET(new VMutableWidgetPos(72, 0, 18, 18, VWidgetOrientation.VERTICAL)),
    NO_19_19(new VMutableWidgetPos(0, 36, 19, 19, VWidgetOrientation.HORIZONTAL)),
    YES_19_19(new VMutableWidgetPos(0, 55, 19, 19, VWidgetOrientation.HORIZONTAL)),
    NO_19_18(new VMutableWidgetPos(0, 74, 19, 18, VWidgetOrientation.HORIZONTAL)),
    YES_19_18(new VMutableWidgetPos(0, 92, 19, 18, VWidgetOrientation.HORIZONTAL)),
    ALTAR_TRASH(new VMutableWidgetPos(0, 110, 18, 18, VWidgetOrientation.HORIZONTAL)),
    ALTAR_MENU(new VMutableWidgetPos(0, 128, 18, 18, VWidgetOrientation.HORIZONTAL)),
    ;

    private final VMutableWidgetPos pos;

    VWidgets(VMutableWidgetPos pos)
    {
        this.pos = pos;
    }

    public VMutableWidgetPos pos()
    {
        return this.pos;
    }
}
