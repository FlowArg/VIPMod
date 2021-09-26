package fr.flowarg.vip3.features;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.network.capabilities.ArmorConfigurationCapability;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class VObjects
{
    private static boolean registered = false;

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VIP3.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, VIP3.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, VIP3.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, VIP3.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, VIP3.MOD_ID);

    private static final CreativeModeTab VIP_TAB = new CreativeModeTab("vip3") {
        @Override
        public @NotNull ItemStack makeIcon()
        {
            return PURE_VIPIUM_SWORD.get().getDefaultInstance();
        }
    };

    public static final RegistryObject<PickaxeItem> VIPIUM_PICKAXE = ITEMS.register("vipium_pickaxe", () -> new PickaxeItem(VTiers.VIPIUM, 1, 2, newVipiumProperties()));
    public static final RegistryObject<AxeItem> VIPIUM_AXE = ITEMS.register("vipium_axe", () -> new AxeItem(VTiers.VIPIUM, 1, 2, newVipiumProperties()));
    public static final RegistryObject<ShovelItem> VIPIUM_SHOVEL = ITEMS.register("vipium_shovel", () -> new ShovelItem(VTiers.VIPIUM, 1, 2, newVipiumProperties()));
    public static final RegistryObject<HoeItem> VIPIUM_HOE = ITEMS.register("vipium_hoe", () -> new HoeItem(VTiers.VIPIUM, 0, 2, newVipiumProperties()));
    public static final RegistryObject<DiggerItem> VIPIUM_MULTI_TOOL = ITEMS.register("vipium_multi_tool", () -> new VMultiToolItem(2, 2, VTiers.VIPIUM, newVipiumProperties()));
    public static final RegistryObject<SwordItem> VIPIUM_SWORD = ITEMS.register("vipium_sword", () -> new SwordItem(VTiers.VIPIUM, 5, 2, newVipiumProperties()));

    public static final RegistryObject<PickaxeItem> PURE_VIPIUM_PICKAXE = ITEMS.register("pure_vipium_pickaxe", () -> new PickaxeItem(VTiers.PURE_VIPIUM, 4, 4, newVipiumPureProperties()));
    public static final RegistryObject<AxeItem> PURE_VIPIUM_AXE = ITEMS.register("pure_vipium_axe", () -> new AxeItem(VTiers.PURE_VIPIUM, 4, 4, newVipiumPureProperties()));
    public static final RegistryObject<ShovelItem> PURE_VIPIUM_SHOVEL = ITEMS.register("pure_vipium_shovel", () -> new ShovelItem(VTiers.PURE_VIPIUM, 4, 4, newVipiumPureProperties()));
    public static final RegistryObject<HoeItem> PURE_VIPIUM_HOE = ITEMS.register("pure_vipium_hoe", () -> new HoeItem(VTiers.PURE_VIPIUM, 0, 4, newVipiumPureProperties()));
    public static final RegistryObject<DiggerItem> PURE_VIPIUM_MULTI_TOOL = ITEMS.register("pure_vipium_multi_tool", () -> new VMultiToolItem(5, 4, VTiers.PURE_VIPIUM, newVipiumPureProperties()));
    public static final RegistryObject<SwordItem> PURE_VIPIUM_SWORD = ITEMS.register("pure_vipium_sword", () -> new SwordItem(VTiers.PURE_VIPIUM, 8, 4, newVipiumPureProperties()));

    public static final RegistryObject<Item> VIPIUM_FRAGMENT = ITEMS.register("vipium_fragment", () -> new Item(newVipiumProperties()));
    public static final RegistryObject<Item> VIPIUM_INGOT = ITEMS.register("vipium_ingot", () -> new Item(newVipiumProperties()));

    public static final RegistryObject<Item> PURE_VIPIUM_FRAGMENT = ITEMS.register("pure_vipium_fragment", () -> new Item(newVipiumPureProperties()));
    public static final RegistryObject<Item> PURE_VIPIUM_INGOT = ITEMS.register("pure_vipium_ingot", () -> new Item(newVipiumPureProperties()));

    public static final RegistryObject<ArmorItem> VIPIUM_HELMET = ITEMS.register("vipium_helmet", () -> new ArmorItem(VArmorMaterials.VIPIUM, EquipmentSlot.HEAD, newVipiumProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_CHESTPLATE = ITEMS.register("vipium_chestplate", () -> new ArmorItem(VArmorMaterials.VIPIUM, EquipmentSlot.CHEST, newVipiumProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_LEGGINGS = ITEMS.register("vipium_leggings", () -> new ArmorItem(VArmorMaterials.VIPIUM, EquipmentSlot.LEGS, newVipiumProperties()));
    public static final RegistryObject<ArmorItem> VIPIUM_BOOTS = ITEMS.register("vipium_boots", () -> new ArmorItem(VArmorMaterials.VIPIUM, EquipmentSlot.FEET, newVipiumProperties()));

    public static final RegistryObject<ArmorItem> PURE_VIPIUM_HELMET = ITEMS.register("pure_vipium_helmet", () -> new ArmorItem(VArmorMaterials.PURE_VIPIUM, EquipmentSlot.HEAD, newVipiumPureProperties()) {
        @Override
        public void onArmorTick(ItemStack stack, Level world, Player player)
        {
            if(!world.isClientSide)
            {
                player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                    if(armorConfiguration.helmetEffect())
                        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 30, 4, false, false));
                });
            }
        }
    });
    public static final RegistryObject<ArmorItem> PURE_VIPIUM_CHESTPLATE = ITEMS.register("pure_vipium_chestplate", () -> new ArmorItem(VArmorMaterials.PURE_VIPIUM, EquipmentSlot.CHEST, newVipiumPureProperties()) {
        @Override
        public void onArmorTick(ItemStack stack, Level world, Player player)
        {
            if(!world.isClientSide)
            {
                player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                    if(armorConfiguration.chestPlateEffect())
                        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30, 2, false, false));

                    final var valid = player.getInventory().getArmor(0).getItem() == PURE_VIPIUM_BOOTS.get()
                            && player.getInventory().getArmor(1).getItem() == PURE_VIPIUM_LEGGINGS.get()
                            && player.getInventory().getArmor(3).getItem() == PURE_VIPIUM_HELMET.get();

                    if(armorConfiguration.fullSet1Effect() && valid)
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 2, false, false));

                    if(armorConfiguration.fullSet2Effect() && valid)
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30, 2, false, false));
                });
            }
        }
    });
    public static final RegistryObject<ArmorItem> PURE_VIPIUM_LEGGINGS = ITEMS.register("pure_vipium_leggings", () -> new ArmorItem(VArmorMaterials.PURE_VIPIUM, EquipmentSlot.LEGS, newVipiumPureProperties()) {
        @Override
        public void onArmorTick(ItemStack stack, Level world, Player player)
        {
            if(!world.isClientSide)
            {
                player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                    if(armorConfiguration.leggingsEffect())
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 2, false, false));
                });
            }
        }
    });
    public static final RegistryObject<ArmorItem> PURE_VIPIUM_BOOTS = ITEMS.register("pure_vipium_boots", () -> new ArmorItem(VArmorMaterials.PURE_VIPIUM, EquipmentSlot.FEET, newVipiumPureProperties()) {
        @Override
        public void onArmorTick(ItemStack stack, Level world, Player player)
        {
            if(!world.isClientSide)
            {
                player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                    if(armorConfiguration.bootsEffect())
                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 30, 4, false, false));
                });
            }
        }
    });
    ElytraItem

    private static final FoodProperties VIPIUM_APPLE_FOOD = new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 120, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6100, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6100, 0), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2500, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 4000, 3), 0.4f)
            .alwaysEat()
            .build();

    private static final FoodProperties PURE_VIPIUM_APPLE_FOOD = new FoodProperties.Builder()
            .nutrition(15)
            .saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6600, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6600, 1), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 3000, 2), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 4600, 3), 0.8f)
            .alwaysEat()
            .build();

    private static final FoodProperties FRENCH_BAGUETTE_FOOD = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(1.65f)
            .fast()
            .build();

    public static final RegistryObject<Item> VIPIUM_APPLE = ITEMS.register("vipium_apple", () -> new Item(newVipiumProperties().food(VIPIUM_APPLE_FOOD)));
    public static final RegistryObject<Item> PURE_VIPIUM_APPLE = ITEMS.register("pure_vipium_apple", () -> new Item(newVipiumPureProperties().food(PURE_VIPIUM_APPLE_FOOD)));
    public static final RegistryObject<Item> FRENCH_BAGUETTE = ITEMS.register("french_baguette", () -> new Item(new Item.Properties().tab(VIP_TAB).rarity(Rarity.UNCOMMON).food(FRENCH_BAGUETTE_FOOD).setNoRepair()));

    public static final RegistryObject<Item> HOLY_CHICKEN_BIBLE = ITEMS.register("holy_chicken_bible", () -> new Item(newVipiumPureProperties()));
    public static final RegistryObject<Item> TELEPORTATION_ATLAS = ITEMS.register("teleportation_atlas", () -> new Item(newVipiumProperties()));

    public static final RegistryObject<SwordItem> AUBIN_SLAYER = ITEMS.register("aubin_slayer", () -> new AubinSlayer(newVipiumPureProperties()));

    public static final RegistryObject<Block> VIPIUM_BLOCK = BLOCKS.register("vipium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(25F, 50F).sound(SoundType.METAL)));
    public static final RegistryObject<Block> PURE_VIPIUM_BLOCK = BLOCKS.register("pure_vipium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(50F, 100F).sound(SoundType.METAL)));
    public static final RegistryObject<VipiumOre> VIPIUM_ORE = BLOCKS.register("vipium_ore", VipiumOre::new);
    public static final RegistryObject<VipiumOre> DEEPSLATE_VIPIUM_ORE = BLOCKS.register("deepslate_vipium_ore", VipiumOre::new);
    public static final RegistryObject<VCrusherBlock> VIPIUM_CRUSHER = BLOCKS.register("vipium_crusher", () -> new VCrusherBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(9.5F).lightLevel(value -> value.getValue(BlockStateProperties.LIT) ? 13 : 0)));
    public static final RegistryObject<Block> TELEPORTATION_ALTAR = BLOCKS.register("teleportation_altar", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).lightLevel(value -> 15)));

    public static final RegistryObject<BlockItem> VIPIUM_BLOCK_ITEM = ITEMS.register("vipium_block", () -> new BlockItem(VIPIUM_BLOCK.get(), newVipiumProperties()));
    public static final RegistryObject<BlockItem> PURE_VIPIUM_BLOCK_ITEM = ITEMS.register("pure_vipium_block", () -> new BlockItem(PURE_VIPIUM_BLOCK.get(), newVipiumPureProperties()));
    public static final RegistryObject<BlockItem> VIPIUM_ORE_ITEM = ITEMS.register("vipium_ore", () -> new BlockItem(VIPIUM_ORE.get(), new Item.Properties().tab(VIP_TAB)));
    public static final RegistryObject<BlockItem> DEEPSLATE_VIPIUM_ORE_ITEM = ITEMS.register("deepslate_vipium_ore", () -> new BlockItem(DEEPSLATE_VIPIUM_ORE.get(), new Item.Properties().tab(VIP_TAB)));
    public static final RegistryObject<BlockItem> VIPIUM_CRUSHER_ITEM = ITEMS.register("vipium_crusher", () -> new BlockItem(VIPIUM_CRUSHER.get(), newVipiumProperties()));
    public static final RegistryObject<BlockItem> TELEPORTATION_ALTAR_ITEM = ITEMS.register("teleportation_altar", () -> new BlockItem(TELEPORTATION_ALTAR.get(), newVipiumProperties()));

    public static final RegistryObject<BlockEntityType<VCrusherEntity>> VIPIUM_CRUSHER_ENTITY = BLOCK_ENTITIES.register("vipium_crusher", () -> BlockEntityType.Builder.of(VCrusherEntity::new, VIPIUM_CRUSHER.get()).build(null));

    public static final RegistryObject<MenuType<VCrusherMenu>> VIPIUM_CRUSHER_MENU = CONTAINERS.register("vipium_crusher", () -> IForgeContainerType.create(VCrusherMenu::new));

    public static final RecipeType<VCrushingRecipe> CRUSHING_RECIPE = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(VIP3.MOD_ID, "crushing"), new RecipeType<VCrushingRecipe>() {
        @Override
        public String toString()
        {
            return VIP3.MOD_ID + ':' + "crushing";
        }
    });

    public static final RegistryObject<VCrushingRecipeSerializer> CRUSHING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crushing", VCrushingRecipeSerializer::new);

    private static Item.Properties newVipiumProperties()
    {
        return new Item.Properties().tab(VIP_TAB).rarity(Rarity.RARE);
    }

    private static Item.Properties newVipiumPureProperties()
    {
        return new Item.Properties().tab(VIP_TAB).rarity(Rarity.EPIC);
    }

    public static void register(IEventBus bus) {
        if(registered) return;

        VTiers.init();

        VIP3.LOGGER.info("Registering blocks...");
        BLOCKS.register(bus);
        VIP3.LOGGER.info("Registering items...");
        ITEMS.register(bus);
        VIP3.LOGGER.info("Registering recipe serializers...");
        RECIPE_SERIALIZERS.register(bus);
        VIP3.LOGGER.info("Registering block entities...");
        BLOCK_ENTITIES.register(bus);
        VIP3.LOGGER.info("Registering containers...");
        CONTAINERS.register(bus);

        registered = true;
    }
}
