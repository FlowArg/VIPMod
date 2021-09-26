package fr.flowarg.vip3.features;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VCrushingRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<VCrushingRecipe>
{
    @Override
    public @NotNull VCrushingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe)
    {
        final var ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(serializedRecipe, "ingredient"));

        if (!serializedRecipe.has("result")) throw new JsonSyntaxException("Missing result, expected to find a string or object");

        final var itemName = GsonHelper.getAsString(serializedRecipe, "result");
        final var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

        if(item == null) throw new IllegalStateException("Item: " + itemName + " does not exist");

        final var result = new ItemStack(item);
        final var experience = GsonHelper.getAsFloat(serializedRecipe, "experience");
        final var crushingTime = GsonHelper.getAsInt(serializedRecipe, "crushingTime");

        return new VCrushingRecipe(recipeId, ingredient, result, experience, crushingTime);
    }

    @Nullable
    @Override
    public VCrushingRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer)
    {
        final var ingredient = Ingredient.fromNetwork(buffer);
        final var result = buffer.readItem();
        final var experience = buffer.readFloat();
        final var crushingTime = buffer.readVarInt();

        return new VCrushingRecipe(recipeId, ingredient, result, experience, crushingTime);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull VCrushingRecipe recipe)
    {
        recipe.ingredient.toNetwork(buffer);
        buffer.writeItem(recipe.getResultItem());
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCrushingTime());
    }
}
