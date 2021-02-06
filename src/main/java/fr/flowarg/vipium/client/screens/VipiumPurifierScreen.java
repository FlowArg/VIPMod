package fr.flowarg.vipium.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.containers.VipiumPurifierContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VipiumPurifierScreen extends ContainerScreen<VipiumPurifierContainer>
{
    private boolean widthTooNarrow;
    private static final ResourceLocation VIPIUM_PURIFIER_GUI_TEXTURE = new ResourceLocation(VIPMod.MODID, "textures/gui/container/vipium_purifier.png");

    public VipiumPurifierScreen(VipiumPurifierContainer container, PlayerInventory inventory, ITextComponent titleIn)
    {
        super(container, inventory, titleIn);
    }

    @Override
    protected void init()
    {
        super.init();
        this.widthTooNarrow = this.width < 379;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();
        if (this.widthTooNarrow)
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        else super.render(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        final String title = this.title.getFormattedText();
        this.font.drawString(title, (float)(this.xSize / 2 - this.font.getStringWidth(title) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 50.0F, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.minecraft != null)
            this.minecraft.getTextureManager().bindTexture(VIPIUM_PURIFIER_GUI_TEXTURE);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
