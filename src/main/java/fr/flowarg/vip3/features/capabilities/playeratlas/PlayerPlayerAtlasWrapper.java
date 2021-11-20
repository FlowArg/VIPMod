package fr.flowarg.vip3.features.capabilities.playeratlas;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPlayerAtlasWrapper implements ICapabilitySerializable<Tag>
{
    private PlayerAtlas holder;
    private final LazyOptional<PlayerAtlas> lazyOptional = LazyOptional.of(() -> this.holder);

    public PlayerPlayerAtlasWrapper(PlayerAtlas holder)
    {
        this.holder = holder;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return PlayerAtlasCapability.PLAYER_ATLAS_CAPABILITY.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public Tag serializeNBT()
    {
        return PlayerAtlas.serializeNBT(this.holder);
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        PlayerAtlas.deserializeNBT(nbt, this.holder);
    }
}
