package fr.flowarg.vipium.common.blocks;

import fr.flowarg.vipium.common.tileentities.VipiumPurifierTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class VipiumPurifierBlock extends Block
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public VipiumPurifierBlock()
    {
        super(Properties.create(Material.IRON).hardnessAndResistance(32f, 29f).harvestTool(ToolType.PICKAXE));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        return state.get(LIT) ? super.getLightValue(state, world, pos) : 0;
    }

    @Nonnull
    @Override
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
            this.interactWith(worldIn, pos, player);
        return ActionResultType.SUCCESS;
    }

    public void interactWith(World worldIn, BlockPos pos, PlayerEntity player)
    {
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof VipiumPurifierTileEntity)
            player.openContainer((INamedContainerProvider)tileentity);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            final TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof VipiumPurifierTileEntity)
                ((VipiumPurifierTileEntity) te).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof VipiumPurifierTileEntity)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (VipiumPurifierTileEntity) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation rot)
    {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Nonnull
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, LIT);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new VipiumPurifierTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand)
    {
        if (stateIn.get(LIT))
        {
            final double baseX = pos.getX() + 0.5D;
            final double baseY = pos.getY();
            final double baseZ = pos.getZ() + 0.5D;

            final double randomNumber = rand.nextDouble() * 0.6D - 0.3D;
            final Direction direction = stateIn.get(FACING);
            final Direction.Axis axis = direction.getAxis();

            final double xAdded = axis == Direction.Axis.X ? direction.getXOffset() * 0.5D : randomNumber;
            final double yAdded = rand.nextDouble() * 6.0D / 16.0D;
            final double zAdded = axis == Direction.Axis.Z ? direction.getZOffset() * 0.52D : randomNumber;
            worldIn.addParticle(new RedstoneParticleData(0.0f, 1.0f, 0.0f, 1.0f), baseX + xAdded, baseY + yAdded, baseZ + zAdded, 1.0D, 1.0D, 1.0D);
        }
    }
}
