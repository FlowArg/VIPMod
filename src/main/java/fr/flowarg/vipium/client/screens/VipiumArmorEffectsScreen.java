package fr.flowarg.vipium.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.core.VipiumConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

@OnlyIn(Dist.CLIENT)
public class VipiumArmorEffectsScreen extends Screen
{
    private static final ResourceLocation VAES_TEXTURE = new ResourceLocation(VIPMod.MODID, "textures/gui/vipium_pure_armor_effects.png");
    private int guiLeft;
    private int guiTop;

    public VipiumArmorEffectsScreen()
    {
        super(new TranslationTextComponent("screen.vipium_armor_effects"));
    }

    @Override
    protected void init()
    {
        this.guiLeft = (this.width - 176) / 2;
        this.guiTop = (this.height - 166) / 2;

        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 8, VipiumConfig.CLIENT.getEnableHelmetEffect()));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 30, VipiumConfig.CLIENT.getEnableChestplateEffect()));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 52, VipiumConfig.CLIENT.getEnableLeggingsEffect()));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 74, VipiumConfig.CLIENT.getEnableBootsEffect()));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 102, VipiumConfig.CLIENT.getEnableFirstFullEffect()));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 124, VipiumConfig.CLIENT.getEnableSecondFullEffect()));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(this.minecraft != null)
            this.minecraft.getTextureManager().bindTexture(VAES_TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, 176, 166);
        this.font.drawString(this.title.getFormattedText(), this.guiLeft + 8, this.guiTop + 150.0F, 4210752);
        super.render(mouseX, mouseY, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    private static class VAESCheckbox extends CheckboxButton
    {
        private final IPressable onPress;

        public VAESCheckbox(int xOffset, int yIn, BooleanValue property)
        {
            super(xOffset + 150, yIn, 18, 18, "", property.get());
            this.onPress = checkbox -> property.set(checkbox.isChecked());
        }

        @Override
        public void onPress()
        {
            super.onPress();
            this.onPress.onPress(this);
        }

        @OnlyIn(Dist.CLIENT)
        @FunctionalInterface
        public interface IPressable
        {
            void onPress(VAESCheckbox checkbox);
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
