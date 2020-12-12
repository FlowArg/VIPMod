package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.server.Home;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.DEDICATED_SERVER)
public class SetHomeCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("sethome").then(Commands.argument("homeName", MessageArgument.message()).executes(context -> {
            final ServerPlayerEntity player = context.getSource().asPlayer();

            if(isDimFull(player.dimension.getId(), VIPMod.serverManager.getHomeCore().listHomes(context.getSource().getName())))
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.toomanyhomes"));
                return 1;
            }

            if(VIPMod.serverManager.getHomeCore().addHome(context.getSource().getName(), new Home(MessageArgument.getMessage(context, "homeName").getFormattedText(), player.dimension.getId(), player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, player.rotationPitch)) != 0)
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.error"));
                return 1;
            }
            context.getSource().sendFeedback(new TranslationTextComponent("commands.home.successset"), true);
            return 0;
        })));
    }

    public static boolean isDimFull(int dim, List<Home> homes)
    {
        short temp = 0;
        for (Home home : homes)
        {
            if(home.getDimension() == dim)
                temp++;
        }
        return temp >= 3;
    }
}
