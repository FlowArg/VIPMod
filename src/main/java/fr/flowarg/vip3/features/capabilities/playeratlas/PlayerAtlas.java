package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.AtlasData;
import fr.flowarg.vip3.features.altar.data.Serialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface PlayerAtlas
{
    AtlasData atlasData();
    boolean enabled();
    void setAtlasData(AtlasData data);
    void setEnabled(boolean enabled);

    AtlasData EMPTY = new AtlasData(Map.of(), "");

    static @NotNull CompoundTag serializeNBT(@NotNull PlayerAtlas holder)
    {
        final var tag = new CompoundTag();
        final var data = holder.atlasData();
        Serialization.serializeAtlas(data != null ? data : EMPTY, tag);
        tag.putBoolean("enabled", holder.enabled());
        return tag;
    }

    static void deserializeNBT(Tag nbt, @NotNull PlayerAtlas holder)
    {
        final var tag = (CompoundTag)nbt;
        holder.setAtlasData(Serialization.deserializeAtlas(tag));
        holder.setEnabled(tag.getBoolean("enabled"));
    }
}
