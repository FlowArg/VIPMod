package fr.flowarg.vipium.common.handlers;

import fr.flowarg.vipium.common.blocks.VipiumPurifierBlock;
import fr.flowarg.vipium.common.blocks.ores.VipiumOre;
import fr.flowarg.vipium.common.containers.VipiumPurifierContainer;
import fr.flowarg.vipium.common.items.VipiumCompassItem;
import fr.flowarg.vipium.common.items.armor.*;
import fr.flowarg.vipium.common.items.materials.VipiumArmorMaterial;
import fr.flowarg.vipium.common.items.materials.VipiumPureArmorMaterial;
import fr.flowarg.vipium.common.items.materials.VipiumPureToolMaterial;
import fr.flowarg.vipium.common.items.materials.VipiumToolMaterial;
import fr.flowarg.vipium.common.items.tools.*;
import fr.flowarg.vipium.common.tilentities.VipiumPurifierTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static fr.flowarg.vipium.Main.*;

public class RegistryHandler
{
    public static final IArmorMaterial VIPIUM_ARMOR_MATERIAL = new VipiumArmorMaterial();
    public static final IArmorMaterial VIPIUM_PURE_ARMOR_MATERIAL = new VipiumPureArmorMaterial();
    public static final IItemTier VIPIUM_TOOL_MATERIAL = new VipiumToolMaterial();
    public static final IItemTier VIPIUM_PURE_TOOL_MATERIAL = new VipiumPureToolMaterial();
    public static final Food VIPIUM_APPLE_FOOD = new Food.Builder().hunger(10).saturation(1.5f).effect(() -> new EffectInstance(Effects.REGENERATION, 120, 2), 1.0f).effect(() -> new EffectInstance(Effects.RESISTANCE, 6100, 1), 1.0f).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 6100, 0), 1.0f).effect(() -> new EffectInstance(Effects.ABSORPTION, 2500, 2), 1.0f).effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 4000, 3), 0.4f).setAlwaysEdible().build();
    public static final Food VIPIUM_PURE_APPLE_FOOD = new Food.Builder().hunger(12).saturation(1.8f).effect(() -> new EffectInstance(Effects.REGENERATION, 160, 2), 1.0f).effect(() -> new EffectInstance(Effects.RESISTANCE, 6300, 2), 1.0f).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 6300, 1), 1.0f).effect(() -> new EffectInstance(Effects.ABSORPTION, 2800, 2), 1.0f).effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 4300, 3), 0.8f).setAlwaysEdible().build();
    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> VIPIUM_BLOCK = BLOCKS.register("vipium_block", () -> new Block(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).hardnessAndResistance(30f, 20f)));
    public static final RegistryObject<VipiumOre> VIPIUM_ORE = BLOCKS.register("vipium_ore", VipiumOre::new);
    public static final RegistryObject<Block> VIPIUM_PURE_BLOCK = BLOCKS.register("vipium_pure_block", () -> new Block(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).hardnessAndResistance(60f, 90f)));
    public static final RegistryObject<VipiumPurifierBlock> VIPIUM_PURIFIER = BLOCKS.register("vipium_purifier", VipiumPurifierBlock::new);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> VIPIUM_BLOCK_ITEM = ITEMS.register("vipium_block", () -> new BlockItem(VIPIUM_BLOCK.get(), new Item.Properties().group(BLOCK_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VIPIUM_PURE_BLOCK_ITEM = ITEMS.register("vipium_pure_block", () -> new BlockItem(VIPIUM_PURE_BLOCK.get(), new Item.Properties().group(BLOCK_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> VIPIUM_ORE_ITEM = ITEMS.register("vipium_ore", () -> new BlockItem(VIPIUM_ORE.get(), new Item.Properties().group(BLOCK_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VIPIUM_PURIFIER_ITEM = ITEMS.register("vipium_purifier", () -> new BlockItem(VIPIUM_PURIFIER.get(), new Item.Properties().group(BLOCK_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VIPIUM_FRAGMENT = ITEMS.register("vipium_fragment", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VIPIUM_INGOT = ITEMS.register("vipium_ingot", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> VIPIUM_APPLE = ITEMS.register("vipium_apple", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.RARE).food(VIPIUM_APPLE_FOOD)));
    public static final RegistryObject<ArmorItem> VIPIUM_HELMET = ITEMS.register("vipium_helmet", VipiumHelmetItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_CHESTPLATE = ITEMS.register("vipium_chestplate", VipiumChestplateItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_LEGGINGS = ITEMS.register("vipium_leggings", VipiumLeggingsItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_BOOTS = ITEMS.register("vipium_boots", VipiumBootsItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_HELMET = ITEMS.register("vipium_pure_helmet", VipiumPureHelmetItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_CHESTPLATE = ITEMS.register("vipium_pure_chestplate", VipiumPureChestplateItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_LEGGINGS = ITEMS.register("vipium_pure_leggings", VipiumPureLeggingsItem::new);
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_BOOTS = ITEMS.register("vipium_pure_boots", VipiumPureBootsItem::new);
    public static final RegistryObject<PickaxeItem> VIPIUM_PICKAXE = ITEMS.register("vipium_pickaxe", VipiumPickaxeItem::new);
    public static final RegistryObject<AxeItem> VIPIUM_AXE = ITEMS.register("vipium_axe", VipiumAxeItem::new);
    public static final RegistryObject<HoeItem> VIPIUM_HOE = ITEMS.register("vipium_hoe", VipiumHoeItem::new);
    public static final RegistryObject<ShovelItem> VIPIUM_SHOVEL = ITEMS.register("vipium_shovel", VipiumShovelItem::new);
    public static final RegistryObject<ToolItem> VIPIUM_MULTI_TOOL = ITEMS.register("vipium_multi_tool", VipiumMultiToolItem::new);
    public static final RegistryObject<SwordItem> VIPIUM_SWORD = ITEMS.register("vipium_sword", VipiumSwordItem::new);
    public static final RegistryObject<PickaxeItem> VIPIUM_PURE_PICKAXE = ITEMS.register("vipium_pure_pickaxe", VipiumPurePickaxeItem::new);
    public static final RegistryObject<AxeItem> VIPIUM_PURE_AXE = ITEMS.register("vipium_pure_axe", VipiumPureAxeItem::new);
    public static final RegistryObject<HoeItem> VIPIUM_PURE_HOE = ITEMS.register("vipium_pure_hoe", VipiumPureHoeItem::new);
    public static final RegistryObject<ShovelItem> VIPIUM_PURE_SHOVEL = ITEMS.register("vipium_pure_shovel", VipiumPureShovelItem::new);
    public static final RegistryObject<ToolItem> VIPIUM_PURE_MULTI_TOOL = ITEMS.register("vipium_pure_multi_tool", VipiumPureMultiToolItem::new);
    public static final RegistryObject<SwordItem> VIPIUM_PURE_SWORD = ITEMS.register("vipium_pure_sword", VipiumPureSwordItem::new);
    public static final RegistryObject<Item> VIPIUM_PURE_FRAGMENT = ITEMS.register("vipium_pure_fragment", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> VIPIUM_PURE_INGOT = ITEMS.register("vipium_pure_ingot", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> VIPIUM_PURE_APPLE = ITEMS.register("vipium_pure_apple", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.EPIC).food(VIPIUM_PURE_APPLE_FOOD)));
    public static final RegistryObject<Item> VIPIUM_COMPASS = ITEMS.register("vipium_compass", VipiumCompassItem::new);
    public static final RegistryObject<Item> VIPIUM_COAL = ITEMS.register("vipium_coal", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> VIPIUM_FUEL_DEP = ITEMS.register("vipium_fuel_dep", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> LETTER = ITEMS.register("letter", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(Rarity.COMMON).setNoRepair()));

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final RegistryObject<TileEntityType<VipiumPurifierTileEntity>> VIPIUM_PURIFIER_TILE_ENTITY = TILE_ENTITIES.register("vipium_purifier", () -> TileEntityType.Builder.create(VipiumPurifierTileEntity::new, VIPIUM_PURIFIER.get()).build(null));
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);
    public static final RegistryObject<ContainerType<VipiumPurifierContainer>> VIPIUM_PURIFIER_CONTAINER = CONTAINERS.register("vipium_purifier", () -> IForgeContainerType.create(VipiumPurifierContainer::new));

    public static void init(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILE_ENTITIES.register(bus);
        CONTAINERS.register(bus);
    }

    public static DeferredRegister<Block> getBlocks()
    {
        return BLOCKS;
    }

    public static DeferredRegister<ContainerType<?>> getContainers()
    {
        return CONTAINERS;
    }

    public static DeferredRegister<Item> getItems()
    {
        return ITEMS;
    }

    public static DeferredRegister<TileEntityType<?>> getTileEntities()
    {
        return TILE_ENTITIES;
    }
}
