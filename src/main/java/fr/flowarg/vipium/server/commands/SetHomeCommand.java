package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.server.Home;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

public class SetHomeCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("sethome").then(Commands.argument("homeName", MessageArgument.message()).executes(context -> {
            final ServerPlayerEntity player = context.getSource().asPlayer();
            if(VIPMod.serverManager.getHomeCore().addHome(player.getName().getFormattedText(), new Home(MessageArgument.getMessage(context, "homeName").getFormattedText(), player.dimension.getId(), player.serverPosX, player.serverPosY, player.serverPosZ, player.rotationYaw, player.rotationPitch)) != 0)
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.error"));
                return 1;
            }
            context.getSource().sendFeedback(new TranslationTextComponent("commands.home.successset"), true);
            return 0;
        })));
    }
}
