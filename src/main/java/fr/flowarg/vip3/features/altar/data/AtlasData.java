package fr.flowarg.vip3.features.altar.data;

import java.util.Map;

public record AtlasData(Map<String, AltarData> altars, String playerOwnerUUID) {}
