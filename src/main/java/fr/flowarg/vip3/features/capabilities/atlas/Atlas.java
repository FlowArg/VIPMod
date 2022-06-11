package fr.flowarg.vip3.features.capabilities.atlas;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Atlas
{
    List<String> connectedAltars();
    void connectedAltars(List<String> connectedAltars);

    static @NotNull CompoundTag serializeNBT(@NotNull Atlas holder)
    {
        return serializeNBT(holder.connectedAltars());
    }

    static @NotNull CompoundTag serializeNBT(@NotNull List<String> connectedAltarsHolder)
    {
        final var tag = new CompoundTag();
        final var connectedAltars = new ListTag();

        for (final var connectedAtlas : connectedAltarsHolder)
            connectedAltars.add(StringTag.valueOf(connectedAtlas));

        tag.put("connectedAltars", connectedAltars);
        return tag;
    }

    static void deserializeNBT(Tag nbt, @NotNull Atlas holder)
    {
        holder.connectedAltars(deserializeNBT(nbt));
    }

    static List<String> deserializeNBT(Tag nbt)
    {
        final var tag = (CompoundTag)nbt;
        return tag.getList("connectedAltars", 8).stream().map(Tag::getAsString).toList();
    }
}
