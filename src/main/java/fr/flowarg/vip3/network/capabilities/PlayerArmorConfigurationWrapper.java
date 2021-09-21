package fr.flowarg.vip3.network.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerArmorConfigurationWrapper implements ICapabilitySerializable<Tag>
{
    private ArmorConfiguration holder;
    private final LazyOptional<ArmorConfiguration> lazyOptional = LazyOptional.of(() -> this.holder);

    public PlayerArmorConfigurationWrapper(ArmorConfiguration holder)
    {
        this.holder = holder;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public Tag serializeNBT()
    {
        return ArmorConfiguration.serializeNBT(this.holder);
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        ArmorConfiguration.deserializeNBT(nbt, this.holder);
    }
}
