package fr.flowarg.vip3.features;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class AubinSlayer extends SwordItem implements ManualModel
{
    private static final UUID REACH_UUID = UUID.fromString("D951EE8E-60CF-4E80-BCF1-1F61AE429590");

    public AubinSlayer(Properties properties)
    {
        super(VTiers.AUBIN_SLAYER, 10, 5, properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(REACH_UUID, () -> "AUBIN_SLAYER_REACH", 1.5, AttributeModifier.Operation.ADDITION));
        builder.putAll(super.getAttributeModifiers(slot, stack));
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, stack);
    }
}
