package fr.flowarg.vip3.features;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum VArmorMaterials implements ArmorMaterial
{
    VIPIUM("vip3:vipium", new int[]{19, 22, 26, 13}, new int[]{6, 12, 16, 6}, 30, 67, 0.2F, () -> Ingredient.of(VObjects.VIPIUM_INGOT.get())),
    PURE_VIPIUM("vip3:pure_vipium", new int[]{29, 35, 39, 27}, new int[]{9, 18, 24, 9}, 40, 72, 0.4F, () -> Ingredient.of(VObjects.PURE_VIPIUM_INGOT.get()));

    private final String name;
    private final int[] slotDurability;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    VArmorMaterials(String name, int[] slotDurability, int[] slotProtections, int enchantmentValue, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient)
    {
        this.name = name;
        this.slotDurability = slotDurability;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = SoundEvents.ARMOR_EQUIP_DIAMOND;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot)
    {
        return (int)(this.slotDurability[slot.getIndex()] * this.getToughness());
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot)
    {
        return this.slotProtections[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue()
    {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull SoundEvent getEquipSound()
    {
        return this.sound;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient()
    {
        return this.repairIngredient.get();
    }

    @Override
    public @NotNull String getName()
    {
        return this.name;
    }

    @Override
    public float getToughness()
    {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance()
    {
        return this.knockbackResistance;
    }
}
