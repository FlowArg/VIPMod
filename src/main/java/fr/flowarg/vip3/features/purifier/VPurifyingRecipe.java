package fr.flowarg.vip3.features.purifier;

import fr.flowarg.vip3.features.ExperienceProvider;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class VPurifyingRecipe implements Recipe<Container>, ExperienceProvider
{
    private final RecipeType<VPurifyingRecipe> recipeType;
    private final RecipeSerializer<VPurifyingRecipe> recipeSerializer;
    private final ResourceLocation id;
    protected final Ingredient ingredient;
    private final ItemStack result;
    private final float experience;
    private final int times;

    public VPurifyingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, float experience, int times)
    {
        this.recipeType = VObjects.PURIFYING_RECIPE;
        this.recipeSerializer = VObjects.PURIFYING_RECIPE_SERIALIZER.get();

        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
        this.times = times;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level)
    {
        return this.ingredient.test(container.getItem(VPurifierEntity.SLOT_INPUT));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container)
    {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return true;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients()
    {
        return NonNullList.of(this.ingredient);
    }

    @Override
    public @NotNull ItemStack getResultItem()
    {
        return this.result;
    }

    public float getExperience()
    {
        return this.experience;
    }

    public int getTimes()
    {
        return this.times;
    }

    @Override
    public @NotNull ResourceLocation getId()
    {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer()
    {
        return this.recipeSerializer;
    }

    @Override
    public @NotNull RecipeType<?> getType()
    {
        return this.recipeType;
    }

    @Override
    public @NotNull ItemStack getToastSymbol()
    {
        return VObjects.VIPIUM_PURIFIER_ITEM.get().getDefaultInstance();
    }
}
