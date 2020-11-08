package fr.flowarg.vipium.common.blocks.ores;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class VipiumOre extends OreBlock
{
    public VipiumOre()
    {
        super(Block.Properties.create(Material.ROCK)
                .harvestTool(ToolType.PICKAXE)
                .hardnessAndResistance(25f, 20f));
    }

    @Override
    protected int getExperience(Random random)
    {
        return MathHelper.nextInt(random, 12, 50);
    }
}
