package fr.flowarg.vip3.features.altar;

import fr.flowarg.vip3.client.AtlasScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AtlasItem extends Item
{
    public AtlasItem(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand)
    {
        if(!pLevel.isClientSide) return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));

        Minecraft.getInstance().setScreen(new AtlasScreen());

        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }
}
