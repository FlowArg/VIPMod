package fr.flowarg.vip3.features.crusher;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VCrusherInputSlot extends Slot
{
    public VCrusherInputSlot(Container container, int index, int x, int y)
    {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.is(VObjects.PURE_VIPIUM_INGOT.get()) || stack.is(VObjects.VIPIUM_INGOT.get());
    }
}
