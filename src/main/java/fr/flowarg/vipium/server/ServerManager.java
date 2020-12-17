package fr.flowarg.vipium.server;

import com.mojang.brigadier.CommandDispatcher;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.core.VIPException;
import fr.flowarg.vipium.server.commands.DelHomeCommand;
import fr.flowarg.vipium.server.commands.HomeCommand;
import fr.flowarg.vipium.server.commands.HomesCommand;
import fr.flowarg.vipium.server.commands.SetHomeCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.minecraft.command.CommandSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerManager implements EventListener
{
    private final HomeCore homeCore;
    private VoiceChannel channelStateDiscord;
    private JDA jda;
    private Guild guild;

    public ServerManager() throws VIPException
    {
        try
        {
            this.getLogger().info(VIPMod.MARKER, "Loading ServerManager...");
            this.homeCore = new HomeCore();
        }
        catch (IOException e)
        {
            throw new VIPException("An error as occurred on ServerManager initialization.", e);
        }
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event)
    {
        final CommandDispatcher<CommandSource> dispatcher = event.getCommandDispatcher();
        HomeCommand.register(dispatcher);
        HomesCommand.register(dispatcher);
        SetHomeCommand.register(dispatcher);
        DelHomeCommand.register(dispatcher);
        try
        {
            final File fileToken = new File("token");
            if(!fileToken.exists())
                return;
            
            final String token = loadFile(fileToken);
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
            e.printStackTrace();
        }
    }
    
    public static String loadFile(final File file) throws IOException
    {
        if (file.exists())
        {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuilder text = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null)
                text.append(line);
            
            reader.close();
            return text.toString();
        }
        return "";
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
                    final int hour = cal.get(Calendar.HOUR_OF_DAY);
                    final int minute = cal.get(Calendar.MINUTE);
 
                    if((hour == 19 && minute == 45) || (hour == 12 && minute == 45))
                    {
                        newsChannel.sendTyping().queue();
                        newsChannel.sendMessage(new MessageBuilder().append("Il est l'heure de manger ! https://tenor.com/view/yoshino-yoshino-himekawa-date-alive-anime-waifu-gif-17503754 ").build()).queue();
                    }
                }
            }, 10000, 10000);
        }
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
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        final TextChannel joinLeaveChannel = this.guild.getTextChannelById(787282709512060939L);
        joinLeaveChannel.sendTyping().queue();
        joinLeaveChannel.sendMessage(new MessageBuilder().append(event.getPlayer().getGameProfile().getName() + " s'est connecté !").build()).queue();
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event)
    {
        final TextChannel joinLeaveChannel = this.guild.getTextChannelById(787282709512060939L);
        joinLeaveChannel.sendTyping().queue();
        joinLeaveChannel.sendMessage(new MessageBuilder().append(event.getPlayer().getGameProfile().getName() + " s'est déconnecté !").build()).queue();
    }

    public HomeCore getHomeCore()
    {
        return this.homeCore;
    }
    
    public Logger getLogger()
    {
        return VIPMod.LOGGER;
    }
}
