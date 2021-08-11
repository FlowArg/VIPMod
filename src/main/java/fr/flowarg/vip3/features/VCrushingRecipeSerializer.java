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
    public @NotNull VCrushingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject jsonObject)
    {
        final var ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));

        if (!jsonObject.has("result")) throw new JsonSyntaxException("Missing result, expected to find a string or object");

        final var itemName = GsonHelper.getAsString(jsonObject, "result");
        final var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

        if(item == null) throw new IllegalStateException("Item: " + itemName + " does not exist");

        final var result = new ItemStack(item);
        final var experience = GsonHelper.getAsFloat(jsonObject, "experience");
        final var crushingTime = GsonHelper.getAsInt(jsonObject, "crushingTime");

        return new VCrushingRecipe(id, ingredient, result, experience, crushingTime);
    }

    @Nullable
    @Override
    public VCrushingRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf byteBuf)
    {
        final var ingredient = Ingredient.fromNetwork(byteBuf);
        final var result = byteBuf.readItem();
        final var experience = byteBuf.readFloat();
        final var crushingTime = byteBuf.readInt();

        return new VCrushingRecipe(id, ingredient, result, experience, crushingTime);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf byteBuf, @NotNull VCrushingRecipe recipe)
    {
        recipe.getIngredients().forEach(ingredient -> ingredient.toNetwork(byteBuf));
        byteBuf.writeItem(recipe.getResultItem());
        byteBuf.writeFloat(recipe.getExperience());
        byteBuf.writeInt(recipe.getCrushingTime());
    }
}
