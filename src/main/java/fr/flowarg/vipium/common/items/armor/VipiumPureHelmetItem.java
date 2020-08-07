package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumPureHelmetItem extends ArmorBaseItem
{
    public VipiumPureHelmetItem()
    {
        super("vipium_pure_helmet", RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL,
              EquipmentSlotType.HEAD,
              Rarity.EPIC);
    }
}
