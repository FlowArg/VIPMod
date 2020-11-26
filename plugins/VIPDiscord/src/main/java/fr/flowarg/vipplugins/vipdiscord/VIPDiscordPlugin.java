package fr.flowarg.vipplugins.vipdiscord;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import fr.flowarg.flowio.FileUtils;
import fr.flowarg.pluginloaderapi.plugin.Plugin;
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
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class VIPDiscordPlugin extends Plugin implements EventListener
{
    private VoiceChannel channelStateDiscord;

    @Override
    public void onStart()
    {
        this.getLogger().info("Starting " + this.getPluginName() + ".");
        try
        {
            final String token = FileUtils.loadFile(new File(this.getDataPluginFolder(), "token"));
            final JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            final CommandClientBuilder clientBuilder = new CommandClientBuilder();

            clientBuilder.setPrefix("!");
            clientBuilder.setOwnerId("406148304448126976");
            clientBuilder.setStatus(OnlineStatus.ONLINE);
            clientBuilder.setActivity(Activity.playing("VIP Server"));

            jdaBuilder.disableCache(CacheFlag.EMOTE);
            jdaBuilder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_BANS, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_INVITES);
            jdaBuilder.addEventListeners(clientBuilder.build(), new EventWaiter());
            this.getLogger().info("Connecting to Discord API...");
            final JDA jda = jdaBuilder.build();
            this.getLogger().info("Connected !");
            jda.awaitReady();
            this.channelStateDiscord = jda.getGuildById(657473306072580096L).getVoiceChannelById(667009869840252929L);
            this.getLogger().info("Finished loading !");
        } catch (IOException | InterruptedException | LoginException e)
        {
            this.getLogger().printStackTrace("Cannot start bot.", e);
        }
    }

    @Override
    public void onStop()
    {
        this.getLogger().info("Stopping " + this.getPluginName() + ".");
        if(this.channelStateDiscord != null)
        {
            this.channelStateDiscord.getManager().queue();
            this.channelStateDiscord.getManager().setName("SERVEUR - FERMÉ").queue();
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event)
    {
        if(event instanceof GuildReadyEvent)
        {
            this.channelStateDiscord.getManager().queue();
            this.channelStateDiscord.getManager().setName("SERVEUR - OUVERT").queue();
        }
    }
}
