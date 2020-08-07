package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumBootsItem extends ArmorBaseItem
{
    public VipiumBootsItem()
    {
        super("vipium_boots", RegistryHandler.VIPIUM_ARMOR_MATERIAL,
              EquipmentSlotType.FEET,
              Rarity.RARE);
    }
}
