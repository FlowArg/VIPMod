package fr.flowarg.vip3.features;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class VSharedMachine<R extends Recipe<Container>> extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, RecipeHolder
{
    protected NonNullList<ItemStack> items;
    protected LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    protected final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    protected final Component name;

    protected VSharedMachine(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, NonNullList<ItemStack> items, Component name)
    {
        super(type, worldPosition, blockState);
        this.items = items;
        this.name = name;
    }

    protected boolean canExecute(R recipe, @NotNull NonNullList<ItemStack> items, int slotIn, int slotOut)
    {
        if(items.get(slotIn).isEmpty() || recipe == null) return false;

        final var recipeResult = recipe.assemble(this);

        if(recipeResult.isEmpty()) return false;

        final var currentOutputItem = items.get(slotOut);

        if(currentOutputItem.isEmpty()) return true;

        if(!currentOutputItem.sameItem(recipeResult)) return false;

        final var count = recipeResult.getCount() + currentOutputItem.getCount();

        if(count <= this.getMaxStackSize() && count <= currentOutputItem.getMaxStackSize()) return true;

        return count <= recipeResult.getMaxStackSize();
    }

    public void awardUsedRecipesAndPopExperience(@NotNull ServerPlayer player)
    {
        player.awardRecipes(this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position()));
        this.recipesUsed.clear();
    }

    public abstract List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 position);

    protected static void createExperience(ServerLevel level, Vec3 position, int recipeUsed, float experience)
    {
        var i = Mth.floor((float)recipeUsed * experience);
        final var f = Mth.frac((float)recipeUsed * experience);

        if (f != 0.0F && Math.random() < (double)f)
            ++i;

        ExperienceOrb.award(level, position, i);
    }

    @Override
    protected @NotNull Component getDefaultName()
    {
        return this.name;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction)
    {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack)
    {
        this.items.set(index, stack);

        if (stack.getCount() > this.getMaxStackSize())
            stack.setCount(this.getMaxStackSize());
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
    public boolean stillValid(@NotNull Player player)
    {
        assert this.level != null;

        if (this.level.getBlockEntity(this.worldPosition) != this)
            return false;
        else return player.distanceToSqr(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
}
