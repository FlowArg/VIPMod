package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumChestplateItem extends ArmorBaseItem
{
    public VipiumChestplateItem()
    {
        super("vipium_chestplate", RegistryHandler.VIPIUM_ARMOR_MATERIAL,
              EquipmentSlotType.CHEST,
              Rarity.RARE);
    }
}
