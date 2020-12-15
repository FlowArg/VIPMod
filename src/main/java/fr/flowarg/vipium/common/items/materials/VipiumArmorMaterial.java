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

public class VipiumArmorMaterial implements IArmorMaterial
{
    @Override
    public int getDurability(EquipmentSlotType slotIn)
    {
        return (int) (new int[]{19, 22, 26, 13}[slotIn.getIndex()] * this.getToughness());
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn)
    {
        return new int[]{10, 12, 15, 9}[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability()
    {
        return 30;
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
        return Ingredient.fromItems(RegistryHandler.VIPIUM_INGOT.get());
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getName()
    {
        return "vipium:vipium";
    }

    @Override
    public float getToughness()
    {
        return 67;
    }
}
