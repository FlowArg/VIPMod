package fr.flowarg.vip3.features.crusher;

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

public class VCrusherEntity extends VSharedMachine<VCrushingRecipe>
{
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_LOCKED = 1;
    public static final int SLOT_OUTPUT = 2;

    private static final int[] SLOTS_FOR_UP = {SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = {SLOT_OUTPUT};
    private static final int[] SLOTS_FOR_SIDES = {SLOT_INPUT, SLOT_OUTPUT};

    private int crushingProgress;
    private int crushingTotalTime;
    private int started;
    private int crushedIngots;
    private int fragmentsResult;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index)
        {
            return switch (index) {
                case 0 -> VCrusherEntity.this.crushingProgress;
                case 1 -> VCrusherEntity.this.crushingTotalTime;
                case 2 -> VCrusherEntity.this.crushedIngots;
                case 3 -> VCrusherEntity.this.fragmentsResult;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int val)
        {
            switch (index)
            {
                case 0 -> VCrusherEntity.this.crushingProgress = val;
                case 1 -> VCrusherEntity.this.crushingTotalTime = val;
                case 2 -> VCrusherEntity.this.crushedIngots = val;
                case 3 -> VCrusherEntity.this.fragmentsResult = val;
            }
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };

    private final RecipeType<VCrushingRecipe> recipeType;
    private final SecureRandom random = new SecureRandom();

    public VCrusherEntity(BlockPos pos, BlockState state)
    {
        super(VObjects.VIPIUM_CRUSHER_ENTITY.get(), pos, state, NonNullList.withSize(3, ItemStack.EMPTY), new TranslatableComponent("container.vipium_crusher"));
        this.recipeType = VObjects.CRUSHING_RECIPE;
    }

    @Override
    public void load(@NotNull CompoundTag tag)
    {
        super.load(tag);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);

        this.crushingProgress = tag.getInt("CrushTime");
        this.crushingTotalTime = tag.getInt("CrushTimeTotal");
        this.started = tag.getInt("Started");
        this.crushedIngots = tag.getInt("CrushedIngots");
        this.fragmentsResult = tag.getInt("FragmentsResult");
        final var recipesUsed = tag.getCompound("RecipesUsed");

        recipesUsed.getAllKeys().forEach(s -> this.recipesUsed.put(new ResourceLocation(s), recipesUsed.getInt(s)));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound)
    {
        super.saveAdditional(compound);

        compound.putInt("CrushTime", this.crushingProgress);
        compound.putInt("CrushTimeTotal", this.crushingTotalTime);
        compound.putInt("Started", this.started);
        compound.putInt("CrushedIngots", this.crushedIngots);
        compound.putInt("FragmentsResult", this.fragmentsResult);

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
        return index != SLOT_LOCKED;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory)
    {
        return new VCrusherMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack)
    {
        final var currentStack = this.items.get(index);
        final var flag = !stack.isEmpty() && stack.sameItem(currentStack) && ItemStack.tagMatches(stack, currentStack);

        super.setItem(index, stack);

        if (index == SLOT_LOCKED && !flag)
        {
            assert this.level != null;
            this.crushingTotalTime = getTotalCrushTime(this.level, this);
            this.crushingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack)
    {
        return index == SLOT_INPUT;
    }

    public boolean isStarted()
    {
        return this.started == 1;
    }

    public void setStarted(boolean started)
    {
        this.started = started ? 1 : 0;
    }

    private boolean isLit()
    {
        return this.crushingProgress > 0;
    }

    public void setCrushedIngots(int crushedIngots)
    {
        this.crushedIngots = crushedIngots;
    }

    public void setFragmentsResult(int fragmentsResult)
    {
        this.fragmentsResult = fragmentsResult;
    }

    private boolean crush(VCrushingRecipe recipe, NonNullList<ItemStack> items)
    {
        if(recipe == null || !this.canExecute(recipe, items, SLOT_LOCKED, SLOT_OUTPUT)) return false;

        final var lockedStack = items.get(SLOT_LOCKED);
        final var recipeResult = recipe.assemble(this);
        final var currentOutputItem = items.get(SLOT_OUTPUT);

        final var newCount = this.random.nextInt(6) + 1;
        recipeResult.setCount(newCount);

        this.fragmentsResult += newCount;

        if(currentOutputItem.isEmpty()) items.set(SLOT_OUTPUT, recipeResult.copy());
        else if(currentOutputItem.is(recipeResult.getItem())) currentOutputItem.grow(recipeResult.getCount());

        lockedStack.shrink(1);

        this.crushedIngots++;

        return true;
    }

    private static int getTotalCrushTime(@NotNull Level level, VCrusherEntity container)
    {
        return level.getRecipeManager().getRecipeFor(container.recipeType, container, level).map(VCrushingRecipe::getCrushingTime).orElse(0);
    }

    static void serverTick(Level level, BlockPos pos, BlockState state, @NotNull VCrusherEntity entity)
    {
        final var wasLit = entity.isLit();
        final var wasStarted = entity.isStarted();
        var changed = false;

        if(entity.isStarted())
        {
            if(entity.items.get(SLOT_LOCKED).isEmpty())
            {
                changed = true;
                entity.crushingProgress = 0;
                entity.setStarted(false);
                entity.crushingTotalTime = 0;
                state = state.setValue(VCrusherBlock.LIT, Boolean.FALSE);
            }
            else
            {
                final var recipe = level.getRecipeManager().getRecipeFor(entity.recipeType, entity, level).orElse(null);

                if (entity.canExecute(recipe, entity.items, SLOT_LOCKED, SLOT_OUTPUT))
                {
                    ++entity.crushingProgress;
                    if (entity.crushingProgress == entity.crushingTotalTime)
                    {
                        entity.crushingProgress = 0;
                        entity.crushingTotalTime = getTotalCrushTime(level, entity);

                        if (entity.crush(recipe, entity.items)) entity.setRecipeUsed(recipe);

                        changed = true;
                    }
                }
                else
                {
                    entity.crushingProgress = 0;
                    entity.setStarted(false);
                }
            }
        }

        if (wasLit != entity.isLit() || wasStarted != entity.isStarted() || (state.getValue(VCrusherBlock.LIT) != entity.isStarted()))
        {
            changed = true;
            state = state.setValue(VCrusherBlock.LIT, entity.isLit() && entity.isStarted());
            level.setBlock(pos, state, 3);
        }

        if(changed)
            setChanged(level, pos, state);
    }
}
