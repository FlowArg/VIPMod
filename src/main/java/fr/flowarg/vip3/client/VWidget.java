package fr.flowarg.vip3.client;

import fr.flowarg.vip3.VIP3;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface VWidget
{
    ResourceLocation WIDGETS = new ResourceLocation(VIP3.MOD_ID, "textures/gui/widgets.png");
}
