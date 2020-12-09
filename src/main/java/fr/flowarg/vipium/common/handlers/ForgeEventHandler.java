package fr.flowarg.vipium.common.handlers;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.client.screens.CustomInGameMenuScreen;
import fr.flowarg.vipium.client.screens.CustomMainMenuScreen;
import fr.flowarg.vipium.common.utils.VipiumConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VIPMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler
{
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiOpenedEvent(final GuiOpenEvent event)
    {
        if (event.getGui() != null)
        {
            if(!VipiumConfig.CLIENT.canShowRealms().get() && event.getGui().getClass() == MainMenuScreen.class)
                event.setGui(new CustomMainMenuScreen(true));
            else if(!VipiumConfig.CLIENT.canShowUselessOptions().get() && event.getGui().getClass() == IngameMenuScreen.class)
                event.setGui(new CustomInGameMenuScreen(!Minecraft.getInstance().isIntegratedServerRunning()));
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(!event.getPlayer().getServer().isSinglePlayer())
        {
            if(event.getPlayer().getServer().getServerHostname().contains("flowargbyfistin"))
            {
                VIPMod.clientManager.getRpcManager().makeChanges(rpc -> {
                    rpc.details = "Playing in V.I.P";
                    rpc.state = "Connected";
                });
            }
        }
    }

    @SubscribeEvent
    public static void onArmorTick(TickEvent.PlayerTickEvent event)
    {
        final PlayerEntity player = event.player;
        if(!player.world.isRemote)
        {
            final PlayerInventory inventory = event.player.inventory;
            final boolean[] bools = new boolean[] {false, false, false, false};
            if(inventory.armorInventory.get(0).getItem() == RegistryHandler.VIPIUM_PURE_BOOTS.get())
            {
                player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 30, 4, false, false));
                bools[0] = true;
            }
            if(inventory.armorInventory.get(1).getItem() == RegistryHandler.VIPIUM_PURE_LEGGINGS.get())
            {
                player.addPotionEffect(new EffectInstance(Effects.SPEED, 30, 2, false, true));
                bools[1] = true;
            }
            if(inventory.armorInventory.get(2).getItem() == RegistryHandler.VIPIUM_PURE_CHESTPLATE.get())
            {
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 30, 4, false, true));
                bools[2] = true;
            }
            if(inventory.armorInventory.get(3).getItem() == RegistryHandler.VIPIUM_PURE_HELMET.get())
            {
                player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 30, 4, false, false));
                bools[3] = true;
            }

            if(bools[0] && bools[1] && bools[2] && bools[3])
            {
                player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 10, 2, true, false));
                player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 2, true, false));
            }
        }
    }
}
