package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.capabilities.armorconfiguration.ArmorConfigurationCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class VConfigureEffectsScreen extends Screen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/configure_effects.png");
    private int guiLeft;
    private int guiTop;
    private boolean doRenderEffects;
    private final int imageWidth = 199, imageHeight = 162;

    public VConfigureEffectsScreen()
    {
        super(new TranslatableComponent("screen.configure_effects"));
    }

    @Override
    protected void init()
    {
        this.guiLeft = (this.width - this.imageWidth) / 2;
        this.guiTop = (this.height - this.imageHeight) / 2;

        Minecraft.getInstance().player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
            final int firstColumn = this.guiLeft + 41;
            this.addRenderableWidget(new VConfigureEffectsCheckboxButton(firstColumn, this.guiTop + 20, armorConfiguration.helmetEffect(), 0, 3));
            this.addRenderableWidget(new VConfigureEffectsCheckboxButton(firstColumn, this.guiTop + 73, armorConfiguration.leggingsEffect(), 2, 1));
            this.addRenderableWidget(new VConfigureEffectsCheckboxButton(firstColumn, this.guiTop + 126, armorConfiguration.fullSet1Effect(), 4, 0, 1, 2, 3));

            final int secondColumn = this.guiLeft + 132;
            this.addRenderableWidget(new VConfigureEffectsCheckboxButton(secondColumn, this.guiTop + 20, armorConfiguration.chestPlateEffect(), 1, 2));
            this.addRenderableWidget(new VConfigureEffectsCheckboxButton(secondColumn, this.guiTop + 73, armorConfiguration.bootsEffect(), 3, 0));
            this.addRenderableWidget(new VConfigureEffectsCheckboxButton(secondColumn, this.guiTop + 126, armorConfiguration.fullSet2Effect(), 5, 0, 1, 2, 3));
        });

        this.doRenderEffects = !this.minecraft.player.getActiveEffects().isEmpty();
    }

    @Override
    public void render(@NotNull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        this.renderBackground(pMatrixStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pMatrixStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        drawCenteredString(pMatrixStack, this.font, this.title, this.width / 2, 7, 16777215);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);

        if (this.doRenderEffects)
            this.renderEffects(pMatrixStack);
    }

    private void renderEffects(PoseStack pPoseStack)
    {
        final var i = this.guiLeft - 124;
        final var collection = this.minecraft.player.getActiveEffects();
        if (!collection.isEmpty())
        {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            var j = 33;
            if (collection.size() > 5)
                j = 132 / (collection.size() - 1);

            final var iterable = collection.stream().filter(ForgeHooksClient::shouldRenderEffect).sorted().collect(Collectors.toList());

            this.renderBackgrounds(pPoseStack, i, j, iterable);
            this.renderIcons(pPoseStack, i, j, iterable);
            this.renderLabels(pPoseStack, i, j, iterable);
        }
    }

    private void renderBackgrounds(PoseStack pPoseStack, int pRenderX, int pYOffset, @NotNull Iterable<MobEffectInstance> pEffects)
    {
        RenderSystem.setShaderTexture(0, AbstractContainerScreen.INVENTORY_LOCATION);
        var i = this.guiTop;

        for(var ignored : pEffects)
        {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.blit(pPoseStack, pRenderX, i, 0, 166, 140, 32);
            i += pYOffset;
        }
    }

    private void renderIcons(PoseStack pPoseStack, int pRenderX, int pYOffset, @NotNull Iterable<MobEffectInstance> pEffects)
    {
        final var effectTextureManager = this.minecraft.getMobEffectTextures();
        var i = this.guiTop;

        for(var effectInstance : pEffects)
        {
            final var mobEffect = effectInstance.getEffect();
            final var textureAtlasSprite = effectTextureManager.get(mobEffect);
            RenderSystem.setShaderTexture(0, textureAtlasSprite.atlas().location());
            blit(pPoseStack, pRenderX + 6, i + 7, this.getBlitOffset(), 18, 18, textureAtlasSprite);
            i += pYOffset;
        }
    }

    private void renderLabels(PoseStack pPoseStack, int pRenderX, int pYOffset, @NotNull Iterable<MobEffectInstance> pEffects)
    {
        var i = this.guiTop;

        for(var mobEffectInstance : pEffects)
        {
            var s = I18n.get(mobEffectInstance.getEffect().getDescriptionId());
            if (mobEffectInstance.getAmplifier() >= 1 && mobEffectInstance.getAmplifier() <= 9)
                s = s + " " + I18n.get("enchantment.level." + (mobEffectInstance.getAmplifier() + 1));

            this.font.drawShadow(pPoseStack, s, (float)(pRenderX + 10 + 18), (float)(i + 6), 16777215);
            final var s1 = MobEffectUtil.formatDuration(mobEffectInstance, 1.0F);
            this.font.drawShadow(pPoseStack, s1, (float)(pRenderX + 10 + 18), (float)(i + 6 + 10), 8355711);
            i += pYOffset;
        }
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
