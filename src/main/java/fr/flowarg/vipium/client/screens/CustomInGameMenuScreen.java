package fr.flowarg.vipium.client.screens;

import net.minecraft.client.gui.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class CustomInGameMenuScreen extends Screen
{
    private final boolean isFullMenu;

    public CustomInGameMenuScreen(boolean multiplayer)
    {
        super(multiplayer ? new TranslationTextComponent("menu.game") : new TranslationTextComponent("menu.paused"));
        this.isFullMenu = true;
    }

    protected void init()
    {
        if (this.isFullMenu)
            this.addButtons();
    }

    private void addButtons()
    {
        this.addButton(new Button(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, I18n.format("menu.returnToGame"), (p_213070_1_) ->
        {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(null);
            this.minecraft.mouseHelper.grabMouse();
        }));
        this.addButton(new Button(this.width / 2 - 102, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.advancements"), (p_213065_1_) ->
        {
            if (this.minecraft != null)
            {
                if (this.minecraft.player != null)
                    this.minecraft.displayGuiScreen(new AdvancementsScreen(this.minecraft.player.connection.getAdvancementManager()));
            }
        }));
        this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 48 + -16, 98, 20, I18n.format("gui.stats"), (p_213066_1_) ->
        {
            if (this.minecraft != null)
            {
                if (this.minecraft.player != null)
                    this.minecraft.displayGuiScreen(new StatsScreen(this, this.minecraft.player.getStats()));
            }
        }));
        this.addButton(new Button(this.width / 2 - 102, this.height / 4 + 72 + -16, 98, 20, I18n.format("menu.options"), (p_213071_1_) ->
        {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new CustomOptionsScreen(this, this.minecraft.gameSettings));
        }));
        Button button = this.addButton(new Button(this.width / 2 + 4, this.height / 4 + 72 + -16, 98, 20, I18n.format("menu.shareToLan"), (p_213068_1_) ->
        {
            if (this.minecraft != null)
                this.minecraft.displayGuiScreen(new ShareToLanScreen(this));
        }));
        if (this.minecraft != null)
            button.active = this.minecraft.isSingleplayer() && !Objects.requireNonNull(this.minecraft.getIntegratedServer()).getPublic();
        Button button1 = this.addButton(new Button(this.width / 2 - 102, this.height / 4 + 96 + -16, 204, 20, I18n.format("menu.returnToMenu"), (button3) ->
        {
            boolean flag = this.minecraft.isIntegratedServerRunning();
            button3.active = false;

            if (this.minecraft.world != null) this.minecraft.world.sendQuittingDisconnectingPacket();
            if (flag) this.minecraft.unloadWorld(new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")));
            else this.minecraft.unloadWorld();

            if (flag) this.minecraft.displayGuiScreen(new CustomMainMenuScreen(true));
            else this.minecraft.displayGuiScreen(new MultiplayerScreen(new CustomMainMenuScreen(true)));
        }));
        if (!this.minecraft.isIntegratedServerRunning())
        {
            button1.setMessage(I18n.format("menu.disconnect"));
        }
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        if (this.isFullMenu)
        {
            this.renderBackground();
            this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 40, 16777215);
        }
        else
        {
            this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 10, 16777215);
        }

        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
