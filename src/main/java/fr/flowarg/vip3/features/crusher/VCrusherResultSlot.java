package fr.flowarg.vip3.features.crusher;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VCrusherResultSlot extends Slot
{
    private final Player player;
    private int removeCount;

    public VCrusherResultSlot(Player player, Container container, int slot, int x, int y)
    {
        super(container, slot, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        return false;
    }

    @Override
    public @NotNull ItemStack remove(int amount)
    {
        if (this.hasItem())
            this.removeCount += Math.min(amount, this.getItem().getCount());

        return super.remove(amount);
    }

    @Override
    public void onTake(@NotNull Player player, @NotNull ItemStack stack)
    {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(@NotNull ItemStack stack, int amount)
    {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack)
    {
        stack.onCraftedBy(this.player.level, this.player, this.removeCount);
        if (this.player instanceof ServerPlayer serverPlayer && this.container instanceof VCrusherEntity crusherEntity)
            crusherEntity.awardUsedRecipesAndPopExperience(serverPlayer);

        this.removeCount = 0;
    }
}
