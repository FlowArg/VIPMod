package fr.flowarg.vip3.features.purifier;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VPurifierInputSlot extends Slot
{
    public VPurifierInputSlot(Container container, int index, int x, int y)
    {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.is(VObjects.VIPIUM_INGOT.get());
    }
}
