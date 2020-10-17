package fr.flowarg.vipium.common.containers;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class VipiumPurifierContainer extends Container
{
    private final IInventory purifierInventory;

    public VipiumPurifierContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer)
    {
        this(id, playerInventory);
    }

    protected VipiumPurifierContainer(int id, PlayerInventory playerInventory)
    {
        this(id, playerInventory, new Inventory(1));
    }

    public VipiumPurifierContainer(int id, PlayerInventory playerInventory, IInventory purifierInventory)
    {
        super(RegistryHandler.VIPIUM_PURIFIER_CONTAINER.get(), id);
        assertInventorySize(purifierInventory, 1);
        this.purifierInventory = purifierInventory;

        this.addSlot(new Slot(purifierInventory, 0, 80, 37));

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (int i = 0; i < 9; i++)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn)
    {
        return this.purifierInventory.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index)
    {
        ItemStack itemStack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack())
        {
            final ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();

            if (!this.mergeItemStack(itemStack1, 1, 37, true)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() ==  itemStack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, itemStack1);
        }

        return itemStack;
    }
}
