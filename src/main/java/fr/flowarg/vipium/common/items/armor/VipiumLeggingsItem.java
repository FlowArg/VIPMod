package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumLeggingsItem extends ArmorBaseItem
{
    public VipiumLeggingsItem()
    {
        super("vipium_leggings", RegistryHandler.VIPIUM_ARMOR_MATERIAL,
              EquipmentSlotType.LEGS,
              Rarity.RARE);
    }
}
