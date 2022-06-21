package fr.flowarg.vip3.utils.extensions;

import fr.flowarg.vip3.utils.CalledAtRuntime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

@CalledAtRuntime
public interface IBlockState {

	@CalledAtRuntime
	default boolean isSignalSourceTo(Level level, BlockPos pos, Direction dir) {
		return false;
	}

	@CalledAtRuntime
	default boolean isDirectSignalSourceTo(Level level, BlockPos pos, Direction dir) {
		return false;
	}
}
