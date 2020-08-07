package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumPureLeggingsItem extends ArmorBaseItem
{
    public VipiumPureLeggingsItem()
    {
        super("vipium_pure_leggings", RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL,
              EquipmentSlotType.LEGS,
              Rarity.EPIC);
    }
}
