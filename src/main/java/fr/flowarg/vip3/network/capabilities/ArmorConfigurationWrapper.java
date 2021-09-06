package fr.flowarg.vip3.network.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorConfigurationWrapper implements ArmorConfiguration
{
    private final LazyOptional<ArmorConfiguration> lazyOptional = LazyOptional.of(() -> this);

    private boolean helmetEffect;
    private boolean chestPlateEffect;
    private boolean leggingsEffect;
    private boolean bootsEffect;
    private boolean fullSet1Effect;
    private boolean fullSet2Effect;

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
    {
        return CapabilitiesEventHandler.ARMOR_CONFIGURATION_CAPABILITY.orEmpty(cap, this.lazyOptional);
    }

    @Override
    public Tag serializeNBT()
    {
        final var tag = new CompoundTag();
        tag.putBoolean("Helmet", this.helmetEffect());
        tag.putBoolean("ChestPlate", this.chestPlateEffect());
        tag.putBoolean("Leggings", this.leggingsEffect());
        tag.putBoolean("Boots", this.bootsEffect());
        tag.putBoolean("Set1", this.fullSet1Effect());
        tag.putBoolean("Set2", this.fullSet2Effect());
        return tag;
    }

    @Override
    public void deserializeNBT(Tag nbt)
    {
        final CompoundTag tag = (CompoundTag)nbt;
        this.helmetEffect(tag.getBoolean("Helmet"));
        this.chestPlateEffect(tag.getBoolean("ChestPlate"));
        this.leggingsEffect(tag.getBoolean("Leggings"));
        this.bootsEffect(tag.getBoolean("Boots"));
        this.fullSet1Effect(tag.getBoolean("Set1"));
        this.fullSet2Effect(tag.getBoolean("Set2"));
    }

    @Override
    public boolean helmetEffect()
    {
        return this.helmetEffect;
    }

    @Override
    public boolean chestPlateEffect()
    {
        return this.chestPlateEffect;
    }

    @Override
    public boolean leggingsEffect()
    {
        return this.leggingsEffect;
    }

    @Override
    public boolean bootsEffect()
    {
        return this.bootsEffect;
    }

    @Override
    public boolean fullSet1Effect()
    {
        return this.fullSet1Effect;
    }

    @Override
    public boolean fullSet2Effect()
    {
        return this.fullSet2Effect;
    }

    @Override
    public void helmetEffect(boolean val)
    {
        this.helmetEffect = val;
    }

    @Override
    public void chestPlateEffect(boolean val)
    {
        this.chestPlateEffect = val;
    }

    @Override
    public void leggingsEffect(boolean val)
    {
        this.leggingsEffect = val;
    }

    @Override
    public void bootsEffect(boolean val)
    {
        this.bootsEffect = val;
    }

    @Override
    public void fullSet1Effect(boolean val)
    {
        this.fullSet1Effect = val;
    }

    @Override
    public void fullSet2Effect(boolean val)
    {
        this.fullSet2Effect = val;
    }

    @Override
    public void notifyChange() {}
}
