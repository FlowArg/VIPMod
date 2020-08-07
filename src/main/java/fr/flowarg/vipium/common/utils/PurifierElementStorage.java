package fr.flowarg.vipium.common.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PurifierElementStorage
{
    private final Collection<Block> quartz;
    private final Block purifier;
    private final PlayerEntity player;
    private final List<Block> columns;
    private final List<Block> vipiumBlocks;

    public PurifierElementStorage(Collection<Block> quartz, Block purifier, PlayerEntity player, List<Block> columns, List<Block> vipiumBlocks)
    {
        this.quartz = new ArrayList<>();
        quartz.forEach(block -> {
            if (block != null)
                this.quartz.add(block);
        });
        this.purifier = purifier;
        this.player = player;
        this.columns = columns;
        this.vipiumBlocks = vipiumBlocks;
    }

    public Collection<Block> getQuartz()
    {
        return this.quartz;
    }

    public Block getPurifier()
    {
        return this.purifier;
    }

    public PlayerEntity getPlayer()
    {
        return this.player;
    }

    public List<Block> getColumns()
    {
        return this.columns;
    }

    public List<Block> getVipiumBlocks()
    {
        return this.vipiumBlocks;
    }
}
