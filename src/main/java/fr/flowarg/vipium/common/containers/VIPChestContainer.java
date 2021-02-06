package fr.flowarg.vipium.common.containers;

import fr.flowarg.vipium.common.containers.slots.upgrades.UpgradeSlot;
import fr.flowarg.vipium.common.containers.slots.upgrades.UpgradeType;
import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class VIPChestContainer extends Container
{
    private final IInventory chestInventory;
    private final int numRows;

    public VIPChestContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer)
    {
        this(id, playerInventory);
    }

    public VIPChestContainer(int id, PlayerInventory playerInventory)
    {
        this(id, playerInventory, new Inventory(80), 6);
    }

    public VIPChestContainer(int id, PlayerInventory playerInventoryIn, IInventory inv, int rows)
    {
        super(RegistryHandler.VIP_CHEST_CONTAINER.get(), id);
        assertInventorySize(inv, (rows * 13) + 2);
        this.chestInventory = inv;
        this.numRows = rows;
        this.chestInventory.openInventory(playerInventoryIn.player);
        int i = (this.numRows - 4) * 18;
        int index = 0;

        for (int rowsIndex = 0; rowsIndex < this.numRows; ++rowsIndex)
        {
            for (int columnsIndex = 0; columnsIndex < 13; ++columnsIndex)
            {
                index = Math.max(index, columnsIndex + rowsIndex * 13);
                if(columnsIndex == 12)
                    this.addSlot(UpgradeType.newSlot(UpgradeType.CHEST_PURIFIER, this, inv, columnsIndex + rowsIndex * 13, 8 + columnsIndex * 18, 18 + rowsIndex * 18));
                else if(columnsIndex > 8)
                    this.addSlot(UpgradeType.newSlot(UpgradeType.CHEST_EXTENSION, this, inv, columnsIndex + rowsIndex * 13, 8 + columnsIndex * 18, 18 + rowsIndex * 18));
                else this.addSlot(new Slot(inv, columnsIndex + rowsIndex * 13, 8 + columnsIndex * 18, 18 + rowsIndex * 18));
            }
        }

        for (int rowsIndex = 0; rowsIndex < 3; ++rowsIndex)
        {
            for (int columnsIndex = 0; columnsIndex < 9; ++columnsIndex)
            {
                index = Math.max(index, columnsIndex + rowsIndex * 9 + 9);
                this.addSlot(new Slot(playerInventoryIn, columnsIndex + rowsIndex * 9 + 9, 8 + columnsIndex * 18, 104 + rowsIndex * 18 + i));
            }
        }

        for (int rowsIndex = 0; rowsIndex < 9; ++rowsIndex)
            this.addSlot(new Slot(playerInventoryIn, rowsIndex, 8 + rowsIndex * 18, 161 + i));

        this.addSlot(new UpgradeSlot(inv, ++index, 188, 158));
        this.addSlot(new UpgradeSlot(inv, ++index, 225, 158));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            final ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if (index < (this.numRows * 13) + 2)
            {
                if (!this.mergeItemStack(itemStack1, (this.numRows * 13) + 2, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if (!this.mergeItemStack(itemStack1, 0, (this.numRows * 13) + 2, false))
                return ItemStack.EMPTY;

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();
        }

        return itemStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return this.chestInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.chestInventory.closeInventory(playerIn);
    }
}
