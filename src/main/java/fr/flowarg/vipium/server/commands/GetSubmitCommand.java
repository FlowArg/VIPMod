package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.util.text.StringTextComponent;

public class GetSubmitCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("getsubmit").then(Commands.argument("playerName", GameProfileArgument.gameProfile()).executes(context -> {
            final CommandSource source = context.getSource();
            GameProfileArgument.getGameProfiles(context, "playerName").forEach(gameProfile -> {
                try
                {
                    source.sendFeedback(new StringTextComponent("Actual submits for " + gameProfile.getName() + ": " + VIPMod.serverManager.getSubmitCore().getSubmittedByPlayer(gameProfile.getName())), false);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
            return 0;
        })));
    }
}
