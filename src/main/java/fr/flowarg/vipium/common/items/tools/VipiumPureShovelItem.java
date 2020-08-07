package fr.flowarg.vipium.common.items.tools;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;

public class VipiumPureShovelItem extends ShovelItem
{
    public VipiumPureShovelItem()
    {
        super(RegistryHandler.VIPIUM_TOOL_MATERIAL, 3, 3, new Properties().group(Main.ITEM_GROUP).rarity(Rarity.EPIC));
    }
}
