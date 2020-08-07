package fr.flowarg.vipium.common.items.tools;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Rarity;

public class VipiumHoeItem extends HoeItem
{
    public VipiumHoeItem()
    {
        super(RegistryHandler.VIPIUM_TOOL_MATERIAL, 1, new Properties().group(Main.ITEM_GROUP).rarity(Rarity.RARE));
    }
}
