package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.server.Home;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class HomeCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("home").then(Commands.argument("homeName", MessageArgument.message()).executes(context -> {
            final Home selected = Main.serverManager.getHomeCore().getHome(context.getSource().getName(), MessageArgument.getMessage(context, "homeName").getFormattedText());
            if(selected == null)
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.nohomeavailable"));
                return 0;
            }

            final ServerPlayerEntity player = context.getSource().asPlayer();
            if(player.dimension.getId() == selected.getDimension())
                player.teleport(player.getServerWorld(), selected.getX(), selected.getY(), selected.getZ(), selected.getYaw(), selected.getPitch());
            else context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.baddimension"));
            return 1;
        })));
    }
}
