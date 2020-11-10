package fr.flowarg.vipium.common.blocks;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import fr.flowarg.vipium.common.tileentities.VipiumChestTileEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class VipiumChestBlock extends Block implements IWaterLoggable
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    private final Supplier<TileEntityType<VipiumChestTileEntity>> tileEntityTypeSupplier;

    public VipiumChestBlock()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).hardnessAndResistance(19f, 45f));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, Boolean.FALSE));
        this.tileEntityTypeSupplier = RegistryHandler.VIPIUM_CHEST_TILE_ENTITY;
    }

    private static boolean isBlocked(IWorld iWorld, BlockPos blockPos)
    {
        return isBelowSolidBlock(iWorld, blockPos) || isCatSittingOn(iWorld, blockPos);
    }

    private static boolean isBelowSolidBlock(IBlockReader iBlockReader, BlockPos worldIn)
    {
        BlockPos blockpos = worldIn.up();
        return iBlockReader.getBlockState(blockpos).isNormalCube(iBlockReader, blockpos);
    }

    private static boolean isCatSittingOn(IWorld iWorld, BlockPos blockPos)
    {
        List<CatEntity> list = iWorld.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB((double)blockPos.getX(), (double)(blockPos.getY() + 1), (double)blockPos.getZ(), (double)(blockPos.getX() + 1), (double)(blockPos.getY() + 2), (double)(blockPos.getZ() + 1)));
        if (!list.isEmpty())
        {
            for (CatEntity catentity : list)
            {
                if (catentity.isSitting())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static TileEntityMerger.ICallback<VipiumChestTileEntity, Float2FloatFunction> getLid(final IChestLid p_226917_0_)
    {
        return new TileEntityMerger.ICallback<VipiumChestTileEntity, Float2FloatFunction>()
        {
            @Override
            public Float2FloatFunction func_225539_a_(VipiumChestTileEntity p_225539_1_, VipiumChestTileEntity p_225539_2_)
            {
                return (p_226921_2_) -> Math.max(p_225539_1_.getLidAngle(p_226921_2_), p_225539_2_.getLidAngle(p_226921_2_));
            }

            @Override
            public Float2FloatFunction func_225538_a_(VipiumChestTileEntity p_225538_1_)
            {
                return p_225538_1_::getLidAngle;
            }

            @Override
            public Float2FloatFunction func_225537_b_()
            {
                return p_226917_0_::getLidAngle;
            }
        };
    }

    public static TileEntityMerger.Type getMergerType(BlockState blockState)
    {
        return TileEntityMerger.Type.SINGLE;
    }

    public static Direction getDirectionToAttached(BlockState state)
    {
        final Direction direction = state.get(FACING);
        return direction.rotateYCCW();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (stateIn.get(WATERLOGGED))
        {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        Direction direction = context.getPlacementHorizontalFacing().getOpposite();
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());

        return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public IFluidState getFluidState(BlockState state)
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof VipiumChestTileEntity)
        {
            if (stack.hasDisplayName())
                ((VipiumChestTileEntity)tileentity).setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof VipiumChestTileEntity)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (VipiumChestTileEntity)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
            if (inamedcontainerprovider != null)
                player.openContainer(inamedcontainerprovider);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos)
    {
        final TileEntity tileentity = world.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot)
    {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return false;
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new VipiumChestTileEntity();
    }

    public TileEntityMerger.ICallbackWrapper<? extends VipiumChestTileEntity> getWrapper(BlockState blockState, World world, BlockPos blockPos, boolean p_225536_4_)
    {
        BiPredicate<IWorld, BlockPos> biPredicate;
        if (p_225536_4_)
        {
            biPredicate = (p_226918_0_, p_226918_1_) -> false;
        }
        else
        {
            biPredicate = VipiumChestBlock::isBlocked;
        }

        return TileEntityMerger.func_226924_a_(this.tileEntityTypeSupplier.get(), VipiumChestBlock::getMergerType, VipiumChestBlock::getDirectionToAttached, FACING, blockState, world, blockPos, biPredicate);
    }
}
