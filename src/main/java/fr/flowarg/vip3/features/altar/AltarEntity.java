package fr.flowarg.vip3.features.altar;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AltarEntity extends BlockEntity
{
    //private static final OLDAltarData EMPTY = new OLDAltarData(Map.of(), "", Map.of(), "");
    //private OLDAltarData data = EMPTY;

    public AltarEntity(BlockPos pWorldPosition, BlockState pBlockState)
    {
        super(VObjects.TELEPORTATION_ALTAR_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag tag)
    {
        super.load(tag);
        //this.data = OLDSerialization.deserializeAltar(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        //OLDSerialization.serializeAltar(this.data, tag);
    }

    static void serverTick(Level level, BlockPos pos, BlockState state, @NotNull AltarEntity entity)
    {

    }
}
