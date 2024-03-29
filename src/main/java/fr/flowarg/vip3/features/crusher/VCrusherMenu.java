package fr.flowarg.vip3.features.crusher;

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

public class VCrusherMenu extends AbstractContainerMenu
{
    private static final int SLOT_COUNT = 3;
    private static final int DATA_COUNT = 4;

    private final Container container;
    private final RecipeType<VCrushingRecipe> recipeType;
    private final ContainerData data;
    private final Level level;

    public VCrusherMenu(int id, Inventory inventory, FriendlyByteBuf ignored)
    {
        this(id, inventory);
    }

    public VCrusherMenu(int id, Inventory inventory)
    {
        this(id, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public VCrusherMenu(int id, Inventory inventory, Container container, ContainerData containerData)
    {
        super(VObjects.VIPIUM_CRUSHER_MENU.get(), id);

        this.container = container;
        this.recipeType = VObjects.CRUSHING_RECIPE;
        this.data = containerData;
        this.level = inventory.player.level;

        checkContainerSize(this.container, SLOT_COUNT);
        checkContainerDataCount(this.data, DATA_COUNT);

        this.addSlot(new VCrusherInputSlot(container, VCrusherEntity.SLOT_INPUT, 45, 35));
        this.addSlot(new VCrusherLockedSlot(container, VCrusherEntity.SLOT_LOCKED, 67, 35));
        this.addSlot(new VCrusherResultSlot(inventory.player, container, VCrusherEntity.SLOT_OUTPUT, 125, 35));

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
        if (slot.hasItem())
        {
            final var slotItem = slot.getItem();
            empty = slotItem.copy();
            if (slotIndex == 2)
            {
                if (!this.moveItemStackTo(slotItem, 3, 39, true))
                    return ItemStack.EMPTY;

                slot.onQuickCraft(slotItem, empty);
            }
            else if (slotIndex != 1 && slotIndex != 0)
            {
                if (this.canCrush(slotItem))
                {
                    if (!this.moveItemStackTo(slotItem, 0, 1, false))
                        return ItemStack.EMPTY;
                }
                else if (slotIndex >= 3 && slotIndex < 30)
                {
                    if (!this.moveItemStackTo(slotItem, 30, 39, false))
                        return ItemStack.EMPTY;
                }
                else if (slotIndex >= 30 && slotIndex < 39 && !this.moveItemStackTo(slotItem, 3, 30, false))
                    return ItemStack.EMPTY;
            }
            else if (!this.moveItemStackTo(slotItem, 3, 39, false))
                return ItemStack.EMPTY;

            if (slotItem.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (slotItem.getCount() == empty.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, slotItem);
        }

        return empty;
    }

    public int getCrushedIngotCount()
    {
        return this.data.get(2);
    }

    public int getFragmentsResultCount()
    {
        return this.data.get(3);
    }

    public float getLuck()
    {
        if(this.getCrushedIngotCount() == 0 || this.getFragmentsResultCount() == 0) return 0;

        // possibilities : 1, 2, 3, 4, 5, 6
        // mid-value of the series : 3.5

        float x = this.getFragmentsResultCount();
        for(int i = 0; i < this.getCrushedIngotCount(); i++)
            // we remove the mid-value of the series
            x -= 3.5;
        //          5 because (1, 2, 3, 4, 5, 6) = 4 ; 6 - 1 = 5
        //                                           and then we made some weird things to get the good percent.
        return x / (5 * this.getCrushedIngotCount()) * 100 + 50F;
    }

    public float getAverage()
    {
        if(this.getCrushedIngotCount() == 0 || this.getFragmentsResultCount() == 0) return 0;
        return (float)this.getFragmentsResultCount() / this.getCrushedIngotCount();
    }

    public int getBurnProgress()
    {
        final var crushingProgress = this.data.get(0);
        final var crushingTotalTime = this.data.get(1);
        return crushingTotalTime != 0 && crushingProgress != 0 ? crushingProgress * 24 / crushingTotalTime : 0;
    }

    public ItemStack getItem(int slot)
    {
        return this.container.getItem(slot);
    }

    private boolean canCrush(ItemStack stack)
    {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new SimpleContainer(stack), this.level).isPresent();
    }
}
