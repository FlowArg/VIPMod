package fr.flowarg.vip3.features.capabilities.atlas;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerAtlasWrapper implements ICapabilitySerializable<Tag>
{
    private Atlas holder;
    private final LazyOptional<Atlas> lazyOptional = LazyOptional.of(() -> this.holder);

    public PlayerAtlasWrapper(Atlas holder)
    {
        this.holder = holder;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return AtlasCapability.ATLAS_CAPABILITY.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public Tag serializeNBT()
    {
        return Atlas.serializeNBT(this.holder);
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        Atlas.deserializeNBT(nbt, this.holder);
    }
}
