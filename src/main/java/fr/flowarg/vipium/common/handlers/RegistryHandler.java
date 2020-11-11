package fr.flowarg.vipium.common.handlers;

import fr.flowarg.vipium.client.renderer.VipiumChestItemStackRenderer;
import fr.flowarg.vipium.common.blocks.VipiumChestBlock;
import fr.flowarg.vipium.common.blocks.VipiumPurifierBlock;
import fr.flowarg.vipium.common.blocks.VipiumRailBlock;
import fr.flowarg.vipium.common.blocks.ores.VipiumOre;
import fr.flowarg.vipium.common.containers.VipiumChestContainer;
import fr.flowarg.vipium.common.containers.VipiumPurifierContainer;
import fr.flowarg.vipium.common.containers.slots.upgrades.UpgradeType;
import fr.flowarg.vipium.common.entities.VipiumMinecartEntity;
import fr.flowarg.vipium.common.items.VipiumMinecartItem;
import fr.flowarg.vipium.common.items.VipiumMultiToolItem;
import fr.flowarg.vipium.common.items.materials.VipiumArmorMaterial;
import fr.flowarg.vipium.common.items.materials.VipiumPureArmorMaterial;
import fr.flowarg.vipium.common.items.materials.VipiumPureToolMaterial;
import fr.flowarg.vipium.common.items.materials.VipiumToolMaterial;
import fr.flowarg.vipium.common.items.UpgradeItem;
import fr.flowarg.vipium.common.tileentities.VipiumChestTileEntity;
import fr.flowarg.vipium.common.tileentities.VipiumPurifierTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.Callable;

import static fr.flowarg.vipium.Main.*;
import static net.minecraft.item.Rarity.*;

