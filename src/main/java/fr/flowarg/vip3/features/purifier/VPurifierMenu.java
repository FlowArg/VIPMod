package fr.flowarg.vip3.features.purifier;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class VPurifierMenu extends AbstractContainerMenu
{
    private static final int SLOT_COUNT = 2;
    private static final int DATA_COUNT = 4;

    private final Container container;
    private final RecipeType<VPurifyingRecipe> recipeType;
    private final ContainerData data;
    private final Level level;

    public VPurifierMenu(int id, Inventory inventory, FriendlyByteBuf ignored)
    {
        this(id, inventory);
    }

    public VPurifierMenu(int id, Inventory inventory)
    {
        this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public VPurifierMenu(int id, @NotNull Inventory inventory, Container container, ContainerData containerData)
    {
        super(VObjects.VIPIUM_PURIFIER_MENU.get(), id);

        this.container = container;
        this.recipeType = VObjects.PURIFYING_RECIPE;
        this.data = containerData;
        this.level = inventory.player.level;

        checkContainerSize(this.container, SLOT_COUNT);
        checkContainerDataCount(this.data, DATA_COUNT);

        this.addSlot(new VPurifierInputSlot(container, VPurifierEntity.SLOT_INPUT, 44, 32));
        this.addSlot(new VPurifierResultSlot(inventory.player, container, VPurifierEntity.SLOT_OUTPUT, 116, 32));

        for(int i = 0; i < 3; ++i)
            for(int j = 0; j < 9; ++j)
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for(int k = 0; k < 9; ++k)
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));

        this.addDataSlots(this.data);
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        return this.container.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex)
    {
        var empty = ItemStack.EMPTY;
        final var slot = this.slots.get(slotIndex);
        if (!slot.hasItem())
            return empty;

        final var slotItem = slot.getItem();
        empty = slotItem.copy();
        if (slotIndex == 1)
        {
            if (!this.moveItemStackTo(slotItem, 3, 38, true))
                return ItemStack.EMPTY;

            slot.onQuickCraft(slotItem, empty);
        }
        else if (slotIndex != 0)
        {
            if (this.canPurify(slotItem))
            {
                if (!this.moveItemStackTo(slotItem, 0, 1, false))
                    return ItemStack.EMPTY;
            }
            else if (slotIndex >= 2 && slotIndex < 29)
            {
                if (!this.moveItemStackTo(slotItem, 29, 38, false))
                    return ItemStack.EMPTY;
            }
            else if (slotIndex >= 29 && slotIndex < 38 && !this.moveItemStackTo(slotItem, 2, 29, false))
                return ItemStack.EMPTY;
        }
        else if (!this.moveItemStackTo(slotItem, 2, 38, false))
            return ItemStack.EMPTY;

        if (slotItem.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        if (slotItem.getCount() == empty.getCount()) return ItemStack.EMPTY;

        slot.onTake(player, slotItem);

        return empty;
    }

    public int getPurifiedIngotCount()
    {
        return this.data.get(2);
    }

    public int getFragmentsResultCount()
    {
        return this.data.get(3);
    }

    public float getLuck()
    {
        if(this.getPurifiedIngotCount() == 0 || this.getFragmentsResultCount() == 0) return 0;

        // possibilities : 1, 2, 3, 4
        // mid-value of the series is 2.5

        float x = this.getFragmentsResultCount();
        for(int i = 0; i < this.getPurifiedIngotCount(); i++)
            // we remove the mid-value of the series
            x -= 2.5;
        //          3 because (1, 2, 3, 4) = 4 ; 4 - 1 = 3
        //                                           and then we made some weird things to get the good percent.
        return x / (3 * this.getPurifiedIngotCount()) * 100 + 50F;
    }

    public float getAverage()
    {
        if(this.getPurifiedIngotCount() == 0 || this.getFragmentsResultCount() == 0) return 0;
        return (float)this.getFragmentsResultCount() / this.getPurifiedIngotCount();
    }

    public int getPurificationProgress()
    {
        final var purificationProgress = this.data.get(0);
        final var totalTimes = this.data.get(1);
        return totalTimes != 0 && purificationProgress != 0 ? purificationProgress * 50 / totalTimes : 0;
    }

    public ItemStack getItem(int slot)
    {
        return this.container.getItem(slot);
    }

    private boolean canPurify(ItemStack stack)
    {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new SimpleContainer(stack), this.level).isPresent();
    }
}
