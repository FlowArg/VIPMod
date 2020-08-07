package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Rarity;

public class VipiumPureChestplateItem extends ArmorBaseItem
{
    public VipiumPureChestplateItem()
    {
        super("vipium_pure_chestplate", RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL,
              EquipmentSlotType.CHEST,
              Rarity.EPIC);
    }
}
