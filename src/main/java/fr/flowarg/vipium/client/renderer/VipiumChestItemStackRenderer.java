package fr.flowarg.vipium.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.flowarg.vipium.common.tileentities.VipiumChestTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class VipiumChestItemStackRenderer extends ItemStackTileEntityRenderer
{
    private final Supplier<VipiumChestTileEntity> te;

    public VipiumChestItemStackRenderer(Supplier<VipiumChestTileEntity> te)
    {
        this.te = te;
    }

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        TileEntityRendererDispatcher.instance.renderItem(this.te.get(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
