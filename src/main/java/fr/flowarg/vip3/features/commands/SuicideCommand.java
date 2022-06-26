package fr.flowarg.vip3.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class SuicideCommand
{
    static void registerSuicideCommand(@NotNull CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> suicide = Commands.literal("suicide").executes(SuicideCommand::runSuicideCommand);
        dispatcher.register(suicide);
    }

    private static int runSuicideCommand(@NotNull CommandContext<CommandSourceStack> ctx)
    {
        final var source = ctx.getSource();
        final var entity = source.getEntity();

        if(!(entity instanceof Player player))
            return -1;

        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), VObjects.SUICIDE_SOUND_EVENT.get(), SoundSource.PLAYERS, 10.0f, 1.0f);
        player.kill();

        source.sendSuccess(new TextComponent("Je suis un homme en colère !"), true);
        return 0;
    }
}
