package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.server.core.SubmitCore.SubmitResult;
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
        dispatcher.register(Commands.literal("sumbitcd").then(Commands.argument("musicName", MessageArgument.message()).executes(context -> {
            try
            {
                final CommandSource source = context.getSource();
                final SubmitResult result = VIPMod.serverManager.getSubmitCore().submitMusic(MessageArgument.getMessage(context, "musicName").getFormattedText(), source.getName());
                switch (result)
                {
                    case SUBMIT_EXIST:
                        source.sendErrorMessage(new TranslationTextComponent("commands.submit.exist"));
                        return 1;
                    case SUCCESS:
                        source.sendFeedback(new TranslationTextComponent("commands.submit.success"), false);
                        return 0;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                return 1;
            }
            return 0;
        })));
    }
}
