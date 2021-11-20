package fr.flowarg.vip3.features.altar;

import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.features.altar.data.AltarData;
import fr.flowarg.vip3.features.altar.data.Serialization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AltarEntity extends BlockEntity
{
    private AltarData data;

    public AltarEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(VObjects.TELEPORTATION_ALTAR_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag tag)
    {
        super.load(tag);
        this.data = Serialization.deserializeAltar(tag);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag)
    {
        super.save(tag);
        Serialization.serializeAltar(this.data, tag);
        return tag;
    }

    static void serverTick(Level level, BlockPos pos, BlockState state, @NotNull AltarEntity entity)
    {

    }
}
