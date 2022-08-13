package fr.flowarg.vip3.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ModelLoadingOptimizer
{
    private static final List<BlockElement> BLOCK_ELEMENTS_EMPTY = new ArrayList<>();
    private static final List<ItemOverride> ITEM_OVERRIDES_EMPTY = new ArrayList<>();
    private static final List<Item> ITEMS = List.of(Items.ACACIA_BOAT, Items.AMETHYST_SHARD, Items.APPLE, Items.ARMOR_STAND, Items.ARROW, Items.BAKED_POTATO, Items.BEEF, Items.BEETROOT, Items.BEETROOT_SOUP, Items.BIRCH_BOAT, Items.BLACK_DYE, Items.BLAZE_POWDER, Items.BLUE_DYE, Items.BONE_MEAL, Items.BOOK, Items.BOWL, Items.BREAD, Items.BRICK, Items.BROWN_DYE, Items.BUCKET, Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_LEGGINGS, Items.CHARCOAL, Items.CHEST_MINECART, Items.CHICKEN, Items.CHORUS_FRUIT, Items.CLAY_BALL, Items.COAL, Items.COD_BUCKET, Items.COMMAND_BLOCK_MINECART, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON, Items.COOKIE, Items.RAW_COPPER, Items.COPPER_INGOT, Items.CREEPER_BANNER_PATTERN, Items.CYAN_DYE, Items.DARK_OAK_BOAT, Items.DIAMOND, Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND_LEGGINGS, Items.DRAGON_BREATH, Items.DRIED_KELP, Items.EGG, Items.EMERALD, Items.ENCHANTED_BOOK, Items.ENDER_EYE, Items.ENDER_PEARL, Items.END_CRYSTAL, Items.EXPERIENCE_BOTTLE, Items.FERMENTED_SPIDER_EYE, Items.FIREWORK_ROCKET, Items.FIRE_CHARGE, Items.FLINT, Items.FLINT_AND_STEEL, Items.FLOWER_BANNER_PATTERN, Items.FURNACE_MINECART, Items.GHAST_TEAR, Items.GLASS_BOTTLE, Items.GLISTERING_MELON_SLICE, Items.GLOBE_BANNER_PATTERN, Items.GLOW_BERRIES, Items.GLOWSTONE_DUST, Items.GLOW_INK_SAC, Items.GLOW_ITEM_FRAME, Items.RAW_GOLD, Items.GOLDEN_APPLE, Items.GOLDEN_BOOTS, Items.GOLDEN_CARROT, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_LEGGINGS, Items.GOLD_INGOT, Items.GOLD_NUGGET, Items.GRAY_DYE, Items.GREEN_DYE, Items.GUNPOWDER, Items.HEART_OF_THE_SEA, Items.HONEYCOMB, Items.HONEY_BOTTLE, Items.HOPPER_MINECART, Items.INK_SAC, Items.RAW_IRON, Items.IRON_BOOTS, Items.IRON_CHESTPLATE, Items.IRON_HELMET, Items.IRON_HORSE_ARMOR, Items.IRON_INGOT, Items.IRON_LEGGINGS, Items.IRON_NUGGET, Items.ITEM_FRAME, Items.JUNGLE_BOAT, Items.KNOWLEDGE_BOOK, Items.LAPIS_LAZULI, Items.LAVA_BUCKET, Items.LEATHER, Items.LEATHER_HORSE_ARMOR, Items.LIGHT_BLUE_DYE, Items.LIGHT_GRAY_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.MAGMA_CREAM, Items.MAP, Items.MELON_SLICE, Items.MILK_BUCKET, Items.MINECART, Items.MOJANG_BANNER_PATTERN, Items.MUSHROOM_STEW, Items.MUSIC_DISC_11, Items.MUSIC_DISC_13, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_PIGSTEP, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WAIT, Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_OTHERSIDE, Items.MUTTON, Items.NAME_TAG, Items.NAUTILUS_SHELL, Items.NETHERITE_BOOTS, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET, Items.NETHERITE_INGOT, Items.NETHERITE_LEGGINGS, Items.NETHERITE_SCRAP, Items.NETHER_BRICK, Items.NETHER_STAR, Items.OAK_BOAT, Items.ORANGE_DYE, Items.PAINTING, Items.PAPER, Items.PHANTOM_MEMBRANE, Items.PIGLIN_BANNER_PATTERN, Items.PINK_DYE, Items.POISONOUS_POTATO, Items.POPPED_CHORUS_FRUIT, Items.PORKCHOP, Items.POWDER_SNOW_BUCKET, Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD, Items.PUFFERFISH, Items.PUFFERFISH_BUCKET, Items.PUMPKIN_PIE, Items.PURPLE_DYE, Items.QUARTZ, Items.RABBIT, Items.RABBIT_FOOT, Items.RABBIT_HIDE, Items.RABBIT_STEW, Items.RED_DYE, Items.ROTTEN_FLESH, Items.SADDLE, Items.SALMON, Items.SALMON_BUCKET, Items.SCUTE, Items.SHEARS, Items.SHULKER_SHELL, Items.SKULL_BANNER_PATTERN, Items.SLIME_BALL, Items.SNOWBALL, Items.SPECTRAL_ARROW, Items.SPIDER_EYE, Items.SPRUCE_BOAT, Items.SPYGLASS, Items.SUGAR, Items.SUSPICIOUS_STEW, Items.TNT_MINECART, Items.TOTEM_OF_UNDYING, Items.TRIDENT, Items.TROPICAL_FISH, Items.TROPICAL_FISH_BUCKET, Items.AXOLOTL_BUCKET, Items.TURTLE_HELMET, Items.WATER_BUCKET, Items.WHEAT, Items.WHITE_DYE, Items.WRITABLE_BOOK, Items.WRITTEN_BOOK, Items.YELLOW_DYE);

    @CalledAtRuntime
    public static final List<String> ITEMS_LOCATION = ITEMS.stream().map(Registry.ITEM::getKey).map(resourceLocation -> new ModelResourceLocation(resourceLocation.toString(), "inventory")).map(ModelResourceLocation::toString).toList();

    @CalledAtRuntime
    public static @NotNull Pair<ResourceLocation, BlockModel> createGeneratedItemModel(@NotNull ResourceLocation item)
    {
        LogUtils.getLogger().info("Creating generated item model for " + item);
        final Map<String, Either<Material, String>> textures = new HashMap<>();
        final var location = new ResourceLocation(item.getNamespace(), "item/" + item.getPath());
        textures.put("layer0", Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, location)));
        final var model = new BlockModel(new ResourceLocation("item/generated"), BLOCK_ELEMENTS_EMPTY, textures, true, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, ITEM_OVERRIDES_EMPTY);
        return Pair.of(location, model);
    }
}
