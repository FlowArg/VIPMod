package fr.flowarg.vip3.features;

import com.google.common.collect.ImmutableSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class VMultiToolItem extends DiggerItem
{
    public VMultiToolItem(float attackDamageIn, float attackSpeedIn, Tier tier, Properties builder)
    {
        super(attackDamageIn, attackSpeedIn, tier, BlockTags.MINEABLE_WITH_AXE, builder);
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, BlockState state)
    {
        return true;
    }

    @NotNull
    @Override
    public Set<ToolType> getToolTypes(@NotNull ItemStack stack)
    {
        return ImmutableSet.of(ToolType.PICKAXE, ToolType.SHOVEL, ToolType.AXE);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state)
    {
        return this.speed;
    }
}
