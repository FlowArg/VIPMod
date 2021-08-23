package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VCrusherEntity;
import fr.flowarg.vip3.features.VCrusherMenu;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.VResetCrusherDataPacket;
import fr.flowarg.vip3.network.VStartStopCrusherPacket;
import fr.flowarg.vip3.network.VSwapSlotCrusherPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class VCrusherScreen extends AbstractContainerScreen<VCrusherMenu>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/container/vipium_crusher.png");

    private boolean widthTooNarrow;
    private final BlockPos blockPos;
    private final VCrusherMenu container;

    public VCrusherScreen(VCrusherMenu menu, Inventory inventory, Component component)
    {
        super(menu, inventory, component);
        final var result = inventory.player.pick(20.0D, 0.0F, false);
        if(result instanceof BlockHitResult blockHitResult)
            this.blockPos = blockHitResult.getBlockPos();
        else this.blockPos = null;
        this.container = menu;
    }

    @Override
    public void init()
    {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;

        this.addRenderableWidget(new Button(this.leftPos - 50, this.topPos, 40, 20, new TextComponent("START"), button -> VNetwork.SYNC_CHANNEL.sendToServer(new VStartStopCrusherPacket(this.blockPos, true))));
        this.addRenderableWidget(new Button(this.leftPos - 50, this.topPos + 25, 40, 20, new TextComponent("STOP"), button -> VNetwork.SYNC_CHANNEL.sendToServer(new VStartStopCrusherPacket(this.blockPos, false))));
        this.addRenderableWidget(new Button(this.leftPos - 50, this.topPos + 50, 40, 20, new TextComponent("+"), button ->  {
            if(!this.container.getItem(VCrusherEntity.SLOT_INPUT).isEmpty())
                VNetwork.SYNC_CHANNEL.sendToServer(new VSwapSlotCrusherPacket(this.blockPos, VCrusherEntity.SLOT_INPUT, VCrusherEntity.SLOT_LOCKED));
        }));
        this.addRenderableWidget(new Button(this.leftPos - 50, this.topPos + 75, 40, 20, new TextComponent("-"), button ->  {
            if(!this.container.getItem(VCrusherEntity.SLOT_LOCKED).isEmpty())
                VNetwork.SYNC_CHANNEL.sendToServer(new VSwapSlotCrusherPacket(this.blockPos, VCrusherEntity.SLOT_LOCKED, VCrusherEntity.SLOT_INPUT));
        }));
        this.addRenderableWidget(new Button(this.leftPos - 50, this.topPos + 100, 40, 20, new TextComponent("RESET"), button -> VNetwork.SYNC_CHANNEL.sendToServer(new VResetCrusherDataPacket(this.blockPos))));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        if (this.widthTooNarrow) this.renderBg(poseStack, partialTicks, mouseX, mouseY);
        else super.render(poseStack, mouseX, mouseY, partialTicks);

        final var lockedItemStack = this.container.getItem(VCrusherEntity.SLOT_LOCKED);
        final var color = "\u00A7" + (lockedItemStack.is(VObjects.VIPIUM_INGOT.get()) ? "b" : lockedItemStack.is(VObjects.PURE_VIPIUM_INGOT.get()) ? "c" : "7");
        final var lockedItems = lockedItemStack.getCount();

        if(mouseX >= 6 + this.leftPos && mouseX <= 36 + this.leftPos && mouseY >= 10 + this.topPos && mouseY <= 40 + this.topPos)
        {
            final var shift = hasShiftDown();
            final var mandatoryComponents = List.of(
                    new TextComponent(color + I18n.get("container.vipium_crusher.stats")),
                    new TextComponent(String.format("\u00A7a%d -> %d \u2248 %d", lockedItems, lockedItems * 6, lockedItems * 3)),
                    new TextComponent("\u00A7d" + I18n.get("container.vipium_crusher.stats.total_ingots", this.container.getCrushedIngotCount())),
                    new TextComponent("\u00A7d" + I18n.get("container.vipium_crusher.stats.total_fragments", this.container.getFragmentsResultCount()))
            );
            final var total = new ArrayList<>(mandatoryComponents);

            if(shift)
            {
                final var optional = List.of(
                        new TextComponent("\u00A7e" + I18n.get("container.vipium_crusher.stats.luck", String.format("%.2f", this.container.getLuck()))),
                        new TextComponent("\u00A7e" + I18n.get("container.vipium_crusher.stats.average_single", String.format("%.1f", this.container.getAverage())))
                );
                total.addAll(optional);
            }
            else total.add(new TextComponent("\u00A77\u00A7o" + "Shift for details"));
            this.renderComponentToolTip(poseStack, total, mouseX, mouseY, this.font);
        }

        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        final var i = this.leftPos;
        final var j = this.topPos;

        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(poseStack, i + 95, j + 47, 176, 14, this.menu.getBurnProgress() + 1, 16);
    }
}
