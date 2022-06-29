package fr.flowarg.vip3.features.purifier;

import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.features.VSharedMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

public class VPurifierEntity extends VSharedMachine<VPurifyingRecipe>
{
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;

    private static final int[] SLOTS_FOR_UP = {SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = {SLOT_OUTPUT};
    private static final int[] SLOTS_FOR_SIDES = {SLOT_INPUT, SLOT_OUTPUT};

    private int purificationProgress;
    private int totalTimes;
    private int purifiedVipium;
    private int vipiumResult;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index)
        {
            return switch (index) {
                case 0 -> VPurifierEntity.this.purificationProgress;
                case 1 -> VPurifierEntity.this.totalTimes;
                case 2 -> VPurifierEntity.this.purifiedVipium;
                case 3 -> VPurifierEntity.this.vipiumResult;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int val)
        {
            switch (index)
            {
                case 0 -> VPurifierEntity.this.purificationProgress = val;
                case 1 -> VPurifierEntity.this.totalTimes = val;
                case 2 -> VPurifierEntity.this.purifiedVipium = val;
                case 3 -> VPurifierEntity.this.vipiumResult = val;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };

    private final RecipeType<VPurifyingRecipe> recipeType;
    private final SecureRandom random = new SecureRandom();

    public VPurifierEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(VObjects.VIPIUM_PURIFIER_ENTITY.get(), worldPosition, blockState, NonNullList.withSize(2, ItemStack.EMPTY), new TranslatableComponent("container.vipium_purifier"));
        this.recipeType = VObjects.PURIFYING_RECIPE;
    }

    @Override
    public void load(@NotNull CompoundTag tag)
    {
        super.load(tag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);

        this.purificationProgress = tag.getInt("PurifyingProgress");
        this.totalTimes = tag.getInt("TotalTimes");
        this.purifiedVipium = tag.getInt("PurifiedVipium");
        this.vipiumResult = tag.getInt("VipiumResult");
        final var recipesUsed = tag.getCompound("RecipesUsed");

        recipesUsed.getAllKeys().forEach(s -> this.recipesUsed.put(new ResourceLocation(s), recipesUsed.getInt(s)));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound)
    {
        super.saveAdditional(compound);

        compound.putInt("PurifyingProgress", this.purificationProgress);
        compound.putInt("TotalTimes", this.totalTimes);
        compound.putInt("PurifiedVipium", this.purifiedVipium);
        compound.putInt("VipiumResult", this.vipiumResult);

        ContainerHelper.saveAllItems(compound, this.items);

        final var recipesUsed = new CompoundTag();
        this.recipesUsed.forEach((id, count) -> recipesUsed.putInt(id.toString(), count));

        compound.put("RecipesUsed", recipesUsed);
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side)
    {
        return side == Direction.DOWN ? SLOTS_FOR_DOWN : side == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction)
    {
        return true;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory)
    {
        return new VPurifierMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack)
    {
        final var currentStack = this.items.get(index);
        final var flag = !stack.isEmpty() && stack.sameItem(currentStack) && ItemStack.tagMatches(stack, currentStack);

        super.setItem(index, stack);

        if (index == SLOT_INPUT && !flag)
        {
            assert this.level != null;
            this.totalTimes = getTotalTimes(this.level, this);
            this.purificationProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack)
    {
        return index == SLOT_INPUT;
    }

    private static int getTotalTimes(@NotNull Level level, VPurifierEntity container)
    {
        return level.getRecipeManager().getRecipeFor(container.recipeType, container, level).map(VPurifyingRecipe::getTimes).orElse(0);
    }

    private boolean isLit()
    {
        return this.purificationProgress > 0;
    }

    public void setPurifiedVipium(int purifiedVipium)
    {
        this.purifiedVipium = purifiedVipium;
    }

    public void setVipiumResult(int fragmentsResult)
    {
        this.vipiumResult = fragmentsResult;
    }

    public void increaseProgress()
    {
        this.purificationProgress++;
    }

    private boolean purify(VPurifyingRecipe recipe, NonNullList<ItemStack> items)
    {
        if(recipe == null || !this.canExecute(recipe, items, SLOT_INPUT, SLOT_OUTPUT)) return false;

        final var inputStack = items.get(SLOT_INPUT);
        final var recipeResult = recipe.assemble(this);
        final var currentOutputItem = items.get(SLOT_OUTPUT);

        final var newCount = this.random.nextInt(4) + 1;

        recipeResult.setCount(newCount);

        this.vipiumResult += newCount;

        if(currentOutputItem.isEmpty()) items.set(SLOT_OUTPUT, recipeResult.copy());
        else if(currentOutputItem.is(recipeResult.getItem())) currentOutputItem.grow(recipeResult.getCount());

        inputStack.shrink(1);

        this.purifiedVipium++;

        return true;
    }

    static void serverTick(Level level, BlockPos pos, BlockState state, @NotNull VPurifierEntity entity)
    {
        var changed = false;

        if(entity.items.get(SLOT_INPUT).isEmpty())
        {
            changed = true;
            entity.purificationProgress = 0;
            entity.totalTimes = 0;
            state = state.setValue(VPurifierBlock.LIT, Boolean.FALSE);
        }
        else
        {
            final var recipe = level.getRecipeManager().getRecipeFor(entity.recipeType, entity, level).orElse(null);

            entity.totalTimes = getTotalTimes(level, entity);

            if (entity.canExecute(recipe, entity.items, SLOT_INPUT, SLOT_OUTPUT))
            {
                if (entity.purificationProgress == entity.totalTimes)
                {
                    entity.purificationProgress = 0;

                    if (entity.purify(recipe, entity.items)) entity.setRecipeUsed(recipe);

                    changed = true;
                }
            }
            else entity.purificationProgress = 0;
        }

        if (state.getValue(VPurifierBlock.LIT) != entity.isLit())
        {
            changed = true;
            state = state.setValue(VPurifierBlock.LIT, entity.isLit());
            level.setBlock(pos, state, 3);
        }

        if(changed)
            setChanged(level, pos, state);
    }
}
