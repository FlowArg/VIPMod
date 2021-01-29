package fr.flowarg.vipium.common.capability.armorconfig;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerArmorConfigWrapper implements ICapabilitySerializable<INBT>
{
    private IArmorConfig holder;

    private final LazyOptional<IArmorConfig> lazyOptional = LazyOptional.of(() -> this.holder);

    public PlayerArmorConfigWrapper(IArmorConfig holder)
    {
        this.holder = holder;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public INBT serializeNBT()
    {
        return ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY.writeNBT(this.holder, null);
    }

    @Override
    public void deserializeNBT(INBT nbt)
    {
        ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY.readNBT(this.holder, null, nbt);
    }
}
