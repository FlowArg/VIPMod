package fr.flowarg.vip3.features.altar.data;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class OLDSerialization
{
    public static void serializeAltar(@NotNull OLDAltarData data, @NotNull CompoundTag tag)
    {
        final var atlasTag = new CompoundTag();
        data.atlas().forEach((index, playerAtlasOwnerUUID) -> atlasTag.putInt(playerAtlasOwnerUUID, index));
        tag.put("atlas", atlasTag);
        tag.putString("playerOwnerUUID", data.playerOwnerUUID());

        final var permissionsTag = new CompoundTag();
        data.permissions().forEach((playerOwnerUUID, altarPermission) -> {
            final var permissionTag = new CompoundTag();
            permissionTag.putBoolean("canTeleport", altarPermission.canTeleport());
            permissionTag.putBoolean("canManagePermissions", altarPermission.canManagePermissions());
            permissionsTag.put(playerOwnerUUID, permissionTag);
        });
        tag.put("permissions", permissionsTag);
        tag.putString("uniqueToken", data.uniqueToken());
    }

    @Contract("_ -> new")
    public static @NotNull OLDAltarData deserializeAltar(@NotNull CompoundTag tag)
    {
        final Map<Integer, String> atlas = new HashMap<>();

        tag.getCompound("atlas").getAllKeys().forEach(s -> atlas.put(tag.getInt(s), s));

        final String playerOwnerUUID = tag.getString("playerOwnerUUID");
        final Map<String, OLDAltarPermission> permissions = new HashMap<>();

        tag.getCompound("permissions").getAllKeys().forEach(s -> {
            final var permissionTag = tag.getCompound(s);
            permissions.put(s, new OLDAltarPermission(permissionTag.getBoolean("canTeleport"), permissionTag.getBoolean("canManagePermissions")));
        });

        final String uniqueToken = tag.getString("uniqueToken");

        return new OLDAltarData(atlas, playerOwnerUUID, permissions, uniqueToken);
    }

    public static void serializeAtlas(@NotNull OLDAtlasData data, @NotNull CompoundTag tag)
    {
        final var altarsTag = new CompoundTag();
        data.altars().forEach((s, altarData) -> {
            final var altarTag = new CompoundTag();
            serializeAltar(altarData, altarTag);
            altarsTag.put(s, altarTag);
        });
        tag.put("altars", altarsTag);
        tag.putString("playerOwnerUUID", data.playerOwnerUUID());
    }

    @Contract("_ -> new")
    public static @NotNull OLDAtlasData deserializeAtlas(@NotNull CompoundTag tag)
    {
        final Map<String, OLDAltarData> altars = new HashMap<>();

        tag.getCompound("altars").getAllKeys().forEach(s -> altars.put(s, deserializeAltar(tag.getCompound(s))));

        final String playerOwnerUUID = tag.getString("playerOwnerUUID");

        return new OLDAtlasData(altars, playerOwnerUUID);
    }
}
