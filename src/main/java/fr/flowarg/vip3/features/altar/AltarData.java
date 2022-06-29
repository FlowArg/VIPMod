package fr.flowarg.vip3.features.altar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AltarData extends SavedData
{
    private List<Altar> altars;

    public List<Altar> altars()
    {
        return this.altars;
    }

    public void altars(List<Altar> altars)
    {
        this.altars = altars;
        this.setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag pCompoundTag)
    {
        return AltarSerialization.serializeNBT(this);
    }

    public static @NotNull AltarData create()
    {
        final var data = new AltarData();
        data.altars(new ArrayList<>());
        return data;
    }

    public static @NotNull AltarData load(CompoundTag tag)
    {
        final var data = create();
        AltarSerialization.deserializeNBT(tag, data);
        return data;
    }

    public static @NotNull AltarData getOrCreate(@NotNull ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(AltarData::load, AltarData::create, "altar");
    }
}


