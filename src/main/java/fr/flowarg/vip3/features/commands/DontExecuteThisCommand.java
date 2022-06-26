package fr.flowarg.vip3.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

public class DontExecuteThisCommand
{
    static void registerDontExecuteThisCommand(@NotNull CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> ftgCommand = Commands.literal("dontexecutethis").executes(DontExecuteThisCommand::runDontExecuteThisCommand);
        dispatcher.register(ftgCommand);
    }

    private static int runDontExecuteThisCommand(@NotNull CommandContext<CommandSourceStack> ctx)
    {
        CommandSourceStack source = ctx.getSource();
        Entity entity = source.getEntity();

        if(!(entity instanceof Player player))
            return -1;

        applyDontExecuteThisCommand(player);

        ctx.getSource().sendSuccess(new TextComponent("R.I.P"), true);
        return 0;
    }

    public static void applyDontExecuteThisCommand(@NotNull Player player)
    {
        final var itemStack = new ItemStack(Items.CARVED_PUMPKIN);
        itemStack.enchant(Enchantments.BINDING_CURSE, 1);

        player.getInventory().armor.set(3, itemStack);;
    }
}
