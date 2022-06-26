package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public final class VToolTip implements Button.OnTooltip
{
    private final Screen parent;
    private final Component toolTip;

    public VToolTip(Screen parent, Component toolTip)
    {
        this.parent = parent;
        this.toolTip = toolTip;
    }

    public VToolTip(Screen parent, String toolTip)
    {
        this.parent = parent;
        this.toolTip = new TextComponent(toolTip);
    }

    @Override
    public void onTooltip(@NotNull Button pButton, @NotNull PoseStack pPoseStack, int pMouseX, int pMouseY)
    {
        this.parent.renderTooltip(pPoseStack, this.toolTip, pMouseX, pMouseY);
    }

    public Screen parent() {return parent;}

    public Component toolTip() {return toolTip;}
}