package fr.flowarg.vip3.features;

import fr.flowarg.vip3.VIP3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class VTiers
{
    public static final TagKey<Block> NEEDS_VIPIUM_TOOL = BlockTags.create(new ResourceLocation(VIP3.MOD_ID, "needs_vipium_tool"));
    public static final TagKey<Block> NEEDS_PURE_VIPIUM_TOOL = BlockTags.create(new ResourceLocation(VIP3.MOD_ID, "needs_pure_vipium_tool"));
    public static final TagKey<Block> NEEDS_AUBIN_SLAYER_TOOL = BlockTags.create(new ResourceLocation(VIP3.MOD_ID, "needs_aubin_slayer_tool"));

    public static final Tier VIPIUM;
    public static final Tier PURE_VIPIUM;
    public static final Tier AUBIN_SLAYER;

    static {
        final var localAubinSlayerTier = new ResourceLocation(VIP3.MOD_ID, "aubin_slayer");
        final var localPureVipiumTier = new ResourceLocation(VIP3.MOD_ID, "pure_vipium");
        final var localVipiumTier = new ResourceLocation(VIP3.MOD_ID, "vipium");

        VIPIUM = TierSortingRegistry.registerTier(
                new ForgeTier(5, 2827, 14f, 5f, 10, NEEDS_VIPIUM_TOOL, () -> Ingredient.of(VObjects.VIPIUM_INGOT.get())),
                localVipiumTier,
                List.of(Tiers.DIAMOND, Tiers.NETHERITE),
                List.of(localPureVipiumTier, localAubinSlayerTier));
        PURE_VIPIUM = TierSortingRegistry.registerTier(
                new ForgeTier(6, 7559, 40f, 7f, 30, NEEDS_PURE_VIPIUM_TOOL, () -> Ingredient.of(VObjects.PURE_VIPIUM_INGOT.get())),
                localPureVipiumTier,
                List.of(localVipiumTier),
                List.of(localAubinSlayerTier));
        AUBIN_SLAYER = TierSortingRegistry.registerTier(
                new ForgeTier(10, 10000, 45f, 15f, 50, NEEDS_AUBIN_SLAYER_TOOL, Ingredient::of),
                localAubinSlayerTier,
                List.of(localVipiumTier, localPureVipiumTier),
                List.of());
    }
}
