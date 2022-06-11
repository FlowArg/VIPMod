package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public interface VWidget extends VWidgetAccessor
{
    VWidgets widget();
    default AbstractWidget same()
    {
        if(this instanceof AbstractWidget)
            return (AbstractWidget)this;
        else
            throw new Error("VWidget is not an AbstractWidget");
    }

    default VWidgetStatus status()
    {
        ((AbstractWidget)this).isHoveredOrFocused();
        return this.same().isHoveredOrFocused() ? VWidgetStatus.HOVERED : VWidgetStatus.NORMAL;
    }

    default void renderButton(@NotNull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        var widgetPos = this.widget().pos();
        switch (this.status())
        {
            case DISABLED -> widgetPos = widgetPos.disabled();
            case HOVERED -> widgetPos = widgetPos.hovered();
        }

        this.same().blit(pMatrixStack, this.same().x, this.same().y, widgetPos.u, widgetPos.v, widgetPos.getWidth(), widgetPos.getHeight());
    }
}
