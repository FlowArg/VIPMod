package fr.flowarg.vip3.features;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FTGCommand
{
    public static void registerCommands(@NotNull RegisterCommandsEvent event)
    {
        registerFTGCommand(event.getDispatcher());
    }

    public static void registerFTGCommand(@NotNull CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> ftgCommand = Commands.literal("ftg")
                .then(Commands.argument("player", EntityArgument.player())
                              .then(Commands.argument("reason", MessageArgument.message())
                                            .executes(FTGCommand::runFTGCommand)));
        dispatcher.register(ftgCommand);
    }

    private static int runFTGCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
    {
        final var player = EntityArgument.getPlayer(ctx, "player");
        final var message = MessageArgument.getMessage(ctx, "reason");

        player.connection.disconnect(message);
        final var text = new TextComponent(player.getDisplayName() + " a fermé la gueule de " + player.getDisplayName() + " pour la raison : ");
        text.append(message);
        ctx.getSource().sendSuccess(new TextComponent("Allez hop, ça dégage !"), true);
        ctx.getSource().getServer().sendMessage(text, UUID.randomUUID());
        return 0;
    }
}
