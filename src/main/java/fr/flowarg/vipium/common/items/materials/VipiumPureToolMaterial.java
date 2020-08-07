package fr.flowarg.vipium.common.items.materials;

import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class VipiumPureToolMaterial implements IItemTier
{
    @Override
    public int getMaxUses()
    {
        return 7559;
    }

    @Override
    public float getEfficiency()
    {
        return 32;
    }

    @Override
    public float getAttackDamage()
    {
        return 7;
    }

    @Override
    public int getHarvestLevel()
    {
        return 5;
    }

    @Override
    public int getEnchantability()
    {
        return 25;
    }

    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(RegistryHandler.VIPIUM_PURE_INGOT.get());
    }
}
