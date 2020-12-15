package fr.flowarg.vipium.common.items.materials;

import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class VipiumToolMaterial implements IItemTier
{
    @Override
    public int getMaxUses()
    {
        return 2827;
    }

    @Override
    public float getEfficiency()
    {
        return 22;
    }

    @Override
    public float getAttackDamage()
    {
        return 4;
    }

    @Override
    public int getHarvestLevel()
    {
        return 4;
    }

    @Override
    public int getEnchantability()
    {
        return 20;
    }

    @Override
    public Ingredient getRepairMaterial()
    {
        return Ingredient.fromItems(RegistryHandler.VIPIUM_INGOT.get());
    }
}
