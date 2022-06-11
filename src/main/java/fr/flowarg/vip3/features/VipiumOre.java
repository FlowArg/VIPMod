package fr.flowarg.vip3.features;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

public class VipiumOre extends OreBlock
{
    public VipiumOre()
    {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().lightLevel(value -> 8).strength(25f, 24f));
    }

    @Override
    public int getExpDrop(@NotNull BlockState state, @NotNull LevelReader reader, @NotNull BlockPos pos, int fortune, int silktouch)
    {
        return Mth.nextInt(RANDOM, 13, 89);
    }
}
