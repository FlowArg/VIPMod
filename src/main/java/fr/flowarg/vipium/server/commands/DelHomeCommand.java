package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class DelHomeCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("delhome").then(Commands.argument("homeName", MessageArgument.message()).executes(context -> {
            if(VIPMod.serverManager.getHomeCore().removeHome(context.getSource().getName(), MessageArgument.getMessage(context, "homeName").getFormattedText()) != 0)
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.error"));
                return 1;
            }
            context.getSource().sendFeedback(new TranslationTextComponent("commands.home.successdel"), true);
            return 0;
        })));
    }
}
