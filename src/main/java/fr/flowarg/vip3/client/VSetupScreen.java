package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VSetupScreen extends Screen
{
    private Button mainMenuButton;
    private Button previousButton;
    private Button nextButton;
    private int step = 0;
    private final int maxStep = 2;

    // step 1
    private int pauseScanCode = -2;
    private int stopScanCode = -2;
    private int skipScanCode = -2;
    private Button currentButton = null;
    private Button pauseResumeButton;
    private Button stopButton;
    private Button skipButton;

    private int xCenter = this.width / 2;
    private int largeButton = 150;
    private int smallButton = largeButton / 2;
    private int spacing = smallButton / 4;

    public VSetupScreen()
    {
        super(new TranslatableComponent("vip3.setup"));
        VIPConfig.CLIENT.getStopMediaKey().set(-1);
        VIPConfig.CLIENT.getPauseMediaKey().set(-1);
        VIPConfig.CLIENT.getSkipMediaKey().set(-1);
    }

    @Override
    protected void init()
    {
        final var y = this.height / 4 + 120;
        this.xCenter = this.width / 2;
        this.largeButton = 150;
        this.smallButton = this.largeButton / 2;
        this.spacing = this.smallButton / 4;

        this.previousButton = this.addRenderableWidget(new Button(this.xCenter - this.spacing / 2 - this.largeButton, y + 20 + 10, this.smallButton, 20, new TranslatableComponent("vip3.setup.previous"), pButton -> {
            this.step--;
            this.updateStep();
        }));
        this.mainMenuButton = this.addRenderableWidget(new Button(this.xCenter - this.spacing / 2 - this.largeButton, y, this.largeButton, 20, new TranslatableComponent("gui.toTitle"), (p_96304_) -> {
            VIPConfig.CLIENT.getFirstLaunch().set(false);
            VIPConfig.CLIENT.getPauseMediaKey().set(this.pauseScanCode);
            VIPConfig.CLIENT.getStopMediaKey().set(this.stopScanCode);
            VIPConfig.CLIENT.getSkipMediaKey().set(this.skipScanCode);
            this.minecraft.setScreen(new TitleScreen());
        }));
        this.addRenderableWidget(new Button(this.xCenter + this.spacing / 2, y, this.largeButton, 20, new TranslatableComponent("menu.quit"), (p_96300_) -> this.minecraft.stop()));
        this.nextButton = this.addRenderableWidget(new Button(this.xCenter + this.spacing / 2 + this.largeButton - this.smallButton, y + 20 + 10, this.smallButton, 20, new TranslatableComponent("vip3.setup.next"), pButton -> {
            this.step++;
            this.updateStep();
        }));
        this.addRenderableWidget(this.pauseResumeButton = new Button(this.xCenter - this.spacing / 2 - this.largeButton, y - 50, this.smallButton, 20, new TranslatableComponent("vip3.undefined"), pButton -> {
            this.currentButton = this.pauseResumeButton;
            this.pauseResumeButton.setMessage(new TranslatableComponent("vip3.undefined").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));
        }));
        this.addRenderableWidget(this.stopButton = new Button(this.xCenter - this.smallButton / 2, y - 50, this.smallButton, 20, new TranslatableComponent("vip3.undefined"), pButton -> {
            this.currentButton = this.stopButton;
            this.stopButton.setMessage(new TranslatableComponent("vip3.undefined").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));
        }));
        this.addRenderableWidget(this.skipButton = new Button(this.xCenter + this.spacing / 2 + this.largeButton - this.smallButton, y - 50, this.smallButton, 20, new TranslatableComponent("vip3.undefined"), pButton -> {
            this.currentButton = this.skipButton;
            this.skipButton.setMessage(new TranslatableComponent("vip3.undefined").withStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));
        }));
        this.addRenderableWidget(new ImageButton(this.mainMenuButton.x - 20 - this.spacing / 2, y, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, (pButton) -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), new TranslatableComponent("narrator.button.language")));
        this.updateStep();
    }

    private void updateStep()
    {
        if(this.step == 0)
        {
            this.previousButton.active = false;
            this.nextButton.active = true;
            this.mainMenuButton.active = false;
            this.pauseResumeButton.visible = false;
            this.stopButton.visible = false;
            this.skipButton.visible = false;
        }

        if(this.step == this.maxStep)
        {
            this.previousButton.active = true;
            this.nextButton.active = false;
            this.mainMenuButton.active = true;
            this.pauseResumeButton.visible = false;
            this.stopButton.visible = false;
            this.skipButton.visible = false;
        }

        if(this.step > 0 && this.step < this.maxStep)
        {
            this.previousButton.active = true;
            this.nextButton.active = true;
            this.mainMenuButton.active = false;
        }

        if(step == 1)
        {
            this.pauseResumeButton.visible = true;
            this.stopButton.visible = true;
            this.skipButton.visible = true;

            if(this.pauseScanCode == -2 || this.stopScanCode == -2 || this.skipScanCode == -2)
            {
                this.nextButton.active = false;
                this.previousButton.active = false;
            }
        }
    }

    @Override
    public boolean shouldCloseOnEsc()
    {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if(keyCode == 256)
        {
            if(this.currentButton != null)
            {
                this.currentButton.setMessage(new TranslatableComponent("vip3.undefined"));
                this.currentButton = null;

                this.updateStep();
                return true;
            }
        }

        if (this.currentButton == this.pauseResumeButton)
        {
            this.pauseScanCode = scanCode;
            this.pauseResumeButton.setMessage(new TextComponent("" + scanCode));
            this.currentButton = null;
            this.updateStep();
            return true;
        }

        if(this.currentButton == this.stopButton)
        {
            this.stopScanCode = scanCode;
            this.stopButton.setMessage(new TextComponent("" + scanCode));
            this.currentButton = null;
            this.updateStep();
            return true;
        }

        if(this.currentButton == this.skipButton)
        {
            this.skipScanCode = scanCode;
            this.skipButton.setMessage(new TextComponent("" + scanCode));
            this.currentButton = null;
            this.updateStep();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderBackground(pPoseStack);

        drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, this.height / 4 - 60 + 20, 16777215);

        if(this.step == 0)
        {
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.1"), this.width / 2 - 140, this.height / 4 - 60 + 60, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.2"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.3"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.4"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.5"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 54, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.6"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 63, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.7"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 72, 10526880);
            drawString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.message.8"), this.width / 2 - 140, this.height / 4 - 60 + 60 + 81, 10526880);
        }

        if(this.step == 1)
        {
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.ass.intro"), this.xCenter, this.height / 4, 0xFFFFFF);
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.ass.hint.1"), this.xCenter, this.height / 4 + 10, 10526880);
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.ass.hint.2"), this.xCenter, this.height / 4 + 20, 10526880);
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.ass.pause_resume_key"), this.xCenter - this.largeButton + this.smallButton / 2 - spacing / 2, this.height / 4 + 50, 0xFFFFFF);
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.ass.stop_key"), this.xCenter, this.height / 4 + 50, 0xFFFFFF);
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.ass.skip_key"), this.xCenter + this.spacing / 2 + this.largeButton - this.smallButton / 2, this.height / 4 + 50, 0xFFFFFF);
            this.pauseResumeButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.stopButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.skipButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        if (this.step == this.maxStep)
            drawCenteredString(pPoseStack, this.font, new TranslatableComponent("vip3.setup.done").withStyle(Style.EMPTY.withBold(true)), this.width / 2, this.height / 4 - 60 + 70, 16777215);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
