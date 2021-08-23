package fr.flowarg.vip3.data.models;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
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
        //this.buildOrientableFaceLitBlock(VObjects.VIPIUM_PURIFIER.get());
        this.buildOrientableFaceLitBlock(VObjects.VIPIUM_CRUSHER.get());
        this.buildCubeBottomTopModel(VObjects.TELEPORTATION_ALTAR.get());
    }

    private void buildSimpleBlock(Block block)
    {
        this.simpleBlock(block);
        this.item(block);
    }

    private void buildOrientableFaceLitBlock(Block block)
    {
        final var builder = this.getVariantBuilder(block);
        final var blockName = block.getRegistryName().getPath();

        final var normal = this.models().orientable(
                blockName,
                this.modLoc("block/" + blockName + "_side"),
                this.modLoc("block/" + blockName + "_front"),
                this.modLoc("block/" + blockName + "_top"));

        final var on = this.models().orientable(
                blockName + "_on",
                this.modLoc("block/" + blockName + "_side"),
                this.modLoc("block/" + blockName + "_front_on"),
                this.modLoc("block/" + blockName + "_top"));

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                  .with(BlockStateProperties.LIT, Boolean.FALSE),
                          ConfiguredModel.builder().rotationY(90).modelFile(normal).build());

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                  .with(BlockStateProperties.LIT, Boolean.TRUE),
                          ConfiguredModel.builder().rotationY(90).modelFile(on).build()
        );

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                  .with(BlockStateProperties.LIT, Boolean.FALSE),
                          ConfiguredModel.builder().modelFile(normal).build()
        );

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                  .with(BlockStateProperties.LIT, Boolean.TRUE),
                          ConfiguredModel.builder().modelFile(on).build()
        );

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                  .with(BlockStateProperties.LIT, Boolean.FALSE),
                          ConfiguredModel.builder().rotationY(180).modelFile(normal).build()
        );

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                  .with(BlockStateProperties.LIT, Boolean.TRUE),
                          ConfiguredModel.builder().rotationY(180).modelFile(on).build()
        );

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                  .with(BlockStateProperties.LIT, Boolean.FALSE),
                          ConfiguredModel.builder().rotationY(270).modelFile(normal).build()
        );

        builder.addModels(builder.partialState()
                                  .with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                  .with(BlockStateProperties.LIT, Boolean.TRUE),
                          ConfiguredModel.builder().rotationY(270).modelFile(on).build()
        );

        this.item(block);
    }

    private void buildCubeBottomTopModel(Block block)
    {
        final var blockName = block.getRegistryName().getPath();
        final var model = this.models().cubeBottomTop(blockName, this.modLoc("block/" + blockName + "_side"), this.modLoc("block/" + blockName + "_bottom"), this.modLoc("block/" + blockName + "_top"));
        this.simpleBlock(block, model);
        this.item(block);
    }

    private void item(Block block)
    {
        this.itemModels().withExistingParent(Objects.requireNonNull(block.getRegistryName()).getPath(), this.modLoc("block/" + block.getRegistryName().getPath()));
    }
}
