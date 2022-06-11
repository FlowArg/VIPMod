package fr.flowarg.vip3.features.altar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltarSerialization
{
    public static @NotNull CompoundTag serializeNBT(@NotNull AltarData holder)
    {
        return serializeNBT(holder.altars());
    }

    public static @NotNull CompoundTag serializeNBT(@NotNull List<Altar> altarsHolder)
    {
        final var tag = new CompoundTag();
        final var altars = new ListTag();

        for (final var altar : altarsHolder)
        {
            final var altarTag = new CompoundTag();
            altarTag.putString("id", altar.getId());
            altarTag.putString("owner", altar.getOwner());
            altarTag.putString("name", altar.getName());

            final var posTag = new CompoundTag();
            posTag.putDouble("x", altar.getPos().x());
            posTag.putDouble("y", altar.getPos().y());
            posTag.putDouble("z", altar.getPos().z());

            altarTag.put("pos", posTag);

            final var connectedAtlases = altar.getConnectedAtlases();
            final var connectedAtlasesTag = new ListTag();
            connectedAtlases.values().forEach(connectedAtlas -> {
                final var connectedAtlasTag = new CompoundTag();
                connectedAtlasTag.putString("id", connectedAtlas.id());

                final var permissionsTag = new CompoundTag();
                permissionsTag.putBoolean("canTeleport", connectedAtlas.permissions().canTeleport());
                permissionsTag.putBoolean("canManagePermissions", connectedAtlas.permissions().canManagePermissions());
                permissionsTag.putBoolean("canAddAtlases", connectedAtlas.permissions().canAddAtlases());

                connectedAtlasTag.put("permissions", permissionsTag);
                connectedAtlasesTag.add(connectedAtlasTag);
            });

            altarTag.put("connectedAtlases", connectedAtlasesTag);
            altarTag.putBoolean("canLink", altar.canLink());

            altars.add(altarTag);
        }

        tag.put("altars", altars);
        return tag;
    }

    public static void deserializeNBT(Tag nbt, @NotNull AltarData holder)
    {
        holder.altars(deserializeNBT(nbt));
    }

    public static List<Altar> deserializeNBT(Tag nbt)
    {
        return ((CompoundTag)nbt).getList("altars", 10).stream().map(tag -> {
            final var altarTag = (CompoundTag)tag;
            final var id = altarTag.getString("id");
            final var owner = altarTag.getString("owner");
            final var name = altarTag.getString("name");
            final var posTag = altarTag.getCompound("pos");
            final var pos = new AltarPos(posTag.getDouble("x"), posTag.getDouble("y"), posTag.getDouble("z"));
            final var connectedAtlasesTag = altarTag.getList("connectedAtlases", 10);
            final Map<String, ConnectedAtlas> connectedAtlases = new HashMap<>();

            connectedAtlasesTag.forEach(connectedAtlas -> {
                final var connectedAtlasTag = (CompoundTag)connectedAtlas;
                final var id2 = connectedAtlasTag.getString("id");

                final var permissionsTag = connectedAtlasTag.getCompound("permissions");
                final var permissions = new AltarPermissions(permissionsTag.getBoolean("canTeleport"), permissionsTag.getBoolean("canManagePermissions"), permissionsTag.getBoolean("canAddAtlases"));

                connectedAtlases.put(id2, new ConnectedAtlas(id2, permissions));
            });

            final var canLink = altarTag.getBoolean("canLink");

            return new Altar(id, owner, pos, name, connectedAtlases, canLink);
        }).toList();
    }
}
