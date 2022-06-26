package fr.flowarg.vip3.features.commands;

import net.minecraftforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

public class VCommands
{
    public static void registerCommands(@NotNull RegisterCommandsEvent event)
    {
        final var dispatcher = event.getDispatcher();
        DontExecuteThisCommand.registerDontExecuteThisCommand(dispatcher);
        FTGCommand.registerFTGCommand(dispatcher);
        SuicideCommand.registerSuicideCommand(dispatcher);
    }
}
