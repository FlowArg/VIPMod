package fr.flowarg.vip3.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class VPurifier extends BaseEntityBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public VPurifier(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.FALSE));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult)
    {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        else
        {
            this.openContainer(level, pos, player);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity entity, ItemStack stack)
    {
        if (!stack.hasCustomHoverName()) return;

        final var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractFurnaceBlockEntity)
            ((AbstractFurnaceBlockEntity)blockEntity).setCustomName(stack.getHoverName());
    }

    @Override
    public void onRemove(BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean unused)
    {
        if (oldState.is(newState.getBlock())) return;

        final var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbstractFurnaceBlockEntity)
        {
            if (level instanceof ServerLevel)
            {
                Containers.dropContents(level, pos, (AbstractFurnaceBlockEntity)blockEntity);
                ((AbstractFurnaceBlockEntity)blockEntity).getRecipesToAwardAndPopExperience((ServerLevel)level, Vec3.atCenterOf(pos));
            }

            level.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(oldState, level, pos, newState, unused);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState p_48700_)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState p_48702_, Level p_48703_, @NotNull BlockPos p_48704_)
    {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(p_48703_.getBlockEntity(p_48704_));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_48727_)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(FACING, LIT);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level, BlockEntityType<T> entityType, BlockEntityType<? extends AbstractFurnaceBlockEntity> registeredEntityType)
    {
        return level.isClientSide ? null : createTickerHelper(entityType, registeredEntityType, AbstractFurnaceBlockEntity::serverTick);
    }

    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state)
    {
        return new FurnaceBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType)
    {
        return createFurnaceTicker(level, entityType, BlockEntityType.FURNACE);
    }

    protected void openContainer(Level level, BlockPos pos, Player player)
    {
        final var blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof FurnaceBlockEntity)) return;

        player.openMenu((MenuProvider)blockEntity);
        player.awardStat(Stats.INTERACT_WITH_FURNACE);
    }

    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Random random)
    {
        if (!state.getValue(LIT)) return;

        final var d0 = pos.getX() + 0.5D;
        final var d1 = pos.getY();
        final var d2 = (double)pos.getZ() + 0.5D;

        if (random.nextDouble() < 0.1D)
            level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);

        final var direction = state.getValue(FACING);
        final var axis = direction.getAxis();
        final var d4 = random.nextDouble() * 0.6D - 0.3D;
        final var d5 = axis == Direction.Axis.X ? direction.getStepX() * 0.52D : d4;
        final var d6 = random.nextDouble() * 6.0D / 16.0D;
        final var d7 = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52D : d4;

        level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
    }
}
