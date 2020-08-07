package fr.flowarg.vipium.client.creativetabs;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BlocksGroup extends ItemGroup
{
    public BlocksGroup()
    {
        super("vipium.blocks");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(RegistryHandler.VIPIUM_ORE.get());
    }
}
