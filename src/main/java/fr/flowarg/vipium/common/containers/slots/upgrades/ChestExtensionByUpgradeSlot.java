package fr.flowarg.vipium.common.containers.slots.upgrades;

import fr.flowarg.vipium.common.items.UpgradeItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ChestExtensionByUpgradeSlot extends AbstractByUpgradeSlot
{
    ChestExtensionByUpgradeSlot(Container container, IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(container, inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        for (Slot slot : this.container.inventorySlots)
        {
            boolean flag = false;
            if(slot instanceof UpgradeSlot)
            {
                if(slot.getHasStack() && slot.getStack().getItem() instanceof UpgradeItem)
                {
                    final UpgradeItem upgradeItem = (UpgradeItem)slot.getStack().getItem();
                    if(upgradeItem.getUpgradeType() == UpgradeType.CHEST_EXTENSION)
                        flag = true;
                }
            }

            if(flag)
                return true;
        }
        return false;
    }
}
