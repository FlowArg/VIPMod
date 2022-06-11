package fr.flowarg.vip3.client.ass;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import fr.flowarg.vip3.VIP3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ASSConfig
{
    public static final ASSConfig CONFIG = new ASSConfig();

    private final List<String> menuMusic = new ArrayList<>();
    private final List<String> gameMusic = new ArrayList<>();
    private final Path configPath = Path.of("config/ass.json");

    private ASSConfig()
    {
        VIP3.LOGGER.info("Initializing ASS config...");
        this.load();
        this.registerWatchService();
    }

    private void registerWatchService()
    {
        try
        {
            final WatchService service = this.configPath.getFileSystem().newWatchService();
            this.configPath.getParent().register(service, StandardWatchEventKinds.ENTRY_MODIFY);

            new Thread(() -> {
                try {
                    WatchKey key;
                    while ((key = service.take()) != null)
                    {
                        for (WatchEvent<?> event : key.pollEvents())
                        {
                            if(event.context().toString().equals("ass.json"))
                                this.load();
                        }
                        key.reset();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load()
    {
        VIP3.LOGGER.info("Reloading ASS config");

        ASSEngine.SOUND_MUSIC_MAP.clear();
        ASSEngine.MUSIC_MAP.clear();

        if(Files.notExists(this.configPath))
        {
            this.menuMusic.clear();
            this.gameMusic.clear();
            this.menuMusic.add("MC_DEFAULT");
            this.gameMusic.add("MC_DEFAULT");
            this.save();
            return;
        }

        try
        {
            final JsonObject json = JsonParser.parseString(Files.readString(this.configPath)).getAsJsonObject();
            final JsonArray menu = json.getAsJsonArray("menu");
            final JsonArray game = json.getAsJsonArray("game");

            this.menuMusic.clear();
            menu.forEach(jsonElement -> this.menuMusic.add(jsonElement.getAsString()));
            this.gameMusic.clear();
            game.forEach(jsonElement -> this.gameMusic.add(jsonElement.getAsString()));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private @NotNull JsonObject serialize()
    {
        final JsonObject json = new JsonObject();
        final JsonArray menu = new JsonArray();
        this.menuMusic.forEach(menu::add);
        final JsonArray game = new JsonArray();
        this.gameMusic.forEach(game::add);
        json.add("menu", menu);
        json.add("game", game);
        return json;
    }

    public void save()
    {
        try {
            final StringWriter stringWriter = new StringWriter();
            final JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setLenient(true);
            jsonWriter.setIndent("  ");
            Streams.write(this.serialize(), jsonWriter);
            final var jsonString = stringWriter.toString();

            if(Files.notExists(this.configPath))
                Files.createFile(this.configPath);
            Files.writeString(this.configPath, jsonString);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<String> getGameMusic()
    {
        return this.gameMusic;
    }

    public List<String> getMenuMusic()
    {
        return this.menuMusic;
    }
}
