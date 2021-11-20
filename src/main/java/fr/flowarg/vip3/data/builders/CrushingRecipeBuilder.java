package fr.flowarg.vip3.data.builders;

import com.google.gson.JsonObject;
import fr.flowarg.vip3.features.VObjects;
import fr.flowarg.vip3.features.crusher.VCrushingRecipeSerializer;
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

public class CrushingRecipeBuilder implements RecipeBuilder
{
    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int crushingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final VCrushingRecipeSerializer serializer;

    private CrushingRecipeBuilder(Ingredient ingredient, ItemLike result, float experience, int crushingTime)
    {
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.experience = experience;
        this.crushingTime = crushingTime;

        this.serializer = VObjects.CRUSHING_RECIPE_SERIALIZER.get();
    }

    public static CrushingRecipeBuilder crushing(Ingredient ingredient, ItemLike result, float experience, int crushingTime)
    {
        return new CrushingRecipeBuilder(ingredient, result, experience, crushingTime);
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
        consumer.accept(new Result(location, this.ingredient, this.result, this.experience, this.crushingTime, this.advancement, new ResourceLocation(location.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + location.getPath()), this.serializer));
    }

    private void ensureValid(ResourceLocation location)
    {
        if (this.advancement.getCriteria().isEmpty()) throw new IllegalStateException("No way of obtaining recipe " + location);
    }

    private record Result(ResourceLocation id, Ingredient ingredient, Item result, float experience, int crushingTime, Advancement.Builder advancement, ResourceLocation advancementId, VCrushingRecipeSerializer serializer) implements FinishedRecipe
    {
        @Override
        public void serializeRecipeData(@NotNull JsonObject jsonObject)
        {
            jsonObject.add("ingredient", this.ingredient.toJson());
            jsonObject.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            jsonObject.addProperty("experience", this.experience);
            jsonObject.addProperty("crushingTime", this.crushingTime);
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
