package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class VCrusherButton extends Button implements VWidget
{
    private final ButtonType buttonType;

    record CustomTooltip(Screen parent, String toolTip) implements OnTooltip
    {
        @Override
        public void onTooltip(@NotNull Button pButton, @NotNull PoseStack pPoseStack, int pMouseX, int pMouseY)
        {
            this.parent.renderTooltip(pPoseStack, new TextComponent(this.toolTip), pMouseX, pMouseY);
        }
    }

    public VCrusherButton(ButtonType type, int pX, int pY, OnPress pOnPress, CustomTooltip toolTip)
    {
        super(pX, pY, 18, 18, TextComponent.EMPTY, pOnPress, toolTip);
        this.buttonType = type;
    }

    @Override
    public void renderButton(@NotNull PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(poseStack, this.x, this.y, this.buttonType.type, this.isHoveredOrFocused() ? 18 : 0, this.width, this.height);

        if(this.isHoveredOrFocused())
            this.renderToolTip(poseStack, pMouseX, pMouseY);
    }

    public enum ButtonType {
        PLUS((byte)0),
        MINUS((byte)18),
        START((byte)36),
        STOP((byte)54),
        RESET((byte)72);

        private final byte type;

        ButtonType(byte type)
        {
            this.type = type;
        }
    }
}
