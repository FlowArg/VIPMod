package fr.flowarg.vipium.common.blocks;

import fr.flowarg.vipium.common.core.RegistryHandler;
import fr.flowarg.vipium.common.tileentities.VIPChestTileEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityMerger.ICallback;
import net.minecraft.tileentity.TileEntityMerger.ICallbackWrapper;
import net.minecraft.tileentity.TileEntityMerger.Type;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class VIPChestBlock extends AbstractChestBlock<VIPChestTileEntity> implements IWaterLoggable
{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D);
    protected static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D);
    protected static final VoxelShape SHAPE_WEST = Block.makeCuboidShape(0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    protected static final VoxelShape SHAPE_EAST = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D);
    protected static final VoxelShape field_196315_B = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    private static final ICallback<VIPChestTileEntity, Optional<IInventory>> field_220109_i = new ICallback<VIPChestTileEntity, Optional<IInventory>>()
    {
        @Override
        public Optional<IInventory> func_225539_a_(VIPChestTileEntity p_225539_1_, VIPChestTileEntity p_225539_2_)
        {
            return Optional.of(new DoubleSidedInventory(p_225539_1_, p_225539_2_));
        }

        @Override
        public Optional<IInventory> func_225538_a_(VIPChestTileEntity p_225538_1_)
        {
            return Optional.of(p_225538_1_);
        }

        @Override
        public Optional<IInventory> func_225537_b_()
        {
            return Optional.empty();
        }
    };
    private static final ICallback<VIPChestTileEntity, Optional<INamedContainerProvider>> field_220110_j = new ICallback<VIPChestTileEntity, Optional<INamedContainerProvider>>()
    {
        @Override
        public Optional<INamedContainerProvider> func_225539_a_(final VIPChestTileEntity p_225539_1_, final VIPChestTileEntity p_225539_2_)
        {
            final IInventory iinventory = new DoubleSidedInventory(p_225539_1_, p_225539_2_);
            return Optional.of(new INamedContainerProvider()
            {
                @Nullable
                @Override
                public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_)
                {
                    if (p_225539_1_.canOpen(p_createMenu_3_) && p_225539_2_.canOpen(p_createMenu_3_))
                    {
                        p_225539_1_.fillWithLoot(p_createMenu_2_.player);
                        p_225539_2_.fillWithLoot(p_createMenu_2_.player);
                        return ChestContainer.createGeneric9X6(p_createMenu_1_, p_createMenu_2_, iinventory);
                    }
                    else
                    {
                        return null;
                    }
                }

                @Override
                public ITextComponent getDisplayName()
                {
                    if (p_225539_1_.hasCustomName()) return p_225539_1_.getDisplayName();
                    else return p_225539_2_.hasCustomName() ? p_225539_2_.getDisplayName() : new TranslationTextComponent("container.chestDouble");
                }
            });
        }

        @Override
        public Optional<INamedContainerProvider> func_225538_a_(VIPChestTileEntity p_225538_1_)
        {
            return Optional.of(p_225538_1_);
        }

        @Override
        public Optional<INamedContainerProvider> func_225537_b_()
        {
            return Optional.empty();
        }
    };

    public VIPChestBlock()
    {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).hardnessAndResistance(19f, 45f), RegistryHandler.VIP_CHEST_TILE_ENTITY::get);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(TYPE, ChestType.SINGLE).with(WATERLOGGED, Boolean.FALSE));
    }

    public static Type func_226919_h_(BlockState p_226919_0_)
    {
        final ChestType chesttype = p_226919_0_.get(TYPE);
        if (chesttype == ChestType.SINGLE) return Type.SINGLE;
        else return chesttype == ChestType.RIGHT ? Type.FIRST : Type.SECOND;
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

        if (facingState.getBlock() == this && facing.getAxis().isHorizontal())
        {
            final ChestType chesttype = facingState.get(TYPE);
            if (stateIn.get(TYPE) == ChestType.SINGLE && chesttype != ChestType.SINGLE && stateIn.get(FACING) == facingState.get(FACING) && getDirectionToAttached(facingState) == facing.getOpposite())
            {
                return stateIn.with(TYPE, chesttype.opposite());
            }
        }
        else if (getDirectionToAttached(stateIn) == facing)
        {
            return stateIn.with(TYPE, ChestType.SINGLE);
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (state.get(TYPE) == ChestType.SINGLE)
        {
            return field_196315_B;
        }
        else
        {
            switch (getDirectionToAttached(state))
            {
                case NORTH:
                default:
                    return SHAPE_NORTH;
                case SOUTH:
                    return SHAPE_SOUTH;
                case WEST:
                    return SHAPE_WEST;
                case EAST:
                    return SHAPE_EAST;
            }
        }
    }
    
    public static Direction getDirectionToAttached(BlockState state)
    {
        final Direction direction = state.get(FACING);
        return state.get(TYPE) == ChestType.LEFT ? direction.rotateY() : direction.rotateYCCW();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        ChestType chesttype = ChestType.SINGLE;
        Direction direction = context.getPlacementHorizontalFacing().getOpposite();
        final IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        boolean flag = context.func_225518_g_(); Direction direction1 = context.getFace();
        if (direction1.getAxis().isHorizontal() && flag)
        {
            final Direction direction2 = this.getDirectionToAttach(context, direction1.getOpposite());
            if (direction2 != null && direction2.getAxis() != direction1.getAxis())
            {
                direction = direction2;
                chesttype = direction2.rotateYCCW() == direction1.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        if (chesttype == ChestType.SINGLE && !flag)
        {
            if (direction == this.getDirectionToAttach(context, direction.rotateY()))
            {
                chesttype = ChestType.LEFT;
            }
            else if (direction == this.getDirectionToAttach(context, direction.rotateYCCW()))
            {
                chesttype = ChestType.RIGHT;
            }
        }

        return this.getDefaultState().with(FACING, direction).with(TYPE, chesttype).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
    }

    @Override
    public IFluidState getFluidState(BlockState state)
    {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }
    
    @Nullable
    private Direction getDirectionToAttach(BlockItemUseContext p_196312_1_, Direction p_196312_2_)
    {
        final BlockState blockstate = p_196312_1_.getWorld().getBlockState(p_196312_1_.getPos().offset(p_196312_2_));
        return blockstate.getBlock() == this && blockstate.get(TYPE) == ChestType.SINGLE ? blockstate.get(FACING) : null;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof VIPChestTileEntity)
            {
                ((VIPChestTileEntity)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof IInventory)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
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
            final INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
            if (inamedcontainerprovider != null)
            {
                player.openContainer(inamedcontainerprovider);
                player.addStat(this.getOpenStat());
            }
        }
        return ActionResultType.SUCCESS;
    }

    private Stat<ResourceLocation> getOpenStat()
    {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }

    @Nullable
    public static IInventory func_226916_a_(VIPChestBlock p_226916_0_, BlockState p_226916_1_, World p_226916_2_, BlockPos p_226916_3_, boolean p_226916_4_)
    {
        return p_226916_0_.combine(p_226916_1_, p_226916_2_, p_226916_3_, p_226916_4_).apply(field_220109_i).orElse(null);
    }

    @Override
    public ICallbackWrapper<? extends VIPChestTileEntity> combine(BlockState state, World worldIn, BlockPos pos, boolean p_225536_4_)
    {
        final BiPredicate<IWorld, BlockPos> bipredicate;
        if (p_225536_4_) bipredicate = (p_226918_0_, p_226918_1_) -> false;
        else bipredicate = VIPChestBlock::isBlocked;
        return TileEntityMerger.func_226924_a_(this.tileEntityType.get(), VIPChestBlock::func_226919_h_, VIPChestBlock::getDirectionToAttached, FACING, state, worldIn, pos, bipredicate);
    }

    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        return this.combine(state, worldIn, pos, false).apply(field_220110_j).orElse(null);
    }

    @OnlyIn(Dist.CLIENT)
    public static ICallback<VIPChestTileEntity, Float2FloatFunction> getLid(final IChestLid p_226917_0_)
    {
        return new ICallback<VIPChestTileEntity, Float2FloatFunction>()
        {
            @Override
            public Float2FloatFunction func_225539_a_(VIPChestTileEntity p_225539_1_, VIPChestTileEntity p_225539_2_)
            {
                return (p_226921_2_) -> Math.max(p_225539_1_.getLidAngle(p_226921_2_), p_225539_2_.getLidAngle(p_226921_2_));
            }

            @Override
            public Float2FloatFunction func_225538_a_(VIPChestTileEntity p_225538_1_)
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

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new VIPChestTileEntity();
    }

    public static boolean isBlocked(IWorld p_220108_0_, BlockPos p_220108_1_)
    {
        return isBelowSolidBlock(p_220108_0_, p_220108_1_) || isCatSittingOn(p_220108_0_, p_220108_1_);
    }

    private static boolean isBelowSolidBlock(IBlockReader p_176456_0_, BlockPos worldIn)
    {
        final BlockPos blockpos = worldIn.up();
        return p_176456_0_.getBlockState(blockpos).isNormalCube(p_176456_0_, blockpos);
    }

    private static boolean isCatSittingOn(IWorld p_220107_0_, BlockPos p_220107_1_)
    {
        List<CatEntity> list = p_220107_0_.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB(p_220107_1_.getX(), p_220107_1_.getY() + 1, p_220107_1_.getZ(), p_220107_1_.getX() + 1, p_220107_1_.getY() + 2, p_220107_1_.getZ() + 1));
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

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory(func_226916_a_(this, blockState, worldIn, pos, false));
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
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return false;
    }
}