public class RegistryHandler
{
    public static final IArmorMaterial VIPIUM_ARMOR_MATERIAL = new VipiumArmorMaterial();
    public static final IArmorMaterial VIPIUM_PURE_ARMOR_MATERIAL = new VipiumPureArmorMaterial();
    public static final IItemTier VIPIUM_TOOL_MATERIAL = new VipiumToolMaterial();
    public static final IItemTier VIPIUM_PURE_TOOL_MATERIAL = new VipiumPureToolMaterial();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static final Food VIPIUM_APPLE_FOOD = new Food.Builder().hunger(10).saturation(1.5f).effect(() -> new EffectInstance(Effects.REGENERATION, 120, 2), 1.0f).effect(() -> new EffectInstance(Effects.RESISTANCE, 6100, 1), 1.0f).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 6100, 0), 1.0f).effect(() -> new EffectInstance(Effects.ABSORPTION, 2500, 2), 1.0f).effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 4000, 3), 0.4f).setAlwaysEdible().build();
    public static final Food VIPIUM_PURE_APPLE_FOOD = new Food.Builder().hunger(12).saturation(1.8f).effect(() -> new EffectInstance(Effects.REGENERATION, 200, 2), 1.0f).effect(() -> new EffectInstance(Effects.RESISTANCE, 6600, 2), 1.0f).effect(() -> new EffectInstance(Effects.FIRE_RESISTANCE, 6600, 1), 1.0f).effect(() -> new EffectInstance(Effects.ABSORPTION, 3000, 2), 1.0f).effect(() -> new EffectInstance(Effects.HEALTH_BOOST, 4600, 3), 0.8f).setAlwaysEdible().build();
    public static final Food FRENCH_BAGUETTE_FOOD = new Food.Builder().hunger(8).saturation(1.7f).fastToEat().build();

    public static final RegistryObject<Block> VIPIUM_BLOCK = BLOCKS.register("vipium_block", () -> new Block(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).hardnessAndResistance(30f, 30f)));
    public static final RegistryObject<VipiumOre> VIPIUM_ORE = BLOCKS.register("vipium_ore", VipiumOre::new);
    public static final RegistryObject<Block> VIPIUM_PURE_BLOCK = BLOCKS.register("vipium_pure_block", () -> new Block(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).hardnessAndResistance(60f, 90f)));
    public static final RegistryObject<VipiumPurifierBlock> VIPIUM_PURIFIER_BLOCK = BLOCKS.register("vipium_purifier", VipiumPurifierBlock::new);
    public static final RegistryObject<RailBlock> VIPIUM_RAIL_BLOCK = BLOCKS.register("vipium_rail", VipiumRailBlock::new);
    public static final RegistryObject<RailBlock> VIPIUM_PURE_RAIL_BLOCK = BLOCKS.register("vipium_pure_rail", VipiumRailBlock::new);
    public static final RegistryObject<VipiumChestBlock> VIPIUM_CHEST_BLOCK = BLOCKS.register("vipium_chest", VipiumChestBlock::new);

    public static final RegistryObject<BlockItem> VIPIUM_BLOCK_ITEM = ITEMS.register("vipium_block", () -> new BlockItem(VIPIUM_BLOCK.get(), newItemBlockVipiumProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_PURE_BLOCK_ITEM = ITEMS.register("vipium_pure_block", () -> new BlockItem(VIPIUM_PURE_BLOCK.get(), newItemBlockVipiumPureProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_ORE_ITEM = ITEMS.register("vipium_ore", () -> new BlockItem(VIPIUM_ORE.get(), newItemBlockVipiumProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_PURIFIER_ITEM = ITEMS.register("vipium_purifier", () -> new BlockItem(VIPIUM_PURIFIER_BLOCK.get(), newItemBlockVipiumProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_RAIL_ITEM = ITEMS.register("vipium_rail", () -> new BlockItem(VIPIUM_RAIL_BLOCK.get(), newItemBlockVipiumProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_PURE_RAIL_ITEM = ITEMS.register("vipium_pure_rail", () -> new BlockItem(VIPIUM_PURE_RAIL_BLOCK.get(), newItemBlockVipiumPureProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_CHEST_ITEM = registerVipiumChestBlockItem();
    
    private static RegistryObject<BlockItem> registerVipiumChestBlockItem()
    {
        return ITEMS.register("vipium_chest", () -> new BlockItem(VIPIUM_CHEST_BLOCK.get(), newItemBlockVipiumProperties().setISTER(() -> createVipiumChestRenderer())));
    }

    public static final RegistryObject<ArmorItem> VIPIUM_HELMET = ITEMS.register("vipium_helmet", () -> new ArmorItem(RegistryHandler.VIPIUM_ARMOR_MATERIAL, EquipmentSlotType.HEAD, newItemVipiumProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_CHESTPLATE = ITEMS.register("vipium_chestplate", () -> new ArmorItem(RegistryHandler.VIPIUM_ARMOR_MATERIAL, EquipmentSlotType.CHEST, newItemVipiumProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_LEGGINGS = ITEMS.register("vipium_leggings", () -> new ArmorItem(RegistryHandler.VIPIUM_ARMOR_MATERIAL, EquipmentSlotType.LEGS, newItemVipiumProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_BOOTS = ITEMS.register("vipium_boots", () -> new ArmorItem(RegistryHandler.VIPIUM_ARMOR_MATERIAL, EquipmentSlotType.FEET, newItemVipiumProperties()));

    public static final RegistryObject<ArmorItem> VIPIUM_PURE_HELMET = ITEMS.register("vipium_pure_helmet", () -> new ArmorItem(RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL, EquipmentSlotType.HEAD, newItemVipiumPureProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_CHESTPLATE = ITEMS.register("vipium_pure_chestplate", () -> new ArmorItem(RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL, EquipmentSlotType.CHEST, newItemVipiumPureProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_LEGGINGS = ITEMS.register("vipium_pure_leggings", () -> new ArmorItem(RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL, EquipmentSlotType.LEGS, newItemVipiumPureProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_PURE_BOOTS = ITEMS.register("vipium_pure_boots", () -> new ArmorItem(RegistryHandler.VIPIUM_PURE_ARMOR_MATERIAL, EquipmentSlotType.FEET, newItemVipiumPureProperties()));

    public static final RegistryObject<PickaxeItem> VIPIUM_PICKAXE = ITEMS.register("vipium_pickaxe", () -> new PickaxeItem(RegistryHandler.VIPIUM_TOOL_MATERIAL, 1, 2, newItemVipiumProperties()));
    public static final RegistryObject<AxeItem> VIPIUM_AXE = ITEMS.register("vipium_axe", () -> new AxeItem(RegistryHandler.VIPIUM_TOOL_MATERIAL, 1, 2, newItemVipiumProperties()));
    public static final RegistryObject<ShovelItem> VIPIUM_SHOVEL = ITEMS.register("vipium_shovel", () -> new ShovelItem(RegistryHandler.VIPIUM_TOOL_MATERIAL, 1, 2, newItemVipiumProperties()));
    public static final RegistryObject<HoeItem> VIPIUM_HOE = ITEMS.register("vipium_hoe", () -> new HoeItem(RegistryHandler.VIPIUM_TOOL_MATERIAL, 2, newItemVipiumProperties()));
    public static final RegistryObject<ToolItem> VIPIUM_MULTI_TOOL = ITEMS.register("vipium_multi_tool", () -> new VipiumMultiToolItem(2, 2, RegistryHandler.VIPIUM_TOOL_MATERIAL, newItemVipiumProperties()));
    public static final RegistryObject<SwordItem> VIPIUM_SWORD = ITEMS.register("vipium_sword", () -> new SwordItem(RegistryHandler.VIPIUM_TOOL_MATERIAL, 5, 2, newItemVipiumProperties()));

    public static final RegistryObject<PickaxeItem> VIPIUM_PURE_PICKAXE = ITEMS.register("vipium_pure_pickaxe", () -> new PickaxeItem(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 4, 5, newItemVipiumPureProperties()));
    public static final RegistryObject<AxeItem> VIPIUM_PURE_AXE = ITEMS.register("vipium_pure_axe", () -> new AxeItem(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 4, 5, newItemVipiumPureProperties()));
    public static final RegistryObject<ShovelItem> VIPIUM_PURE_SHOVEL = ITEMS.register("vipium_pure_shovel", () -> new ShovelItem(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 4, 5, newItemVipiumPureProperties()));
    public static final RegistryObject<HoeItem> VIPIUM_PURE_HOE = ITEMS.register("vipium_pure_hoe", () -> new HoeItem(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 5, newItemVipiumPureProperties()));
    public static final RegistryObject<ToolItem> VIPIUM_PURE_MULTI_TOOL = ITEMS.register("vipium_pure_multi_tool", () -> new VipiumMultiToolItem(5, 5, RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, newItemVipiumPureProperties()));
    public static final RegistryObject<SwordItem> VIPIUM_PURE_SWORD = ITEMS.register("vipium_pure_sword", () -> new SwordItem(RegistryHandler.VIPIUM_PURE_TOOL_MATERIAL, 8, 5, newItemVipiumPureProperties()));

    public static final RegistryObject<Item> VIPIUM_FRAGMENT = ITEMS.register("vipium_fragment", () -> new Item(newItemVipiumProperties()));
    public static final RegistryObject<Item> VIPIUM_INGOT = ITEMS.register("vipium_ingot", () -> new Item(newItemVipiumProperties()));
    public static final RegistryObject<Item> VIPIUM_APPLE = ITEMS.register("vipium_apple", () -> new Item(newItemVipiumProperties().food(VIPIUM_APPLE_FOOD)));

    public static final RegistryObject<Item> VIPIUM_PURE_FRAGMENT = ITEMS.register("vipium_pure_fragment", () -> new Item(newItemVipiumPureProperties()));
    public static final RegistryObject<Item> VIPIUM_PURE_INGOT = ITEMS.register("vipium_pure_ingot", () -> new Item(newItemVipiumPureProperties()));
    public static final RegistryObject<Item> VIPIUM_PURE_APPLE = ITEMS.register("vipium_pure_apple", () -> new Item(newItemVipiumPureProperties().food(VIPIUM_PURE_APPLE_FOOD)));

    public static final RegistryObject<Item> VIPIUM_COAL = ITEMS.register("vipium_coal", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(UNCOMMON)));
    public static final RegistryObject<Item> LETTER = ITEMS.register("letter", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(COMMON).setNoRepair()));
    public static final RegistryObject<Item> ENVELOPE_OPEN = ITEMS.register("envelope_open", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(COMMON).setNoRepair()));
    public static final RegistryObject<Item> ENVELOPE_CLOSE = ITEMS.register("envelope_close", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(COMMON).setNoRepair()));
    public static final RegistryObject<Item> FRENCH_BAGUETTE = ITEMS.register("french_baguette", () -> new Item(new Item.Properties().group(ITEM_GROUP).rarity(COMMON).food(FRENCH_BAGUETTE_FOOD).setNoRepair()));
    public static final RegistryObject<Item> VIPIUM_MINECART = ITEMS.register("vipium_minecart", VipiumMinecartItem::new);

    public static final RegistryObject<UpgradeItem> CHEST_EXTENSION_UPGRADE = ITEMS.register("chest_extension_upgrade", () -> new UpgradeItem(UpgradeType.CHEST_EXTENSION, new Item.Properties().group(ITEM_GROUP).rarity(UNCOMMON)));
    public static final RegistryObject<UpgradeItem> CHEST_PURIFIER_UPGRADE = ITEMS.register("chest_purifier_upgrade", () -> new UpgradeItem(UpgradeType.CHEST_PURIFIER, new Item.Properties().group(ITEM_GROUP).rarity(UNCOMMON)));

    public static final RegistryObject<TileEntityType<VipiumPurifierTileEntity>> VIPIUM_PURIFIER_TILE_ENTITY = TILE_ENTITIES.register("vipium_purifier", () -> TileEntityType.Builder.create(VipiumPurifierTileEntity::new, VIPIUM_PURIFIER_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<VipiumChestTileEntity>> VIPIUM_CHEST_TILE_ENTITY = TILE_ENTITIES.register("vipium_chest", () -> TileEntityType.Builder.create(VipiumChestTileEntity::new, VIPIUM_CHEST_BLOCK.get()).build(null));

    public static final RegistryObject<ContainerType<VipiumPurifierContainer>> VIPIUM_PURIFIER_CONTAINER = CONTAINERS.register("vipium_purifier", () -> IForgeContainerType.create(VipiumPurifierContainer::new));
    public static final RegistryObject<ContainerType<VipiumChestContainer>> VIPIUM_CHEST_CONTAINER = CONTAINERS.register("vipium_chest", () -> IForgeContainerType.create(VipiumChestContainer::new));

    public static final RegistryObject<EntityType<VipiumMinecartEntity>> VIPIUM_MINECART_ENTITY = ENTITIES.register("vipium_minecart", () -> EntityType.Builder.create((EntityType.IFactory<VipiumMinecartEntity>)VipiumMinecartEntity::new, EntityClassification.MISC).size(0.98F, 0.7F).build("vipium_minecart"));

    public static Item.Properties newItemVipiumProperties()
    {
        return new Item.Properties().group(ITEM_GROUP).rarity(RARE);
    }

    public static Item.Properties newItemBlockVipiumProperties()
    {
        return new Item.Properties().group(BLOCK_GROUP).rarity(RARE);
    }

    public static Item.Properties newItemVipiumPureProperties()
    {
        return new Item.Properties().group(ITEM_GROUP).rarity(EPIC);
    }

    public static Item.Properties newItemBlockVipiumPureProperties()
    {
        return new Item.Properties().group(BLOCK_GROUP).rarity(EPIC);
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> createVipiumChestRenderer()
    {
        return () -> new VipiumChestItemStackRenderer(VipiumChestTileEntity::new);
    }

    public static void init(IEventBus bus)
    {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILE_ENTITIES.register(bus);
        CONTAINERS.register(bus);
        ENTITIES.register(bus);
    }
}
