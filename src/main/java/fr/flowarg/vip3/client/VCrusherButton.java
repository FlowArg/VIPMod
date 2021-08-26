package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VCrusherButton extends Button
{
    private static final ResourceLocation WIDGETS = new ResourceLocation(VIP3.MOD_ID, "textures/gui/widgets.png");

    private final ButtonType buttonType;

    public VCrusherButton(ButtonType type, int pX, int pY, OnPress pOnPress)
    {
        super(pX, pY, 18, 18, TextComponent.EMPTY, pOnPress);
        this.buttonType = type;
    }

    @Override
    public void renderButton(@NotNull PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(poseStack, this.x, this.y, this.buttonType.type, this.isHovered() ? 18 : 0, this.width, this.height);
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
