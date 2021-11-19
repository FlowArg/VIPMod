package fr.flowarg.vip3.features.crusher;

import fr.flowarg.vip3.features.VObjects;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class VCrusherEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, RecipeHolder
{
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_LOCKED = 1;
    public static final int SLOT_OUTPUT = 2;

    private static final int[] SLOTS_FOR_UP = {SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = {SLOT_OUTPUT};
    private static final int[] SLOTS_FOR_SIDES = {SLOT_INPUT, SLOT_OUTPUT};

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

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

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeType<VCrushingRecipe> recipeType;
    private final SecureRandom random = new SecureRandom();
    private LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    public VCrusherEntity(BlockPos p_155229_, BlockState p_155230_)
    {
        super(VObjects.VIPIUM_CRUSHER_ENTITY.get(), p_155229_, p_155230_);
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
    public @NotNull CompoundTag save(@NotNull CompoundTag compound)
    {
        super.save(compound);
        compound.putInt("CrushTime", this.crushingProgress);
        compound.putInt("CrushTimeTotal", this.crushingTotalTime);
        compound.putInt("Started", this.started);
        compound.putInt("CrushedIngots", this.crushedIngots);
        compound.putInt("FragmentsResult", this.fragmentsResult);

        ContainerHelper.saveAllItems(compound, this.items);

        final var recipesUsed = new CompoundTag();
        this.recipesUsed.forEach((id, count) -> recipesUsed.putInt(id.toString(), count));

        compound.put("RecipesUsed", recipesUsed);
        return compound;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side)
    {
        return side == Direction.DOWN ? SLOTS_FOR_DOWN : side == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction)
    {
        return index != SLOT_LOCKED;
    }

    @Override
    protected @NotNull Component getDefaultName()
    {
        return new TranslatableComponent("container.vipium_crusher");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory)
    {
        return new VCrusherMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    public int getContainerSize()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int index)
    {
        return this.items.get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count)
    {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index)
    {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack)
    {
        assert this.level != null;

        final var currentStack = this.items.get(slot);
        final var flag = !stack.isEmpty() && stack.sameItem(currentStack) && ItemStack.tagMatches(stack, currentStack);

        this.items.set(slot, stack);

        if (stack.getCount() > this.getMaxStackSize())
            stack.setCount(this.getMaxStackSize());

        if (slot == SLOT_LOCKED && !flag)
        {
            this.crushingTotalTime = getTotalCrushTime(this.level, this);
            this.crushingProgress = 0;
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player)
    {
        assert this.level != null;

        if (this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        else return player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack)
    {
        return index == SLOT_INPUT;
    }

    @Override
    public void clearContent()
    {
        this.items.clear();
    }

    @Override
    public void fillStackedContents(@NotNull StackedContents contents)
    {
        this.items.forEach(contents::accountStack);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe)
    {
        if(recipe != null)
            this.recipesUsed.addTo(recipe.getId(), 1);
    }

    @Override
    public void awardUsedRecipes(@NotNull Player player) {}

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed()
    {
        return null;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction facing)
    {
        if (!this.remove && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (facing == Direction.UP) return this.handlers[0].cast();
            else if (facing == Direction.DOWN) return this.handlers[1].cast();
            else return this.handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps()
    {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) handler.invalidate();
    }

    @Override
    public void reviveCaps()
    {
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
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

    private boolean canCrush(VCrushingRecipe recipe, @NotNull NonNullList<ItemStack> items)
    {
        if(items.get(SLOT_LOCKED).isEmpty() || recipe == null) return false;

        final var recipeResult = recipe.assemble(this);

        if(recipeResult.isEmpty()) return false;

        final var currentOutputItem = items.get(SLOT_OUTPUT);

        if(currentOutputItem.isEmpty()) return true;

        if(!currentOutputItem.sameItem(recipeResult)) return false;

        final var count = recipeResult.getCount() + currentOutputItem.getCount();

        if(count <= this.getMaxStackSize() && count <= currentOutputItem.getMaxStackSize()) return true;

        return count <= recipeResult.getMaxStackSize();
    }

    private boolean crush(VCrushingRecipe recipe, NonNullList<ItemStack> items)
    {
        if(recipe == null || !this.canCrush(recipe, items)) return false;

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

    public void awardUsedRecipesAndPopExperience(@NotNull ServerPlayer player)
    {
        player.awardRecipes(this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position()));
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 position)
    {
        final var list = new ArrayList<Recipe<?>>();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet())
        {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                createExperience(level, position, entry.getIntValue(), ((VCrushingRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel level, Vec3 position, int recipeUsed, float experience)
    {
        var i = Mth.floor((float)recipeUsed * experience);
        final var f = Mth.frac((float)recipeUsed * experience);

        if (f != 0.0F && Math.random() < (double)f)
            ++i;

        ExperienceOrb.award(level, position, i);
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

                if (entity.canCrush(recipe, entity.items))
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
            level.setBlock(pos, state, Constants.BlockFlags.DEFAULT);
        }

        if(changed)
            setChanged(level, pos, state);
    }
}
