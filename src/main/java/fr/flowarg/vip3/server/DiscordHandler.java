package fr.flowarg.vip3.server;

import fr.flowarg.vip3.VIP3;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class DiscordHandler extends ListenerAdapter
{
    private MinecraftServer server;
    private JDA jda;
    private Guild guild;
    private boolean started;
    private VoiceChannel channelStateDiscord;
    private final Path messagePath = Paths.get("lastmessage.txt");
    private final List<DHMTimestamp> eatTimeStamps = new ArrayList<>();

    @SubscribeEvent
    public void onServerStart(@NotNull FMLServerStartingEvent event)
    {
        this.server = event.getServer();

        try
        {
            final var fileToken = Paths.get("token.txt");
            if(Files.notExists(fileToken))
            {
                VIP3.LOGGER.error("The file {} doesn't exist", fileToken.toAbsolutePath().toString());
                return;
            }

            final var token = Files.readAllLines(fileToken, StandardCharsets.UTF_8).get(0);
            final var builder = JDABuilder.createDefault(token);

            builder.setStatus(OnlineStatus.ONLINE);
            builder.setActivity(Activity.playing("VIP 3 Server"));

            builder.disableCache(CacheFlag.EMOTE);
            builder.disableIntents(GatewayIntent.GUILD_PRESENCES,
                                   GatewayIntent.DIRECT_MESSAGES,
                                   GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                                   GatewayIntent.DIRECT_MESSAGE_TYPING,
                                   GatewayIntent.GUILD_BANS,
                                   GatewayIntent.GUILD_EMOJIS,
                                   GatewayIntent.GUILD_INVITES);
            VIP3.LOGGER.info("Connecting to Discord API...");
            this.jda = builder.build();
            this.jda.addEventListener(this);
            VIP3.LOGGER.info("Connected !");
        }
        catch (Exception e)
        {
            VIP3.LOGGER.catching(e);
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event)
    {
        if(this.started) return;

        this.initNullableVars();

        final var newsChannel = this.guild.getTextChannelById(877627199992242248L);
        assert newsChannel != null;
        final Message message = new MessageBuilder()
                .setActionRows(ActionRow.of(Button.danger("stop", "Éteint le serveur")),
                               ActionRow.of(Button.link("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Site web VIP 3")))
                .allowMentions(Message.MentionType.ROLE)
                .append("Le serveur est ouvert ! https://tenor.com/view/date-alive-yoshinon-patting-yoshino-gif-12018889 ")
                .build();

        try
        {
            if(Files.notExists(this.messagePath))
            {
                newsChannel.sendTyping().queue(unused -> newsChannel.sendMessage(message).queue(message1 -> newsChannel.getManager().queue(unused1 -> {
                    try
                    {
                        Files.createFile(this.messagePath);
                        Files.writeString(this.messagePath, message1.getId(), StandardCharsets.UTF_8);
                    } catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                })));
            }
            else newsChannel.editMessageById(Files.readString(this.messagePath), message).queue();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        final var timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                final var cal = Calendar.getInstance();
                final var day = cal.get(Calendar.DAY_OF_MONTH);
                final var hour = cal.get(Calendar.HOUR_OF_DAY);
                final var minute = cal.get(Calendar.MINUTE);

                if((hour == 19 - 2 && minute == 45) || (hour == 12 - 2 && minute == 45))
                {
                    final var timestamp = new DHMTimestamp(day, hour, minute);
                    if(!DiscordHandler.this.eatTimeStamps.contains(timestamp))
                    {
                        DiscordHandler.this.eatTimeStamps.add(timestamp);
                        newsChannel.sendTyping().queue(unused -> newsChannel.sendMessage(
                                new MessageBuilder()
                                        .append("Il est l'heure de manger ! https://tenor.com/view/yoshino-yoshino-himekawa-date-alive-anime-waifu-gif-17503754 ")
                                        .build()).queue());
                    }
                }
            }
        }, 10000, 10000);

        VIP3.LOGGER.info("Started task for Timezone {}: {}.", TimeZone.getDefault().getDisplayName(), new SimpleDateFormat("hh:mm:ss").format(new Date()));

        this.started = true;
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event)
    {
        if(!event.getComponentId().equals("stop")) return;
        if(event.getButton().isDisabled()) return;
        if (this.server == null) return;
        if(this.server.getPlayerCount() != 0) return;

        event.editButton(Button.danger("stop", "Stoppé!").asDisabled()).queue(unused -> this.server.stopServer());
    }

    @SubscribeEvent
    public void onServerStop(FMLServerStoppingEvent event)
    {
        if(!this.started) return;
        this.initNullableVars();

        try
        {
            final var newsChannel = this.guild.getTextChannelById(877627199992242248L);
            assert newsChannel != null;

            newsChannel.editMessageById(Files.readString(this.messagePath), new MessageBuilder()
                            .setActionRows(ActionRow.of(Button.danger("stop", "Éteint le serveur").asDisabled()),
                                           ActionRow.of(Button.link("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "Site web VIP 3")))
                            .allowMentions(Message.MentionType.ROLE)
                            .append("Le serveur est fermé ! https://tenor.com/view/date-alive-yoshinon-patting-yoshino-gif-12018889 ")
                            .build())
                    .queue(message -> {
                        this.jda.shutdownNow();
                        this.eatTimeStamps.clear();
                        this.started = false;
                    });
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        this.onPlayerIO(event, false);
    }

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event)
    {
        this.onPlayerIO(event, true);
    }

    private void onPlayerIO(@NotNull PlayerEvent event, boolean out)
    {
        if(!this.started) return;
        final var joinLeaveChannel = this.guild.getTextChannelById(787282709512060939L);
        assert joinLeaveChannel != null;
        joinLeaveChannel.sendTyping().queue(unused -> joinLeaveChannel.sendMessage(
                        new MessageBuilder()
                                .append(event.getPlayer().getGameProfile().getName())
                                .append(" s'est ")
                                .append(out ? "dé" : "")
                                .append("connecté !")
                                .build())
                .queue());
    }

    private void initNullableVars()
    {
        if(this.guild == null && this.jda != null)
            this.guild = this.jda.getGuildById(657473306072580096L);

        if(this.channelStateDiscord == null && this.guild != null)
            this.channelStateDiscord = this.guild.getVoiceChannelById(667009869840252929L);
    }
}
