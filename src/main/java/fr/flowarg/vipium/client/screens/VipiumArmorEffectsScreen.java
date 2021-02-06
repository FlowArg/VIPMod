package fr.flowarg.vipium.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.capability.armorconfig.ArmorConfigCapability;
import fr.flowarg.vipium.common.capability.armorconfig.IArmorConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.CheckboxButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

        final boolean[] config = new boolean[IArmorConfig.DEFAULT.length];
        Minecraft.getInstance().player.getCapability(ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY).ifPresent(iArmorConfig -> {
            final int[] conf = iArmorConfig.getArmorConfig();
            for (int i = 0; i < conf.length; i++)
                config[i] = conf[i] == 1;
        });
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 8, config[0], 0));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 30, config[1], 1));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 52, config[2], 2));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 74, config[3], 3));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 102, config[4], 4));
        this.addButton(new VAESCheckbox(this.guiLeft, this.guiTop + 124, config[5], 5));
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

        public VAESCheckbox(int xOffset, int yIn, boolean property, int index)
        {
            super(xOffset + 150, yIn, 18, 18, "", property);
            this.onPress = checkbox -> Minecraft.getInstance().player.getCapability(ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY).ifPresent(iArmorConfig -> {
                final int[] current = iArmorConfig.getArmorConfig();
                current[index] = checkbox.active ? 1 : 0;
                iArmorConfig.setArmorConfig(current);
            });
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
