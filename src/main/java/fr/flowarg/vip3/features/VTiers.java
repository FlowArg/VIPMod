package fr.flowarg.vip3.features;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public enum VTiers implements Tier
{
    VIPIUM(5, 2827, 14f, 5f, 10, () -> {
        return Ingredient.of(VObjects.VIPIUM_INGOT.get());
    }),
    PURE_VIPIUM(6, 7559, 40f, 7f, 30, () -> {
        return Ingredient.of(VObjects.PURE_VIPIUM_INGOT.get());
    }),
    AUBIN_SLAYER(10, 10000, 45f, 15f, 50, Ingredient::of);

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    VTiers(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient)
    {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses()
    {
        return this.uses;
    }

    @Override
    public float getSpeed()
    {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus()
    {
        return this.damage;
    }

    @Override
    public int getLevel()
    {
        return this.level;
    }

    @Override
    public int getEnchantmentValue()
    {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient()
    {
        return this.repairIngredient.get();
    }

    public static void init()
    {
        final var vipium = new ResourceLocation(VIP3.MOD_ID, "vipium");
        final var pureVipium = new ResourceLocation(VIP3.MOD_ID, "pure_vipium");
        final var aubinSlayer = new ResourceLocation(VIP3.MOD_ID, "aubin_slayer");
        TierSortingRegistry.registerTier(VIPIUM, vipium, List.of(), List.of(pureVipium, aubinSlayer));
        TierSortingRegistry.registerTier(PURE_VIPIUM, pureVipium, List.of(vipium), List.of(aubinSlayer));
        TierSortingRegistry.registerTier(AUBIN_SLAYER, aubinSlayer, List.of(vipium, pureVipium), List.of());
    }
}
