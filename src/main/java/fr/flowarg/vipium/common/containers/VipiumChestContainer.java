package fr.flowarg.vipium.common.containers;

import fr.flowarg.vipium.common.containers.slots.UpgradeSlot;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class VipiumChestContainer extends Container
{
    private final IInventory chestInventory;
    private final int numRows;

    public VipiumChestContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer)
    {
        this(id, playerInventory);
    }

    public VipiumChestContainer(int id, PlayerInventory playerInventory)
    {
        this(id, playerInventory, new Inventory(78), 6);
    }

    public VipiumChestContainer(int id, PlayerInventory playerInventoryIn, IInventory inv, int rows)
    {
        super(RegistryHandler.VIPIUM_CHEST_CONTAINER.get(), id);
        assertInventorySize(inv, rows * 13);
        this.chestInventory = inv;
        this.numRows = rows;
        this.chestInventory.openInventory(playerInventoryIn.player);
        int i = (this.numRows - 4) * 18;
        int index = 0;

        for (int j = 0; j < this.numRows; ++j)
        {
            for (int k = 0; k < 13; ++k)
            {
                index = Math.max(index, k + j * 9);
                this.addSlot(new Slot(inv, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                index = Math.max(index, j1 + l * 9 + 9);
                this.addSlot(new Slot(playerInventoryIn, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
            this.addSlot(new Slot(playerInventoryIn, i1, 8 + i1 * 18, 161 + i));

        this.addSlot(new UpgradeSlot(playerInventoryIn, ++index, 187, 157));
        this.addSlot(new UpgradeSlot(playerInventoryIn, ++index, 224, 157));
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
            if (index < this.numRows * 13)
            {
                if (!this.mergeItemStack(itemStack1, this.numRows * 13, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if (!this.mergeItemStack(itemStack1, 0, this.numRows * 13, false))
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

    public int getNumRows()
    {
        return this.numRows;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
        this.chestInventory.closeInventory(playerIn);
    }
}
