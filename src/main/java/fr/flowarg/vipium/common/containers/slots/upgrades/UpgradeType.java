package fr.flowarg.vipium.common.containers.slots.upgrades;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public enum UpgradeType
{
    CHEST_PURIFIER,
    CHEST_EXTENSION;

    public static AbstractByUpgradeSlot newSlot(UpgradeType type, Container container, IInventory inv, int index, int xPosition, int yPosition)
    {
        switch (type)
        {
            case CHEST_PURIFIER:
                return new ChestPurifierByUpgradeSlot(container, inv, index, xPosition, yPosition);
            case CHEST_EXTENSION:
                return new ChestExtensionByUpgradeSlot(container, inv, index, xPosition, yPosition);
            default:
                return new AbstractByUpgradeSlot(container, inv, index, xPosition, yPosition) {
                    @Override
                    public boolean isItemValid(ItemStack stack)
                    {
                        return true;
                    }
                };
        }
    }
}
