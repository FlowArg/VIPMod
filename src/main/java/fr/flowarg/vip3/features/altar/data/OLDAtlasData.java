package fr.flowarg.vip3.features.altar.data;

import java.util.Map;

@Deprecated
public record OLDAtlasData(Map<String, OLDAltarData> altars, String playerOwnerUUID) {}
