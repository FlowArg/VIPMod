package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.AtlasData;
import fr.flowarg.vip3.features.altar.data.Serialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public interface PlayerAtlas
{
    AtlasData atlasData();
    void setAtlasData(AtlasData data);

    static @NotNull CompoundTag serializeNBT(@NotNull PlayerAtlas holder)
    {
        final var tag = new CompoundTag();
        Serialization.serializeAtlas(holder.atlasData(), tag);
        return tag;
    }

    static void deserializeNBT(Tag nbt, @NotNull PlayerAtlas holder)
    {
        final var tag = (CompoundTag)nbt;
        holder.setAtlasData(Serialization.deserializeAtlas(tag));
    }
}
