package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VButton extends Button implements VWidget
{
    private final VWidgets buttonType;

    public VButton(int pX, int pY, VWidgets type, OnPress pOnPress, VToolTip toolTip)
    {
        super(pX, pY, type.pos().width, type.pos().height, TextComponent.EMPTY, pOnPress, toolTip);
        this.buttonType = type;
    }

    public VButton(int pX, int pY, VWidgets type, OnPress pOnPress)
    {
        super(pX, pY, type.pos().width, type.pos().height, TextComponent.EMPTY, pOnPress);
        this.buttonType = type;
    }

    public VButton(int pX, int pY, VWidgets type, Component component, OnPress pOnPress, VToolTip toolTip)
    {
        super(pX, pY, type.pos().width, type.pos().height, component, pOnPress, toolTip);
        this.buttonType = type;
    }

    public VButton(int pX, int pY, VWidgets type, Component component, OnPress pOnPress)
    {
        super(pX, pY, type.pos().width, type.pos().height, component, pOnPress);
        this.buttonType = type;
    }

    @Override
    public VWidgets widget()
    {
        return this.buttonType;
    }

    @Override
    public void renderButton(@NotNull PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        VWidget.super.renderButton(poseStack, pMouseX, pMouseY, pPartialTicks);

        if(this.isHoveredOrFocused())
            this.renderToolTip(poseStack, pMouseX, pMouseY);
    }
}
