package fr.flowarg.vip3.data.data;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
        this.build(VObjects.VIPIUM_PICKAXE.get(), builder ->
                builder.pattern("XXX")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_AXE.get(), builder ->
                builder.pattern(" XX")
                        .pattern(" YX")
                        .pattern(" Y ")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_SHOVEL.get(), builder ->
                builder.pattern(" X ")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_HOE.get(), builder ->
                builder.pattern(" XX")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_MULTI_TOOL.get(), builder ->
                builder.pattern("XXX")
                        .pattern("XYX")
                        .pattern(" Y ")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_SWORD.get(), builder ->
                builder.pattern(" X ")
                        .pattern(" X ")
                        .pattern(" Y ")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);
        
        this.build(VObjects.PURE_VIPIUM_PICKAXE.get(), builder -> 
                builder.pattern("XXX")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        
        this.build(VObjects.PURE_VIPIUM_AXE.get(), builder -> 
                builder.pattern(" XX")
                        .pattern(" YX")
                        .pattern(" Y ")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);
        
        this.build(VObjects.PURE_VIPIUM_SHOVEL.get(), builder -> 
                builder.pattern(" X ")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_HOE.get(), builder ->
                builder.pattern(" XX")
                        .pattern(" Y ")
                        .pattern(" Y ")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_MULTI_TOOL.get(), builder ->
               builder.pattern("XXX")
                       .pattern("XYX")
                       .pattern(" Y ")
                       .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                       .define('Y', Items.STICK)
                       .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_SWORD.get(), builder ->
                builder.pattern(" X ")
                        .pattern(" X ")
                        .pattern(" Y ")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .define('Y', Items.STICK)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_INGOT.get(), builder ->
                builder.pattern("XXX")
                        .pattern("XXX")
                        .define('X', VObjects.VIPIUM_FRAGMENT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_FRAGMENT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_INGOT.get(), builder ->
                builder.pattern("XXX")
                        .pattern("XXX")
                        .define('X', VObjects.PURE_VIPIUM_FRAGMENT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_FRAGMENT.get())), consumer);

        this.build(VObjects.VIPIUM_HELMET.get(), builder ->
                builder.pattern("XXX")
                        .pattern("X X")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_CHESTPLATE.get(), builder ->
                builder.pattern("X X")
                        .pattern("XXX")
                        .pattern("XXX")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_LEGGINGS.get(), builder ->
                builder.pattern("XXX")
                        .pattern("X X")
                        .pattern("X X")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_BOOTS.get(), builder ->
                builder.pattern("X X")
                        .pattern("X X")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_HELMET.get(), builder ->
            builder.pattern("XXX")
                    .pattern("X X")
                    .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                    .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_CHESTPLATE.get(), builder ->
                builder.pattern("X X")
                        .pattern("XXX")
                        .pattern("XXX")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_LEGGINGS.get(), builder ->
                builder.pattern("XXX")
                        .pattern("X X")
                        .pattern("X X")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.PURE_VIPIUM_BOOTS.get(), builder ->
                builder.pattern("X X")
                        .pattern("X X")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get())), consumer);

        this.build(VObjects.VIPIUM_APPLE.get(), builder ->
                builder.pattern("XXX")
                        .pattern("XYX")
                        .pattern("XXX")
                        .define('X', VObjects.VIPIUM_INGOT.get())
                        .define('Y', Items.APPLE)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.VIPIUM_INGOT.get(), Items.APPLE)), consumer);

        this.build(VObjects.PURE_VIPIUM_APPLE.get(), builder ->
                builder.pattern("XXX")
                        .pattern("XYX")
                        .pattern("XXX")
                        .define('X', VObjects.PURE_VIPIUM_INGOT.get())
                        .define('Y', Items.APPLE)
                        .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(VObjects.PURE_VIPIUM_INGOT.get(), Items.APPLE)), consumer);

        this.build(VObjects.FRENCH_BAGUETTE.get(), builder ->
            builder.pattern(" X ")
                    .pattern("YXY")
                    .pattern(" X ")
                    .define('X', Items.WHEAT)
                    .define('Y', Items.PAPER)
                    .unlockedBy("unlock", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PAPER, Items.WHEAT)), consumer);
    }

    private void build(Item item, Function<ShapedRecipeBuilder, ShapedRecipeBuilder> function, Consumer<FinishedRecipe> consumer)
    {
        function.apply(ShapedRecipeBuilder.shaped(item)).save(consumer);
    }
}
