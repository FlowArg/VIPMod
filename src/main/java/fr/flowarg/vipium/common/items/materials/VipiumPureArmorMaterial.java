package fr.flowarg.vipium.common.items.materials;

import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class VipiumPureArmorMaterial implements IArmorMaterial
{
    @Override
    public int getDurability(EquipmentSlotType slotIn)
    {
        return (int) (new int[]{29, 35, 39, 27}[slotIn.getIndex()] * this.getToughness());
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn)
    {
        return new int[]{13, 16, 19, 12}[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability()
    {
        return 40;
    }

    @Nonnull
    @Override
    public SoundEvent getSoundEvent()
    {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Nonnull
    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(RegistryHandler.VIPIUM_PURE_INGOT.get());
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getName()
    {
        return "vipium:vipium_pure";
    }

    @Override
    public float getToughness()
    {
        return 72;
    }
}
