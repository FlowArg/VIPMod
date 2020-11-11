package fr.flowarg.vipium.common.containers.slots.upgrades;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public abstract class AbstractByUpgradeSlot extends Slot
{
    protected final Container container;

    AbstractByUpgradeSlot(Container container, IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public abstract boolean isItemValid(ItemStack stack);
}
