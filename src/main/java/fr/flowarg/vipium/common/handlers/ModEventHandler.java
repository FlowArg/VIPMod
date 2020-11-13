package fr.flowarg.vipium.common.handlers;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.client.renderer.VipiumChestRenderer;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventHandler
{    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event)
    {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS))
            return;

        Main.LOGGER.info(Main.MARKER, "Adding VipiumChest Texture in Chest Atlas");
        event.addSprite(VipiumChestRenderer.CHEST_TEXTURE_LOCATION);
    }
}
