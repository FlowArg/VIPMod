package fr.flowarg.vipium.server;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.server.commands.DelHomeCommand;
import fr.flowarg.vipium.server.commands.HomeCommand;
import fr.flowarg.vipium.server.commands.HomesCommand;
import fr.flowarg.vipium.server.commands.SetHomeCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.*;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerManager implements EventListener
{
    private final HomeCore homeCore;
    private VoiceChannel channelStateDiscord;
    private JDA jda;

    public ServerManager() throws ServerException
    {
        try
        {
            VIPMod.LOGGER.info(VIPMod.MARKER, "Loading ServerManager...");
            this.homeCore = new HomeCore();
        }
        catch (IOException e)
        {
            throw new ServerException("An error as occurred on ServerManager initialization.", e);
        }
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStartingEvent event)
    {
        HomeCommand.register(event.getCommandDispatcher());
        HomesCommand.register(event.getCommandDispatcher());
        SetHomeCommand.register(event.getCommandDispatcher());
        DelHomeCommand.register(event.getCommandDispatcher());
        try
        {
            final File fileToken = new File("token");
            if(!fileToken.exists())
                return;
            
            final String token = loadFile(fileToken);
            final JDABuilder jdaBuilder = JDABuilder.createDefault(token);

            jdaBuilder.setStatus(OnlineStatus.ONLINE);
            jdaBuilder.setActivity(Activity.playing("VIP Server"));

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
            if(this.channelStateDiscord == null)
                this.channelStateDiscord = this.jda.getGuildById(657473306072580096L).getVoiceChannelById(667009869840252929L);
            this.channelStateDiscord.getManager().queue();
            this.channelStateDiscord.getManager().setName("SERVEUR - OUVERT").queue();
        }
    }
    
    @SubscribeEvent
    public void onServerStop(FMLServerStoppingEvent event)
    {
        if(this.channelStateDiscord == null)
            this.channelStateDiscord = this.jda.getGuildById(657473306072580096L).getVoiceChannelById(667009869840252929L);
        this.channelStateDiscord.getManager().queue();
        this.channelStateDiscord.getManager().setName("SERVEUR - FERMÉ").queue();
        this.jda.shutdown();
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
