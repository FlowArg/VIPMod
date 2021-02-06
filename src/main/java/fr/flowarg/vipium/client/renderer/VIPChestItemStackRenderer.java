package fr.flowarg.vipium.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.flowarg.vipium.common.tileentities.VIPChestTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class VIPChestItemStackRenderer extends ItemStackTileEntityRenderer
{
    private final Supplier<VIPChestTileEntity> te;

    public VIPChestItemStackRenderer(Supplier<VIPChestTileEntity> te)
    {
        this.te = te;
    }

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        TileEntityRendererDispatcher.instance.renderItem(this.te.get(), matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
