package fr.flowarg.vipium.common.core;

import net.minecraft.block.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandler
{
    @SubscribeEvent
    public void onBreakFire(BlockEvent.BreakEvent event)
    {
        if(event.getState().getBlock() == Blocks.FIRE)
        {
            event.getPlayer().setHealth(event.getPlayer().getHealth() - 0.5f);
            event.getPlayer().setFire(1);
        }
    }
}
