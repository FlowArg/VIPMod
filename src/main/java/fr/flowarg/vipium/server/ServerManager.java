package fr.flowarg.vipium.server;

import com.google.common.io.Files;
import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.core.VIPException;
import fr.flowarg.vipium.server.commands.*;
import fr.flowarg.vipium.server.core.HomeCore;
import fr.flowarg.vipium.server.core.SubmitCore;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerManager implements EventListener
{
    private final HomeCore homeCore;
    private final SubmitCore submitCore;
    private final List<DHMTimestamp> eatTimeStamps = new ArrayList<>();
    private VoiceChannel channelStateDiscord;
    private JDA jda;
    private Guild guild;
    private MinecraftServer server;
    private boolean started = false;

    public ServerManager() throws VIPException
    {
        try
        {
            this.getLogger().info(VIPMod.MARKER, "Loading ServerManager...");
            this.homeCore = new HomeCore();
            this.submitCore = new SubmitCore();
        }
        catch (IOException e)
        {
            throw new VIPException("An error as occurred on ServerManager initialization.", e);
        }
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event)
    {
        this.server = event.getServer();
        final CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();
        HomeCommand.register(dispatcher);
        HomesCommand.register(dispatcher);
        SetHomeCommand.register(dispatcher);
        DelHomeCommand.register(dispatcher);
        SubmitCDCommand.register(dispatcher);
        GetSubmitCommand.register(dispatcher);

        try
        {
            final File fileToken = new File("token");
            if(!fileToken.exists())
                return;
            
            final String token = Files.readFirstLine(fileToken, StandardCharsets.UTF_8);
            final JDABuilder jdaBuilder = JDABuilder.createDefault(token);

            jdaBuilder.setStatus(OnlineStatus.ONLINE);
            jdaBuilder.setActivity(Activity.playing("V.I.P Server"));

            jdaBuilder.disableCache(CacheFlag.EMOTE);
            jdaBuilder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES);
            this.getLogger().info(VIPMod.MARKER, "Connecting to Discord API...");
            this.jda = jdaBuilder.build();
            this.jda.addEventListener(this);
            this.getLogger().info(VIPMod.MARKER, "Connected !");
        } catch (IOException | LoginException e)
        {
            VIPMod.LOGGER.catching(e);
        }
    }
    
    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if(event instanceof GuildReadyEvent)
        {
            if(this.guild == null)
                this.guild = this.jda.getGuildById(657473306072580096L);
            if(this.channelStateDiscord == null)
                this.channelStateDiscord = this.guild.getVoiceChannelById(667009869840252929L);
            this.channelStateDiscord.getManager().queue();
            this.channelStateDiscord.getManager().setName("SERVEUR - OUVERT").queue();
            final TextChannel newsChannel = this.guild.getTextChannelById(657473712005578772L);
            newsChannel.sendTyping().queue();
            newsChannel.sendMessage(new MessageBuilder().allowMentions(Message.MentionType.ROLE).append("Le serveur est ouvert ! https://tenor.com/view/date-alive-yoshinon-patting-yoshino-gif-12018889 ").append(this.guild.getRoleById(658309459755532289L)).build()).queue();

            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run()
                {
                    final Calendar cal = Calendar.getInstance();
                    final int day = cal.get(Calendar.DAY_OF_MONTH);
                    final int hour = cal.get(Calendar.HOUR_OF_DAY);
                    final int minute = cal.get(Calendar.MINUTE);
 
                    if((hour == 19 && minute == 45) || (hour == 12 && minute == 45))
                    {
                        final DHMTimestamp timestamp = new DHMTimestamp(day, hour, minute);
                        if(!ServerManager.this.eatTimeStamps.contains(timestamp))
                        {
                            ServerManager.this.eatTimeStamps.add(timestamp);
                            newsChannel.sendTyping().queue();
                            newsChannel.sendMessage(new MessageBuilder().append("Il est l'heure de manger ! https://tenor.com/view/yoshino-yoshino-himekawa-date-alive-anime-waifu-gif-17503754 ").build()).queue();
                        }
                    }
                }
            }, 10000, 10000);
            this.getLogger().info(VIPMod.MARKER, "Started task for Timezone" + TimeZone.getDefault().getDisplayName() + String.format(" (%s) ", new SimpleDateFormat("hh:mm:ss").format(new Date())));
            this.started = true;
        }
        else if(this.started)
        {
            if(event instanceof MessageReceivedEvent)
                this.onMessageReceivedEvent((MessageReceivedEvent)event);
        }
    }

    private void onMessageReceivedEvent(MessageReceivedEvent event)
    {
        if(event.getMessage().getContentRaw().equalsIgnoreCase("!stopVIP"))
        {
            event.getChannel().sendTyping().complete();
            if (this.server != null)
            {
                if(this.server.getCurrentPlayerCount() == 0)
                {
                    event.getChannel().sendMessage(this.defaultEmbed(new Color(7, 170, 52), "You want to stop server and...", "Server is stopping !", event.getMember())).complete();
                    this.server.initiateShutdown(false);
                }
                else event.getChannel().sendMessage(this.defaultEmbed(new Color(234, 197, 6), "You want to stop server but...", "Server is not empty !", event.getMember())).complete();
            }
            else event.getChannel().sendMessage(this.defaultEmbed(new Color(175, 5, 5), "You want to stop server but...", "Server cannot stop !", event.getMember())).complete();
        }
    }

    private MessageEmbed defaultEmbed(Color color, String actionName, String actionValue, Member sender)
    {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        final User user = sender.getUser();
        embedBuilder.setColor(color);
        embedBuilder.addField(actionName, actionValue, false);
        embedBuilder.setTitle("Action from " + sender.getEffectiveName() + '(' + user.getName() + '#' + user.getDiscriminator() + ')');
        embedBuilder.setFooter("Created by FlowArg for V.I.P community!").build();
        return embedBuilder.build();
    }
    
    @SubscribeEvent
    public void onServerStop(FMLServerStoppingEvent event)
    {
        if(this.guild == null)
            this.guild = this.jda.getGuildById(657473306072580096L);
        if(this.channelStateDiscord == null)
            this.channelStateDiscord = this.guild.getVoiceChannelById(667009869840252929L);
        this.channelStateDiscord.getManager().queue();
        this.channelStateDiscord.getManager().setName("SERVEUR - FERMÉ").queue();
        final TextChannel newsChannel = this.guild.getTextChannelById(657473712005578772L);
        newsChannel.sendTyping().queue();
        newsChannel.sendMessage(new MessageBuilder().allowMentions(Message.MentionType.ROLE).append("Le serveur est fermé ! https://tenor.com/view/yoshino-hide-anime-frightened-date-alive-gif-3532023 ").append(this.guild.getRoleById(658309459755532289L)).build()).queue();
        this.jda.shutdown();
        this.eatTimeStamps.clear();
        this.started = false;
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        final TextChannel joinLeaveChannel = this.guild.getTextChannelById(787282709512060939L);
        joinLeaveChannel.sendTyping().queue();
        joinLeaveChannel.sendMessage(new MessageBuilder().append(event.getPlayer().getGameProfile().getName()).append(" s'est connecté !").build()).queue();
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event)
    {
        final TextChannel joinLeaveChannel = this.guild.getTextChannelById(787282709512060939L);
        joinLeaveChannel.sendTyping().queue();
        joinLeaveChannel.sendMessage(new MessageBuilder().append(event.getPlayer().getGameProfile().getName()).append(" s'est déconnecté !").build()).queue();
    }

    public HomeCore getHomeCore()
    {
        return this.homeCore;
    }

    public SubmitCore getSubmitCore()
    {
        return this.submitCore;
    }

    public Logger getLogger()
    {
        return VIPMod.LOGGER;
    }

    private static class DHMTimestamp
    {
        private final int day;
        private final int hour;
        private final int minute;

        public DHMTimestamp(int day, int hour, int minute)
        {
            this.day = day;
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;

            final DHMTimestamp dhmTimestamp = (DHMTimestamp)o;

            if (this.day != dhmTimestamp.day) return false;
            if (this.hour != dhmTimestamp.hour) return false;
            return this.minute == dhmTimestamp.minute;
        }

        @Override
        public int hashCode()
        {
            int result = this.day;
            result = 31 * result + this.hour;
            result = 31 * result + this.minute;
            return result;
        }
    }
}
