package fr.flowarg.vipium.client.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vipium.VIPMod;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.gui.NotificationModUpdateScreen;
import net.minecraftforge.fml.BrandingControl;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.Objects;
import java.util.Random;

import static net.minecraftforge.fml.VersionChecker.Status.BETA;
import static net.minecraftforge.fml.VersionChecker.Status.BETA_OUTDATED;

@OnlyIn(Dist.CLIENT)
public class CustomMainMenuScreen extends Screen
{
    public static final RenderSkyboxCube PANORAMA_RESOURCES = new RenderSkyboxCube(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private static final ResourceLocation ACCESSIBILITY_TEXTURES = new ResourceLocation("textures/gui/accessibility.png");

    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation MINECRAFT_TITLE_EDITION = new ResourceLocation("textures/gui/title/edition.png");
    private final RenderSkybox panorama = new RenderSkybox(PANORAMA_RESOURCES);
    private final boolean showFadeInAnimation;
    private final boolean showTitleWronglySpelled;
    private int widthCopyright;
    private int widthCopyrightRest;
    private long firstRenderTime;
    private String splashText;
    private NotificationModUpdateScreen modUpdateNotification;

    public CustomMainMenuScreen(boolean fadeIn)
    {
        super(new TranslationTextComponent("narrator.screen.title"));
        this.showFadeInAnimation = fadeIn;
        this.showTitleWronglySpelled = (double)(new Random()).nextFloat() < 1.0E-4D;
        VIPMod.clientManager.getRpcManager().makeChanges(rpc -> {
            rpc.details = "In main menu";
            rpc.state = "Ready to play !";
        });
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
    protected void init()
    {
        if (this.splashText == null)
        {
            if (this.minecraft != null)
                this.splashText = this.minecraft.getSplashes().getSplashText();
        }

        this.widthCopyright = this.font.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.widthCopyrightRest = this.width - this.widthCopyright - 2;
        int y = this.height / 4 + 48;

        this.addButton(new Button(this.width / 2 - 100, y, 200, 20, I18n.format("menu.singleplayer"), (p_213089_1_) -> {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new WorldSelectionScreen(this));
        }));
        this.addButton(new Button(this.width / 2 - 100, y + 24, 200, 20, I18n.format("menu.multiplayer"), (p_213086_1_) -> {
            if (this.minecraft != null)
            {
                if (!this.minecraft.gameSettings.field_230152_Z_)
                    this.minecraft.gameSettings.field_230152_Z_ = true;
                this.minecraft.displayGuiScreen(new MultiplayerScreen(this));
            }
        }));
        final Button modButton = this.addButton(new Button(this.width / 2 - 100, y + 24 * 2, 200, 20, I18n.format("fml.menu.mods"), button -> {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new ModListScreen(this));
        }));

        this.addButton(new ImageButton(this.width / 2 - 124, y + 72 + 12, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION, 256, 256, (p_213090_1_) -> {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new LanguageScreen(this, this.minecraft.gameSettings, this.minecraft.getLanguageManager()));
        }, I18n.format("narrator.button.language")));
        this.addButton(new Button(this.width / 2 - 100, y + 72 + 12, 98, 20, I18n.format("menu.options"), (p_213096_1_) -> {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new CustomOptionsScreen(this, this.minecraft.gameSettings));
        }));
        this.addButton(new Button(this.width / 2 + 2, y + 72 + 12, 98, 20, I18n.format("menu.quit"), (p_213094_1_) -> {
            if (this.minecraft != null)
                this.minecraft.shutdown();
        }));
        this.addButton(new ImageButton(this.width / 2 + 104, y + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURES, 32, 64, (p_213088_1_) -> {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new AccessibilityScreen(this, this.minecraft.gameSettings));
        }, I18n.format("narrator.button.accessibility")));
        if (this.minecraft != null)
            this.minecraft.setConnectedToRealms(false);
        this.modUpdateNotification = new NotificationModUpdateScreen(modButton);
        this.modUpdateNotification.init(this.minecraft, this.width, this.height);
        this.modUpdateNotification.init();
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        if (this.firstRenderTime == 0L && this.showFadeInAnimation)
            this.firstRenderTime = Util.milliTime();

        float f = this.showFadeInAnimation ? (float)(Util.milliTime() - this.firstRenderTime) / 1000.0F : 1.0F;
        fill(0, 0, this.width, this.height, -1);
        this.panorama.render(p_render_3_, MathHelper.clamp(f, 0.0F, 1.0F));
        int j = this.width / 2 - 137;
        if (this.minecraft != null)
            this.minecraft.getTextureManager().bindTexture(PANORAMA_OVERLAY_TEXTURES);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.showFadeInAnimation ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
        blit(0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float f1 = this.showFadeInAnimation ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(f1 * 255.0F) << 24;
        if ((l & -67108864) != 0)
        {
            this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURES);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
            if (this.showTitleWronglySpelled)
            {
                this.blit(j, 30, 0, 0, 99, 44);
                this.blit(j + 99, 30, 129, 0, 27, 44);
                this.blit(j + 99 + 26, 30, 126, 0, 3, 44);
                this.blit(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
                this.blit(j + 155, 30, 0, 45, 155, 44);
            }
            else
            {
                this.blit(j, 30, 0, 0, 155, 44);
                this.blit(j + 155, 30, 0, 45, 155, 44);
            }

            this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_EDITION);
            blit(j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);

            final VersionChecker.Status status = ForgeVersion.getStatus();
            if (status == BETA || status == BETA_OUTDATED)
            {
                String line = I18n.format("forge.update.beta.1", TextFormatting.RED, TextFormatting.RESET);
                this.drawString(font, line, (width - font.getStringWidth(line)) / 2, 4, -1);
                line = I18n.format("forge.update.beta.2");
                this.drawString(font, line, (width - font.getStringWidth(line)) / 2, 4 + (font.FONT_HEIGHT + 1), -1);
            }

            String line = null;
            switch (status)
            {
                case OUTDATED:
                case BETA_OUTDATED:
                    line = I18n.format("forge.update.newversion", ForgeVersion.getTarget());
                    break;
                default:
                    break;
            }

            ForgeHooksClient.forgeStatusLine = line;
            if (this.splashText != null)
            {
                RenderSystem.pushMatrix();
                RenderSystem.translatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
                RenderSystem.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
                float f2 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Util.milliTime() % 1000L) / 1000.0F * ((float)Math.PI * 2F)) * 0.1F);
                f2 = f2 * 100.0F / (float)(this.font.getStringWidth(this.splashText) + 32);
                RenderSystem.scalef(f2, f2, f2);
                this.drawCenteredString(this.font, this.splashText, 0, -8, 16776960 | l);
                RenderSystem.popMatrix();
            }

            String s = "Minecraft " + SharedConstants.getVersion().getName();
            s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());

            if (this.minecraft.func_230151_c_())
            {
                s = s + I18n.format("menu.modded");
            }

            BrandingControl.forEachLine(true, true, (brdline, brd) -> this.drawString(this.font, brd, 2, this.height - (10 + brdline * (this.font.FONT_HEIGHT + 1)), 16777215 | l));
            BrandingControl.forEachAboveCopyrightLine((brdline, brd) -> this.drawString(this.font, brd, this.width - font.getStringWidth(brd), this.height - (10 + (brdline + 1) * (this.font.FONT_HEIGHT + 1)), 16777215 | l));
            this.drawString(this.font, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, 16777215 | l);
            if (p_render_1_ > this.widthCopyrightRest && p_render_1_ < this.widthCopyrightRest + this.widthCopyright && p_render_2_ > this.height - 10 && p_render_2_ < this.height)
                fill(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, 16777215 | l);

            for (Widget widget : this.buttons)
                widget.setAlpha(f1);

            super.render(p_render_1_, p_render_2_, p_render_3_);
            modUpdateNotification.render(p_render_1_, p_render_2_, p_render_3_);
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
    {
        if (super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_))
            return true;
        else
        {
            if (p_mouseClicked_1_ > (double)this.widthCopyrightRest && p_mouseClicked_1_ < (double)(this.widthCopyrightRest + this.widthCopyright) && p_mouseClicked_3_ > (double)(this.height - 10) && p_mouseClicked_3_ < (double)this.height)
                Objects.requireNonNull(this.minecraft).displayGuiScreen(new WinGameScreen(false, () -> {}));
            return false;
        }
    }
}
