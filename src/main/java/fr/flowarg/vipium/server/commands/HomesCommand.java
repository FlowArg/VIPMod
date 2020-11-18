package fr.flowarg.vipium.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.server.Home;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.DEDICATED_SERVER)
public class HomesCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal("homes").executes(context -> {
            final List<Home> homes = VIPMod.serverManager.getHomeCore().listHomes(context.getSource().getName());
            if(homes.isEmpty())
            {
                context.getSource().sendErrorMessage(new TranslationTextComponent("commands.home.nohomeavailable"));
                return 1;
            }
            homes.forEach(home -> {
                int length;
                final String nameMsg = "Home: " + home.getName();
                final String idMsg = "DimensionID: " + home.getDimension();
                final String xMsg = "X: " + home.getX();
                final String yMsg = "Y: " + home.getY();
                final String zMsg = "Z: " + home.getZ();
                final String yawMsg = "Yaw: " + home.getYaw();
                final String pitchMsg = "Pitch: " + home.getPitch();

                length = nameMsg.length();
                length = Math.max(length, idMsg.length());
                length = Math.max(length, xMsg.length());
                length = Math.max(length, yMsg.length());
                length = Math.max(length, zMsg.length());
                length = Math.max(length, yawMsg.length());
                length = Math.max(length, pitchMsg.length());

                context.getSource().sendFeedback(new StringTextComponent(nameMsg), true);
                context.getSource().sendFeedback(new StringTextComponent(idMsg), true);
                context.getSource().sendFeedback(new StringTextComponent(xMsg), true);
                context.getSource().sendFeedback(new StringTextComponent(yMsg), true);
                context.getSource().sendFeedback(new StringTextComponent(zMsg), true);
                context.getSource().sendFeedback(new StringTextComponent(yawMsg), true);
                context.getSource().sendFeedback(new StringTextComponent(pitchMsg), true);

                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < Math.floor(length / 1.5); i++)
                    builder.append('-');
                context.getSource().sendFeedback(new StringTextComponent(builder.toString()), true);
            });
            return 0;
        }));
    }
}
