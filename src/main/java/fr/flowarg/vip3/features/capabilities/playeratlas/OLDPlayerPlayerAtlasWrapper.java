package fr.flowarg.vip3.features.capabilities.playeratlas;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class OLDPlayerPlayerAtlasWrapper implements ICapabilitySerializable<Tag>
{
    private OLDPlayerAtlas holder;
    private final LazyOptional<OLDPlayerAtlas> lazyOptional = LazyOptional.of(() -> this.holder);

    public OLDPlayerPlayerAtlasWrapper(OLDPlayerAtlas holder)
    {
        this.holder = holder;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return OLDPlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public Tag serializeNBT()
    {
        return OLDPlayerAtlas.serializeNBT(this.holder);
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        OLDPlayerAtlas.deserializeNBT(nbt, this.holder);
    }
}
