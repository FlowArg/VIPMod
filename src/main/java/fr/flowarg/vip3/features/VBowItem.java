package fr.flowarg.vip3.features;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

public class VBowItem extends BowItem implements ManualModel
{
    public VBowItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft)
    {
        if (!(pEntityLiving instanceof Player player))
            return;

        boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
        ItemStack itemStack = player.getProjectile(pStack);

        int i = this.getUseDuration(pStack) - pTimeLeft;
        i = ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemStack.isEmpty() || flag);

        if (i < 0) return;

        if (!itemStack.isEmpty() || flag)
        {
            if (itemStack.isEmpty())
                itemStack = new ItemStack(Items.ARROW);

            float power = getPowerForTime(i) * 1.2f;

            if (((double)power < 0.1D))
                return;

            boolean flag1 = player.getAbilities().instabuild || (itemStack.getItem() instanceof ArrowItem && ((ArrowItem)itemStack.getItem()).isInfinite(itemStack, pStack, player));

            if (!pLevel.isClientSide)
            {
                ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                final var abstractArrow = arrowItem.createArrow(pLevel, itemStack, player);
                abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, power * 3.0F, 1.0F);

                if (power == 1.0F)
                    abstractArrow.setCritArrow(true);

                int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);

                abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + 6);

                if (j > 0)
                    abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double)j * 0.5D + 0.5D);

                final int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);

                abstractArrow.setKnockback(3 + k);

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack) > 0)
                    abstractArrow.setSecondsOnFire(100);

                pStack.hurtAndBreak(1, player, (p_40665_) -> p_40665_.broadcastBreakEvent(player.getUsedItemHand()));

                if (flag1 || player.getAbilities().instabuild && (itemStack.is(Items.SPECTRAL_ARROW) || itemStack.is(Items.TIPPED_ARROW)))
                    abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                pLevel.addFreshEntity(abstractArrow);
            }

            pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);

            if (!flag1 && !player.getAbilities().instabuild)
            {
                itemStack.shrink(1);

                if (itemStack.isEmpty())
                    player.getInventory().removeItem(itemStack);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }
}
