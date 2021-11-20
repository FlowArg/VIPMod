package fr.flowarg.vip3.features.altar.data;

import java.util.Map;

public record AltarData(Map<Integer, String> atlas, String playerOwnerUUID, Map<String, AltarPermission> permissions, String uniqueToken)
{

}
