package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public record VToolTip(Screen parent, String toolTip) implements Button.OnTooltip
{
    @Override
    public void onTooltip(@NotNull Button pButton, @NotNull PoseStack pPoseStack, int pMouseX, int pMouseY)
    {
        this.parent.renderTooltip(pPoseStack, new TextComponent(this.toolTip), pMouseX, pMouseY);
    }
}