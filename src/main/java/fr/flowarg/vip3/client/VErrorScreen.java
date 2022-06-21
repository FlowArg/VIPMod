package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VErrorScreen extends Screen
{
    public VErrorScreen()
    {
        super(new TranslatableComponent("vip3.ass.error_title"));
    }

    @Override
    protected void init()
    {
        final var y = this.height / 4 + 172;
        final var xCenter = this.width / 2;
        final var largeButton = 150;
        final var smallButton = largeButton / 2;
        final var spacing = smallButton / 4;

        this.addRenderableWidget(new Button(xCenter - spacing / 2 - largeButton, y, largeButton, 20, new TranslatableComponent("gui.toTitle"), (button) -> this.minecraft.setScreen(new TitleScreen())));
        this.addRenderableWidget(new Button(xCenter + spacing / 2, y, largeButton, 20, new TranslatableComponent("menu.quit"), (p_96300_) -> this.minecraft.stop()));
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderBackground(pPoseStack);

        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
        drawString(pPoseStack, this.font, new TranslatableComponent("vip3.ass.error_message.1"), this.width / 2 - 140, this.height / 4 - 60 + 60, 10526880);
        drawString(pPoseStack, this.font, new TranslatableComponent("vip3.ass.error_message.2"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 9, 10526880);
        drawString(pPoseStack, this.font, new TranslatableComponent("vip3.ass.error_message.3"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
        drawString(pPoseStack, this.font, new TranslatableComponent("vip3.ass.error_message.4"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
        drawString(pPoseStack, this.font, new TranslatableComponent("vip3.ass.error_message.5"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
        drawString(pPoseStack, this.font, new TranslatableComponent("vip3.ass.error_message.6"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 45, 10526880);

        int i = 10;
        for (String s : ClientEventHandler.SOUND_FILE_ERROR)
        {
            drawCenteredString(pPoseStack, this.font, s, this.width / 2, this.height / 4 - 60 + 60 + 63 + i, 0xFFFFFF);
            i += 10;
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
