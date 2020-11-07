package fr.flowarg.vipium.common.blocks;

import net.minecraft.block.RailBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class VipiumRailBlock extends RailBlock
{
    public VipiumRailBlock()
    {
        super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.7f).sound(SoundType.METAL));
    }
}
