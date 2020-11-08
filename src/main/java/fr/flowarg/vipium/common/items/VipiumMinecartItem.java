package fr.flowarg.vipium.common.items;

import fr.flowarg.vipium.common.entities.VipiumMinecartEntity;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VipiumMinecartItem extends Item
{
    public VipiumMinecartItem()
    {
        super(RegistryHandler.newItemVipiumProperties());
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.getBlock() instanceof AbstractRailBlock)
        {
            ItemStack itemstack = context.getItem();
            if (!world.isRemote)
            {
                RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock)blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0D;
                if (railshape.isAscending())
                {
                    d0 = 0.5D;
                }

                final VipiumMinecartEntity entity = new VipiumMinecartEntity((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.0625D + d0, (double)blockpos.getZ() + 0.5D, world);
                if (itemstack.hasDisplayName())
                    entity.setCustomName(itemstack.getDisplayName());

                world.addEntity(entity);
            }

            itemstack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        else return ActionResultType.FAIL;
    }
}
