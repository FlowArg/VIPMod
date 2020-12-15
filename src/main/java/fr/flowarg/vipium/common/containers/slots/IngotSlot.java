package fr.flowarg.vipium.common.containers.slots;

import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class IngotSlot extends Slot
{
    public IngotSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() == RegistryHandler.VIPIUM_INGOT.get();
    }
}
