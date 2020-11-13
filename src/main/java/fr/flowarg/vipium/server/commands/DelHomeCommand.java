package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.Main;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.TranslationTextComponent;

public class DelHomeCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("delhome").then(Commands.argument("homeName", MessageArgument.message()).executes(context -> {
            if(Main.serverManager.getHomeCore().removeHome(context.getSource().getName(), MessageArgument.getMessage(context, "homeName").getFormattedText()) != 0)
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.error"));
                return 1;
            }
            return 0;
        })));
    }
}