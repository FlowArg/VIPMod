package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.purifier.VPurifierEntity;
import fr.flowarg.vip3.features.purifier.VPurifierMenu;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.VProgressPurifierPacket;
import fr.flowarg.vip3.network.VResetMachineStatsPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class VPurifierScreen extends AbstractContainerScreen<VPurifierMenu>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/container/vipium_purifier.png");

    private boolean widthTooNarrow;
    private final BlockPos blockPos;
    private final VPurifierMenu container;
    private final VToolTip statsTooltip;

    public VPurifierScreen(VPurifierMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        final var result = playerInventory.player.pick(20.0D, 0.0F, false);
        if(result instanceof BlockHitResult blockHitResult)
            this.blockPos = blockHitResult.getBlockPos();
        else this.blockPos = null;
        this.container = menu;
        this.statsTooltip = new VToolTip(this, "Réinitialiser les statistiques");
    }

    @Override
    public void init()
    {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;

        this.addRenderableWidget(new Button(this.leftPos + 62, this.topPos + 52, 52, 20, new TextComponent("Purifier"), pButton -> {
            if(!this.container.getItem(VPurifierEntity.SLOT_INPUT).isEmpty())
            {
                this.minecraft.level.playLocalSound(this.blockPos.getX() + 0.5d, this.blockPos.getY(), this.blockPos.getZ() + 0.5d, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 0.4F, 1.0F, false);
                VNetwork.SYNC_CHANNEL.sendToServer(new VProgressPurifierPacket(this.blockPos));
            }
        }));
        this.addRenderableWidget(new VButton(this.leftPos + 137, this.topPos + 31, VWidgets.STATS_RESET, button -> VNetwork.SYNC_CHANNEL.sendToServer(new VResetMachineStatsPacket(this.blockPos, VResetMachineStatsPacket.TYPE_PURIFIER)), this.statsTooltip));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        if (this.widthTooNarrow) this.renderBg(poseStack, partialTicks, mouseX, mouseY);
        else super.render(poseStack, mouseX, mouseY, partialTicks);

        final var lockedItemStack = this.container.getItem(VPurifierEntity.SLOT_INPUT);
        final var color = "\u00A7b";
        final var lockedItems = lockedItemStack.getCount();

        if(mouseX >= 4 + this.leftPos && mouseX <= 34 + this.leftPos && mouseY >= 4 + this.topPos && mouseY <= 34 + this.topPos)
        {
            final var shift = hasShiftDown();
            final var mandatoryComponents = List.of(
                    new TextComponent(color + I18n.get("container.common.stats")),
                    new TextComponent(String.format("\u00A7a%d -> %d \u2248 %s", lockedItems, lockedItems * 4, String.format("%.2f", lockedItems * 2.5d))),
                    new TextComponent("\u00A7d" + I18n.get("container.vipium_purifier.stats.total_ingots", this.container.getPurifiedIngotCount())),
                    new TextComponent("\u00A7d" + I18n.get("container.common.stats.total_fragments", this.container.getFragmentsResultCount()))
            );

            final var total = new ArrayList<>(mandatoryComponents);

            if(shift)
            {
                final var optional = List.of(
                        new TextComponent("\u00A7e" + I18n.get("container.common.stats.luck", String.format("%.2f", this.container.getLuck()))),
                        new TextComponent("\u00A7e" + I18n.get("container.common.stats.average_single", String.format("%.1f", this.container.getAverage())))
                );
                total.addAll(optional);
            }
            else total.add(new TextComponent("\u00A77\u00A7o" + "Shift for details"));
            this.renderComponentTooltip(poseStack, total, mouseX, mouseY, this.font);
        }

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(poseStack, this.leftPos + 63, this.topPos + 32, 176, 0, this.menu.getPurificationProgress(), 16);
    }
}
