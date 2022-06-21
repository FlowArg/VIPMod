package fr.flowarg.vip3.utils.extensions;

import fr.flowarg.vip3.utils.CalledAtRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@CalledAtRuntime
public interface IBlock {

	@CalledAtRuntime
	default boolean isSignalSourceTo(Level level, BlockPos pos, BlockState state, Direction dir) {
		return false;
	}

	@CalledAtRuntime
	default boolean isDirectSignalSourceTo(Level level, BlockPos pos, BlockState state, Direction dir) {
		return false;
	}
}
