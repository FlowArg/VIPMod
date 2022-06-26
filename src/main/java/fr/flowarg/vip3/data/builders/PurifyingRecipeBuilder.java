package fr.flowarg.vip3.data.builders;

import com.google.gson.JsonObject;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.features.purifier.VPurifyingRecipeSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PurifyingRecipeBuilder implements RecipeBuilder
{
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int times;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final VPurifyingRecipeSerializer serializer;

    private PurifyingRecipeBuilder(Ingredient ingredient, ItemLike result, float experience, int times)
    {
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.experience = experience;
        this.times = times;

        this.serializer = VObjects.PURIFYING_RECIPE_SERIALIZER.get();
    }

    public static PurifyingRecipeBuilder purifying(Ingredient ingredient, ItemLike result, float experience, int times)
    {
        return new PurifyingRecipeBuilder(ingredient, result, experience, times);
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTriggerInstance)
    {
        this.advancement.addCriterion(criterionName, criterionTriggerInstance);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String group)
    {
        return this;
    }

    @Override
    public @NotNull Item getResult()
    {
        return this.result;
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation location)
    {
        this.ensureValid(location);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(location, this.ingredient, this.result, this.experience, this.times, this.advancement, new ResourceLocation(location.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + location.getPath()), this.serializer));
    }

    private void ensureValid(ResourceLocation location)
    {
        if (this.advancement.getCriteria().isEmpty()) throw new IllegalStateException("No way of obtaining recipe " + location);
    }

    private record Result(ResourceLocation id, Ingredient ingredient, Item result, float experience, int times, Advancement.Builder advancement, ResourceLocation advancementId, VPurifyingRecipeSerializer serializer) implements FinishedRecipe
    {
        @Override
        public void serializeRecipeData(@NotNull JsonObject jsonObject)
        {
            jsonObject.add("ingredient", this.ingredient.toJson());
            jsonObject.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            jsonObject.addProperty("experience", this.experience);
            jsonObject.addProperty("times", this.times);
        }

        @Override
        public @NotNull ResourceLocation getId()
        {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType()
        {
            return this.serializer;
        }

        @Override
        public @NotNull JsonObject serializeAdvancement()
        {
            return this.advancement.serializeToJson();
        }

        @Override
        public ResourceLocation getAdvancementId()
        {
            return this.advancementId;
        }
    }
}
