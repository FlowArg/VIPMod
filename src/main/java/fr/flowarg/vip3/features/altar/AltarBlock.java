package fr.flowarg.vip3.features.altar;

import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.VSendAltarPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class AltarBlock extends Block
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AltarBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult)
    {
        if (!level.isClientSide)
        {
            final var altarData = AltarData.getOrCreate((ServerLevel)level);
            for (Altar altar : altarData.altars())
            {
                if(altar.getPos().x() == pos.getX() && altar.getPos().y() == pos.getY() && altar.getPos().z() == pos.getZ())
                {
                    if(altar.getOwner().equals(player.getStringUUID()) || altar.getConnectedAtlases().containsKey(player.getStringUUID()))
                        VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)player), new VSendAltarPacket(altar, true));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving)
    {
        super.onRemove(state, level, pos, newState, isMoving);
        final var altarData = AltarData.getOrCreate((ServerLevel)level);
        altarData.altars().removeIf(altar -> altar.getPos().x() == pos.getX() && altar.getPos().y() == pos.getY() && altar.getPos().z() == pos.getZ());
        altarData.setDirty();
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack)
    {
        if(level.isClientSide || placer == null)
            return;

        final var altarData = AltarData.getOrCreate((ServerLevel)level);
        final var altar = new Altar(UUID.randomUUID().toString(), placer.getStringUUID(), new AltarPos(pos.getX(), pos.getY(), pos.getZ()), "Altar #12", new HashMap<>(), true);
        altarData.altars().add(altar);
        altarData.setDirty();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> stateBuilder)
    {
        stateBuilder.add(FACING);
    }
}
