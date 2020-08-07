package fr.flowarg.vipium.client.creativetabs;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemsGroup extends ItemGroup
{
    public ItemsGroup()
    {
        super("vipium.items");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(RegistryHandler.VIPIUM_INGOT.get());
    }
}
