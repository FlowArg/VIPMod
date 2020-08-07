package fr.flowarg.vipium.common.items.tools;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

import java.util.Set;

public class VipiumPureMultiToolItem extends ToolItem
{
    public VipiumPureMultiToolItem()
    {
        super(3, 3, RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, Sets.newHashSet(), new Properties().group(Main.ITEM_GROUP).rarity(Rarity.RARE));
    }

    @Override
    public boolean canHarvestBlock(BlockState state)
    {
        return true;
    }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack)
    {
        return ImmutableSet.of(ToolType.PICKAXE, ToolType.SHOVEL, ToolType.AXE);
    }
}
