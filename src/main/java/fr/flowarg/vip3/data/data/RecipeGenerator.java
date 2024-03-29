package fr.flowarg.vip3.data.data;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.data.builders.CrushingRecipeBuilder;
import fr.flowarg.vip3.data.builders.PurifyingRecipeBuilder;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.OreBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class RecipeGenerator extends RecipeProvider
{
    public RecipeGenerator(DataGenerator generator)
    {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer)
    {
        this.build(VObjects.VIPIUM_BOW.get(), builder -> builder.pattern("XYZ").pattern("YXZ").pattern("XYZ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).define('Z', Items.STRING).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_PICKAXE.get(), builder -> builder.pattern("XXX").pattern(" Y ").pattern(" Y ").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_AXE.get(), builder -> builder.pattern(" XX").pattern(" YX").pattern(" Y ").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_SHOVEL.get(), builder -> builder.pattern(" X ").pattern(" Y ").pattern(" Y ").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_HOE.get(), builder -> builder.pattern(" XX").pattern(" Y ").pattern(" Y ").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_MULTI_TOOL.get(), builder -> builder.pattern("XXX").pattern("XYX").pattern(" Y ").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_SWORD.get(), builder -> builder.pattern(" X ").pattern(" X ").pattern(" Y ").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_PICKAXE.get(), builder -> builder.pattern("XXX").pattern(" Y ").pattern(" Y ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_AXE.get(), builder -> builder.pattern(" XX").pattern(" YX").pattern(" Y ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_SHOVEL.get(), builder -> builder.pattern(" X ").pattern(" Y ").pattern(" Y ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_HOE.get(), builder -> builder.pattern(" XX").pattern(" Y ").pattern(" Y ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_MULTI_TOOL.get(), builder -> builder.pattern("XXX").pattern("XYX").pattern(" Y ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_SWORD.get(), builder -> builder.pattern(" X ").pattern(" X ").pattern(" Y ").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.STICK).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_INGOT.get(), builder -> builder.pattern("XXX").pattern("XXX").define('X', VObjects.VIPIUM_FRAGMENT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_FRAGMENT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_INGOT.get(), builder -> builder.pattern("XXX").pattern("XXX").define('X', VObjects.PURE_VIPIUM_FRAGMENT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_FRAGMENT.get())), consumer);
        this.build(VObjects.VIPIUM_HELMET.get(), builder -> builder.pattern("XXX").pattern("X X").define('X', VObjects.VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_CHESTPLATE.get(), builder -> builder.pattern("X X").pattern("XXX").pattern("XXX").define('X', VObjects.VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_LEGGINGS.get(), builder -> builder.pattern("XXX").pattern("X X").pattern("X X").define('X', VObjects.VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_BOOTS.get(), builder -> builder.pattern("X X").pattern("X X").define('X', VObjects.VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_HELMET.get(), builder -> builder.pattern("XXX").pattern("X X").define('X', VObjects.PURE_VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_CHESTPLATE.get(), builder -> builder.pattern("X X").pattern("XXX").pattern("XXX").define('X', VObjects.PURE_VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_LEGGINGS.get(), builder -> builder.pattern("XXX").pattern("X X").pattern("X X").define('X', VObjects.PURE_VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_BOOTS.get(), builder -> builder.pattern("X X").pattern("X X").define('X', VObjects.PURE_VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_APPLE.get(), builder -> builder.pattern("XXX").pattern("XYX").pattern("XXX").define('X', VObjects.VIPIUM_INGOT.get()).define('Y', Items.APPLE).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get(), Items.APPLE)), consumer);
        this.build(VObjects.PURE_VIPIUM_APPLE.get(), builder -> builder.pattern("XXX").pattern("XYX").pattern("XXX").define('X', VObjects.PURE_VIPIUM_INGOT.get()).define('Y', Items.APPLE).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get(), Items.APPLE)), consumer);
        this.build(VObjects.FRENCH_BAGUETTE.get(), builder -> builder.pattern(" X ").pattern("YXY").pattern(" X ").define('X', Items.WHEAT).define('Y', Items.PAPER).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PAPER, Items.WHEAT)), consumer);
        this.build(VObjects.VIPIUM_BLOCK_ITEM.get(), builder -> builder.pattern("XXX").pattern("XXX").pattern("XXX").define('X', VObjects.VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.PURE_VIPIUM_BLOCK_ITEM.get(), builder -> builder.pattern("XXX").pattern("XXX").pattern("XXX").define('X', VObjects.PURE_VIPIUM_INGOT.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        this.build(VObjects.VIPIUM_CRUSHER_ITEM.get(), builder -> builder.pattern("XXX").pattern("XYZ").pattern("ZZZ").define('X', Items.POLISHED_ANDESITE).define('Y', VObjects.VIPIUM_MULTI_TOOL.get()).define('Z', Items.POLISHED_DIORITE).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_MULTI_TOOL.get(), Items.POLISHED_ANDESITE, Items.POLISHED_DIORITE)), consumer);
        this.build(VObjects.VIPIUM_PURIFIER_ITEM.get(), builder -> builder.pattern("XXX").pattern("XYZ").pattern("ZZZ").define('X', Items.POLISHED_ANDESITE).define('Y', VObjects.VIPIUM_PICKAXE.get()).define('Z', Items.POLISHED_DIORITE).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_PICKAXE.get(), Items.POLISHED_ANDESITE, Items.POLISHED_DIORITE)), consumer);

        this.buildShapelessFromBlock(VObjects.VIPIUM_INGOT.get(), builder -> builder.requires(VObjects.VIPIUM_BLOCK_ITEM.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_BLOCK_ITEM.get())), consumer);
        this.buildShapelessFromBlock(VObjects.PURE_VIPIUM_INGOT.get(), builder -> builder.requires(VObjects.PURE_VIPIUM_BLOCK_ITEM.get()).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_BLOCK_ITEM.get())), consumer);

        this.buildOreCook(VObjects.VIPIUM_ORE.get(), VObjects.VIPIUM_FRAGMENT.get(), consumer);
        this.buildOreCook(VObjects.DEEPSLATE_VIPIUM_ORE.get(), VObjects.VIPIUM_FRAGMENT.get(), consumer);

        this.buildCrush(VObjects.VIPIUM_INGOT.get(), VObjects.VIPIUM_FRAGMENT.get(), 13f, 200, consumer);
        this.buildCrush(VObjects.PURE_VIPIUM_INGOT.get(), VObjects.PURE_VIPIUM_FRAGMENT.get(), 26f, 400, consumer);

        this.buildPurification(VObjects.VIPIUM_INGOT.get(), VObjects.PURE_VIPIUM_FRAGMENT.get(), 12f, 10, consumer);
    }

    private void buildPurification(Item ingredient, Item result, float experience, int times, Consumer<FinishedRecipe> consumer)
    {
        PurifyingRecipeBuilder.purifying(Ingredient.of(ingredient), result, experience, times)
                .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_PURIFIER_ITEM.get()))
                .save(consumer, new ResourceLocation(VIP3.MOD_ID, result.getRegistryName().getPath() + "_from_" + ingredient.getRegistryName().getPath() + "_purify"));
    }

    private void build(Item item, @NotNull Function<ShapedRecipeBuilder, ShapedRecipeBuilder> function, Consumer<FinishedRecipe> consumer)
    {
        function.apply(ShapedRecipeBuilder.shaped(item)).save(consumer);
    }

    private void buildShapelessFromBlock(Item item, @NotNull Function<ShapelessRecipeBuilder, ShapelessRecipeBuilder> function, Consumer<FinishedRecipe> consumer)
    {
        function.apply(ShapelessRecipeBuilder.shapeless(item, 9)).save(consumer, new ResourceLocation(VIP3.MOD_ID, item.getRegistryName().getPath() + "_from_block"));
    }

    private void buildOreCook(@NotNull OreBlock ore, @NotNull Item result, Consumer<FinishedRecipe> consumer)
    {
        final var name = result.getRegistryName().getPath().replace("deepslate_", "");
        final var base = name + "_from_" + ore.getRegistryName().getPath();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ore), result, 20, 200).group(name).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(ore)).save(consumer, new ResourceLocation(VIP3.MOD_ID, base + "_smelt"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ore), result, 20, 100).group(name).unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(ore)).save(consumer, new ResourceLocation(VIP3.MOD_ID, base + "_blast"));
    }

    private void buildCrush(Item ingredient, Item result, float experience, int crushingTime, Consumer<FinishedRecipe> consumer)
    {
        CrushingRecipeBuilder.crushing(Ingredient.of(ingredient), result, experience, crushingTime)
                .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_CRUSHER_ITEM.get()))
                .save(consumer, new ResourceLocation(VIP3.MOD_ID, result.getRegistryName().getPath() + "_from_" + ingredient.getRegistryName().getPath() + "_crush"));
    }
}
