package fr.flowarg.vipium.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.containers.VipiumChestContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VipiumChestScreen extends ContainerScreen<VipiumChestContainer> implements IHasContainer<VipiumChestContainer>
{
    private static final ResourceLocation VIPIUM_CHEST_GUI_TEXTURE = new ResourceLocation(VIPMod.MODID, "textures/gui/container/vipium_chest.png");

    public VipiumChestScreen(VipiumChestContainer chest, PlayerInventory inv, ITextComponent titleIn)
    {
        super(chest, inv, titleIn);
        this.passEvents = false;
        this.xSize = 254;
        this.ySize = 222;
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(VIPIUM_CHEST_GUI_TEXTURE);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        blit(x, y, 0, 0, this.xSize, this.ySize, 256, 256);
    }
}
