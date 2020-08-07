package fr.flowarg.vipium.common.items.tools;

import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;

public class VipiumSwordItem extends SwordItem
{
    public VipiumSwordItem()
    {
        super(RegistryHandler.VIPIUM_TOOL_MATERIAL, 5, 2, new Properties().rarity(Rarity.RARE).group(Main.ITEM_GROUP));
    }
}
