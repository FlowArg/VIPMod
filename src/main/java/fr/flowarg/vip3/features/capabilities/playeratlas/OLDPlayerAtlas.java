package fr.flowarg.vip3.features.capabilities.playeratlas;

import fr.flowarg.vip3.features.altar.data.OLDAtlasData;
import fr.flowarg.vip3.features.altar.data.OLDSerialization;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Deprecated
public interface OLDPlayerAtlas
{
    OLDAtlasData atlasData();
    boolean enabled();
    void setAtlasData(OLDAtlasData data);
    void setEnabled(boolean enabled);

    OLDAtlasData EMPTY = new OLDAtlasData(Map.of(), "");

    static @NotNull CompoundTag serializeNBT(@NotNull OLDPlayerAtlas holder)
    {
        final var tag = new CompoundTag();
        final var data = holder.atlasData();
        OLDSerialization.serializeAtlas(data != null ? data : EMPTY, tag);
        tag.putBoolean("enabled", holder.enabled());
        return tag;
    }

    static void deserializeNBT(Tag nbt, @NotNull OLDPlayerAtlas holder)
    {
        final var tag = (CompoundTag)nbt;
        holder.setAtlasData(OLDSerialization.deserializeAtlas(tag));
        holder.setEnabled(tag.getBoolean("enabled"));
    }
}
