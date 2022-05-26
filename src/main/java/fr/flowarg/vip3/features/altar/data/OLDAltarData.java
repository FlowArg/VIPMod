package fr.flowarg.vip3.features.altar.data;

import java.util.Map;

@Deprecated
public record OLDAltarData(Map<Integer, String> atlas, String playerOwnerUUID, Map<String, OLDAltarPermission> permissions, String uniqueToken)
{

}
