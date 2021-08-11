package fr.flowarg.vip3.features;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class VMultiToolItem extends DiggerItem
{
    public VMultiToolItem(float attackDamageIn, float attackSpeedIn, Tier tier, Properties builder)
    {
        super(attackDamageIn, attackSpeedIn, tier, BlockTags.MINEABLE_WITH_AXE, builder);
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state)
    {
        return true;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state)
    {
        return this.speed;
    }
}
