package fr.flowarg.vip3.features.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.flowarg.vip3.network.VChatClearPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ChatClearCommand
{
    static void registerChatClearCommand(@NotNull CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralArgumentBuilder<CommandSourceStack> suicide = Commands.literal("chatclear").executes(ChatClearCommand::runChatClearCommand);
        dispatcher.register(suicide);
    }

    private static int runChatClearCommand(@NotNull CommandContext<CommandSourceStack> ctx)
    {
        VNetwork.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)ctx.getSource().getEntity()), VChatClearPacket.INSTANCE);
        return 0;
    }
}
