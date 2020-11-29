package fr.flowarg.vipium.common.handlers;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.client.screens.CustomInGameMenuScreen;
import fr.flowarg.vipium.client.screens.CustomMainMenuScreen;
import fr.flowarg.vipium.common.utils.VipiumConfig;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = VIPMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler
{
    public static final BlockPos PRESSURE_PLATE_EMPLACEMENT = new BlockPos(-179, 79, 137);
    public static final BlockPos OAK_PLANKS_EMPLACEMENT = new BlockPos(-179, 78, 137);
    public static final BlockPos TNT_EMPLACEMENT = new BlockPos(-179, 77, 137);
    public static final Random RANDOM = new Random();

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
    public static void onPressurePlateActivation(final BlockEvent.NeighborNotifyEvent event)
    {
        if(event.getState().getBlock() instanceof AbstractPressurePlateBlock)
        {
            if(event.getPos().equals(PRESSURE_PLATE_EMPLACEMENT))
            {
                if(event.getWorld().getBlockState(OAK_PLANKS_EMPLACEMENT).getBlock() == Blocks.OAK_PLANKS)
                {
                    if(RANDOM.nextInt(150) == 26)
                    {
                        event.getWorld().setBlockState(TNT_EMPLACEMENT, Blocks.TNT.getDefaultState(), 1);
                    }
                }
            }
        }
    }
}
