package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VCrusherEntity;
import fr.flowarg.vip3.features.VCrusherMenu;
import fr.flowarg.vip3.network.VInputSlotCrusherPacket;
import fr.flowarg.vip3.network.VLockSlotCrusherPacket;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.VStartStopCrusherPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

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
        this.addRenderableWidget(new Button(20, 20, 20, 20, new TextComponent("START"), button -> VNetwork.SYNC_CHANNEL.sendToServer(new VStartStopCrusherPacket(this.blockPos, true))));
        this.addRenderableWidget(new Button(60, 20, 20, 20, new TextComponent("STOP"), button -> VNetwork.SYNC_CHANNEL.sendToServer(new VStartStopCrusherPacket(this.blockPos, false))));
        this.addRenderableWidget(new Button(100, 20, 20, 20, new TextComponent("+"), button ->  {
            final var itemStack = this.container.getItem(VCrusherEntity.SLOT_INPUT);

            if(!itemStack.isEmpty())
                VNetwork.SYNC_CHANNEL.sendToServer(new VLockSlotCrusherPacket(this.blockPos, new ItemStack(itemStack.getItem(), 1)));

        }));
        this.addRenderableWidget(new Button(140, 20, 20, 20, new TextComponent("-"), button ->  {
            final var itemStack = this.container.getItem(VCrusherEntity.SLOT_LOCKED);

            if(!itemStack.isEmpty())
                VNetwork.SYNC_CHANNEL.sendToServer(new VInputSlotCrusherPacket(this.blockPos, new ItemStack(itemStack.getItem(), 1)));
        }));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(poseStack);
        if (this.widthTooNarrow) this.renderBg(poseStack, partialTicks, mouseX, mouseY);
         else super.render(poseStack, mouseX, mouseY, partialTicks);

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
        this.blit(poseStack, i + 101, j + 47, 176, 14, this.menu.getBurnProgress() + 1, 16);
    }
}
