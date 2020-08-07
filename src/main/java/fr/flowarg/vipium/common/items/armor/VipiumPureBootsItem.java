package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumPureBootsItem extends ArmorBaseItem
{
    public VipiumPureBootsItem()
    {
        super("vipium_pure_boots", RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL,
              EquipmentSlotType.FEET,
              Rarity.EPIC);
    }
}
