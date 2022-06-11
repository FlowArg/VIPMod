package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public interface VBackgroundScreen
{
    default Screen same()
    {
        if(this instanceof Screen)
            return (Screen) this;
        else throw new Error("This screen is not a Screen");
    }

    default void renderBackground(@NotNull PoseStack pPoseStack, @NotNull ResourceLocation texture, int imageWidth, int imageHeight)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        final var leftPos = (this.same().width - imageWidth) / 2;
        final var topPos = (this.same().height - imageHeight) / 2;

        this.same().blit(pPoseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
