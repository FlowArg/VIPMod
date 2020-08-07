package fr.flowarg.vipium.common.items.tools;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Rarity;

public class VipiumPureHoeItem extends HoeItem
{
    public VipiumPureHoeItem()
    {
        super(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 3, new Properties().group(Main.ITEM_GROUP).rarity(Rarity.EPIC));
    }
}
