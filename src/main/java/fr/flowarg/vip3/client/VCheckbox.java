package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VCheckbox extends Checkbox implements VWidget
{
    private final Font font = Minecraft.getInstance().font;
    private OnPress onPress;

    public VCheckbox(int pX, int pY, int width, int height, boolean pSelected)
    {
        super(pX, pY, width, height, TextComponent.EMPTY, pSelected, false);
    }

    public VCheckbox(int pX, int pY, int width, int height, Component component, boolean pSelected)
    {
        super(pX, pY, width, height, component, pSelected, true);
    }

    public VCheckbox(int pX, int pY, int width, int height, boolean pSelected, OnPress onPress)
    {
        super(pX, pY, width, height, TextComponent.EMPTY, pSelected, false);
        this.onPress = onPress;
    }

    public VCheckbox(int pX, int pY, int width, int height, Component component, boolean pSelected, OnPress onPress)
    {
        super(pX, pY, width, height, component, pSelected, true);
        this.onPress = onPress;
    }

    @Override
    public VWidgets widget()
    {
        return this.selected() ? VWidgets.YES_19_19 : VWidgets.NO_19_19;
    }

    @Override
    public void renderButton(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        VWidget.super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
        drawString(pPoseStack, this.font, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public void onPress()
    {
        super.onPress();
        if (this.onPress != null)
            this.onPress.onPress(this);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress
    {
        void onPress(VCheckbox checkbox);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if (!this.active || !this.visible)
            return false;

        if (!this.isValidClickButton(pButton))
            return false;

        if (!this.clicked(pMouseX, pMouseY))
            return false;

        this.playDownSound(Minecraft.getInstance().getSoundManager());
        this.onClick(pMouseX, pMouseY);
        return true;
    }
}
