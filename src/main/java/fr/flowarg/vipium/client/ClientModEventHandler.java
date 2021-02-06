package fr.flowarg.vipium.client;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.client.renderer.VIPChestRenderer;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = VIPMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler
{    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event)
    {
        if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS))
            return;

        VIPMod.LOGGER.info(VIPMod.MARKER, "Adding VipiumChest Texture in Chest Atlas");
        event.addSprite(VIPChestRenderer.CHEST_TEXTURE_LOCATION);
    }
}
