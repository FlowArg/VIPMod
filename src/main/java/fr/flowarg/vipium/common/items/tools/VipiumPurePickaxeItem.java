package fr.flowarg.vipium.common.items.tools;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;

public class VipiumPurePickaxeItem extends PickaxeItem
{
    public VipiumPurePickaxeItem()
    {
        super(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 3, 3, new Properties().group(Main.ITEM_GROUP).rarity(Rarity.EPIC));
    }
}
