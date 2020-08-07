package fr.flowarg.vipium.common.items.armor;

import fr.flowarg.vipium.Main;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Rarity;

abstract class ArmorBaseItem extends ArmorItem
{
    public ArmorBaseItem(String name, IArmorMaterial materialIn, EquipmentSlotType slot, Rarity rarity)
    {
        super(materialIn,
                slot,
                new Properties()
                        .group(Main.ITEM_GROUP)
                        .rarity(rarity));
    }
}
