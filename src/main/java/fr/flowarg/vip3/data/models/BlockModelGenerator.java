package fr.flowarg.vip3.data.models;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class BlockModelGenerator extends BlockStateProvider
{
    public BlockModelGenerator(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, VIP3.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        this.buildSimpleBlock(VObjects.VIPIUM_BLOCK.get());
        this.buildSimpleBlock(VObjects.PURE_VIPIUM_BLOCK.get());
        this.buildSimpleBlock(VObjects.VIPIUM_ORE.get());
        this.buildSimpleBlock(VObjects.DEEPSLATE_VIPIUM_ORE.get());
    }

    private void buildSimpleBlock(Block block)
    {
        this.simpleBlock(block);
        this.itemModels().withExistingParent(Objects.requireNonNull(block.getRegistryName()).getPath(), this.modLoc("block/" + block.getRegistryName().getPath()));
    }
}
