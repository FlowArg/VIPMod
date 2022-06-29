package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VAltarCheckboxButton extends Checkbox implements VWidget
{
    private final boolean available;

    public VAltarCheckboxButton(int pX, int pY, boolean pSelected, boolean available)
    {
        super(pX, pY, 19, 18, TextComponent.EMPTY, pSelected, false);
        this.available = available;
    }

    @Override
    public void onPress()
    {
        if(!this.available)
            super.onPress();
    }

    @Override
    public void renderButton(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        VWidget.super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public VWidgetStatus status()
    {
        return this.available ? VWidgetStatus.DISABLED : VWidget.super.status();
    }

    @Override
    public VWidgets widget()
    {
        return this.selected() ? VWidgets.YES_19_18 : VWidgets.NO_19_18;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if (!this.active || !this.visible)
            return false;

        if (this.isValidClickButton(pButton))
        {
            boolean flag = this.clicked(pMouseX, pMouseY) && this.status() != VWidgetStatus.DISABLED;
            if (flag)
            {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.onClick(pMouseX, pMouseY);
                return true;
            }
        }
        return false;
    }
}
