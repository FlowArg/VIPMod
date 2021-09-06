package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.network.capabilities.ArmorConfiguration;
import fr.flowarg.vip3.network.capabilities.CapabilitiesEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigureEffectsScreen extends Screen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/configure_effects.png");
    private int guiLeft;
    private int guiTop;

    public ConfigureEffectsScreen()
    {
        super(new TranslatableComponent("screen.configure_effects"));
    }

    @Override
    protected void init()
    {
        this.guiLeft = (this.width - 199) / 2;
        this.guiTop = (this.height - 162) / 2;

        Minecraft.getInstance().player.getCapability(CapabilitiesEventHandler.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
            this.addRenderableWidget(new VConfigureEffectsButton(this.guiLeft + 41, this.guiTop + 20, armorConfiguration.chestPlateEffect(), ArmorConfiguration::chestPlateEffect, 2));
            this.addRenderableWidget(new VConfigureEffectsButton(this.guiLeft + 41, this.guiTop + 73, armorConfiguration.leggingsEffect(), ArmorConfiguration::leggingsEffect, 1));
            this.addRenderableWidget(new VConfigureEffectsButton(this.guiLeft + 41, this.guiTop + 126, armorConfiguration.fullSet1Effect(), ArmorConfiguration::fullSet1Effect, 0, 1, 2, 3));
            this.addRenderableWidget(new VConfigureEffectsButton(this.guiLeft + 132, this.guiTop + 20, armorConfiguration.helmetEffect(), ArmorConfiguration::helmetEffect, 3));
            this.addRenderableWidget(new VConfigureEffectsButton(this.guiLeft + 132, this.guiTop + 73, armorConfiguration.bootsEffect(), ArmorConfiguration::bootsEffect, 0));
            this.addRenderableWidget(new VConfigureEffectsButton(this.guiLeft + 132, this.guiTop + 126, armorConfiguration.fullSet2Effect(), ArmorConfiguration::fullSet2Effect, 0, 1, 2, 3));
        });
    }

    @Override
    public void render(@NotNull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        this.renderBackground(pMatrixStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pMatrixStack, this.guiLeft, this.guiTop, 0, 0, 199, 162);
        drawCenteredString(pMatrixStack, this.font, this.title, this.width / 2, 7, 16777215);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
