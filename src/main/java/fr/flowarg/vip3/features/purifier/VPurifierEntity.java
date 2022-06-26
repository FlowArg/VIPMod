package fr.flowarg.vip3.features.purifier;

import fr.flowarg.vip3.features.VObjects;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class VPurifierEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, RecipeHolder
{
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;

    private static final int[] SLOTS_FOR_UP = {SLOT_INPUT};
    private static final int[] SLOTS_FOR_DOWN = {SLOT_OUTPUT};
    private static final int[] SLOTS_FOR_SIDES = {SLOT_INPUT, SLOT_OUTPUT};

    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

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

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeType<VPurifyingRecipe> recipeType;
    private final SecureRandom random = new SecureRandom();
    private final Component name = new TranslatableComponent("container.vipium_purifier");
    private LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    public VPurifierEntity(BlockPos worldPosition, BlockState blockState)
    {
        super(VObjects.VIPIUM_PURIFIER_ENTITY.get(), worldPosition, blockState);
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
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction)
    {
        return true;
    }

    @Override
    protected @NotNull Component getDefaultName()
    {
        return this.name;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory)
    {
        return new VPurifierMenu(containerId, inventory, this, this.dataAccess);
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
    public void setItem(int index, @NotNull ItemStack stack)
    {
        assert this.level != null;

        final var currentStack = this.items.get(index);
        final var flag = !stack.isEmpty() && stack.sameItem(currentStack) && ItemStack.tagMatches(stack, currentStack);

        this.items.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
            stack.setCount(this.getMaxStackSize());

        if (index == SLOT_INPUT && !flag)
        {
            this.totalTimes = getTotalTimes(this.level, this);
            this.purificationProgress = 0;
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
    public void fillStackedContents(@NotNull StackedContents helper)
    {
        this.items.forEach(helper::accountStack);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe)
    {
        if(recipe != null)
            this.recipesUsed.addTo(recipe.getId(), 1);
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed()
    {
        return null;
    }

    @Override
    public void awardUsedRecipes(@NotNull Player player) {}

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

    private boolean canPurify(VPurifyingRecipe recipe, @NotNull NonNullList<ItemStack> items)
    {
        if(items.get(SLOT_INPUT).isEmpty() || recipe == null) return false;

        final var recipeResult = recipe.assemble(this);

        if(recipeResult.isEmpty()) return false;

        final var currentOutputItem = items.get(SLOT_OUTPUT);

        if(currentOutputItem.isEmpty()) return true;

        if(!currentOutputItem.sameItem(recipeResult)) return false;

        final var count = recipeResult.getCount() + currentOutputItem.getCount();

        if(count <= this.getMaxStackSize() && count <= currentOutputItem.getMaxStackSize()) return true;

        return count <= recipeResult.getMaxStackSize();
    }

    private boolean purify(VPurifyingRecipe recipe, NonNullList<ItemStack> items)
    {
        if(recipe == null || !this.canPurify(recipe, items)) return false;

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

    public void awardUsedRecipesAndPopExperience(@NotNull ServerPlayer player)
    {
        player.awardRecipes(this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position()));
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 position)
    {
        final List<Recipe<?>> list = new ArrayList<>();

        for(var entry : this.recipesUsed.object2IntEntrySet())
        {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                createExperience(level, position, entry.getIntValue(), ((VPurifyingRecipe)recipe).getExperience());
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

            if (entity.canPurify(recipe, entity.items))
            {
                if (entity.purificationProgress == entity.totalTimes)
                {
                    entity.purificationProgress = 0;
                    entity.totalTimes = getTotalTimes(level, entity);

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
