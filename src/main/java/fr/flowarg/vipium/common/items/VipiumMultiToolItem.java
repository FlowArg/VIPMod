package fr.flowarg.vipium.common.items;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public class VipiumMultiToolItem extends ToolItem
{
    public VipiumMultiToolItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Properties builder)
    {
        super(attackDamageIn, attackSpeedIn, tier, Collections.emptySet(), builder);
    }

    @Override
    public boolean canHarvestBlock(@Nonnull BlockState blockIn)
    {
        return true;
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state)
    {
        return true;
    }

    @Nonnull
    @Override
    public Set<ToolType> getToolTypes(@Nonnull ItemStack stack)
    {
        return ImmutableSet.of(ToolType.PICKAXE, ToolType.SHOVEL, ToolType.AXE);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state)
    {
        return this.efficiency;
    }
}
