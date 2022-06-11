package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VConfirmScreen extends Screen implements VWidgetAccessor, VBackgroundScreen
{
    private static final ResourceLocation CONFIRM_TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/confirm.png");

    private final BooleanConsumer callback;
    private final int imageWidth = 170, imageHeight = 80;

    public VConfirmScreen(BooleanConsumer callback)
    {
        super(TextComponent.EMPTY);
        this.callback = callback;
    }

    @Override
    protected void init()
    {
        final int leftPos = (this.width - this.imageWidth) / 2;
        final int topPos = (this.height - this.imageHeight) / 2;
        final int y = topPos + 30;

        this.addRenderableWidget(new VButton(leftPos + 27, y, VWidgets.YES_19_19, pButton -> this.callback.accept(true)));
        this.addRenderableWidget(new VButton(leftPos + 120, y, VWidgets.NO_19_19, pButton -> this.callback.accept(false)));
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pPoseStack)
    {
        super.renderBackground(pPoseStack);
        this.renderBackground(pPoseStack, CONFIRM_TEXTURE, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers)
    {
        if(pKeyCode == 256)
        {
            this.callback.accept(false);
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
