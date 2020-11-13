package fr.flowarg.vipium.client.renderer;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.entities.VipiumMinecartEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VipiumMinecartRenderer extends MinecartRenderer<VipiumMinecartEntity>
{
    public static final ResourceLocation MINECART_TEXTURE = new ResourceLocation(VIPMod.MODID, "textures/entity/vipium_minecart.png");

    public VipiumMinecartRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(VipiumMinecartEntity entity)
    {
        return MINECART_TEXTURE;
    }
}
