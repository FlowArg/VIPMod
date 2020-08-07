package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumHelmetItem extends ArmorBaseItem
{
    public VipiumHelmetItem()
    {
        super("vipium_helmet", RegistryHandler.VIPIUM_ARMOR_MATERIAL,
              EquipmentSlotType.HEAD,
              Rarity.RARE);
    }
}
