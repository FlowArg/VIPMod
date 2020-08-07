package fr.flowarg.vipium.common.items.tools;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;

public class VipiumShovelItem extends ShovelItem
{
    public VipiumShovelItem()
    {
        super(RegistryHandler.VIPIUM_TOOL_MATERIAL, 1, 1, new Properties().group(Main.ITEM_GROUP).rarity(Rarity.RARE));
    }
}
