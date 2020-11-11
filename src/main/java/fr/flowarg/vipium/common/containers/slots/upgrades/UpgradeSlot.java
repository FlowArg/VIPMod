package fr.flowarg.vipium.common.containers.slots.upgrades;

import fr.flowarg.vipium.common.items.UpgradeItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class UpgradeSlot extends Slot
{
    public UpgradeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() instanceof UpgradeItem;
    }
}
