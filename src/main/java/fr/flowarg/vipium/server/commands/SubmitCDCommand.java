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
public class SubmitCDCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("submitcd").then(Commands.argument("musicName", MessageArgument.message()).executes(context -> {
            try
            {
                final CommandSource source = context.getSource();
                VIPMod.serverManager.getSubmitCore().submitMusic(MessageArgument.getMessage(context, "musicName").getFormattedText(), source.getName());
                source.sendFeedback(new TranslationTextComponent("commands.submit.success"), false);
                return 0;
            } catch (Exception e)
            {
                VIPMod.LOGGER.catching(e);
                return 1;
            }
        })));
    }
}
