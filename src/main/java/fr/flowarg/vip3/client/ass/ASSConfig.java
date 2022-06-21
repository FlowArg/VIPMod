package fr.flowarg.vip3.client.ass;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import fr.flowarg.vip3.VIP3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.StringWriter;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class ASSConfig
{
    public static final String MINECRAFT_DEFAULT_CONFIG_KEY = "MC_DEFAULT";
    private static final ASSConfigSound MINECRAFT_DEFAULT_CONFIG = new ASSConfigSound(MINECRAFT_DEFAULT_CONFIG_KEY, 1.0F);

    public static final ASSConfig CONFIG = new ASSConfig();

    private final Map<String, ASSConfigSound> menuMusics = new ConcurrentHashMap<>();
    private final Map<String, ASSConfigSound> gameMusics = new ConcurrentHashMap<>();
    private final Path configPath = FMLPaths.CONFIGDIR.get().resolve("ass.json");

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
                            if(event.context().toString().equals("ass.json") &&
                                    Files.size(this.configPath) > 0)
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

        if(Files.notExists(this.configPath))
        {
            this.menuMusics.clear();
            this.gameMusics.clear();

            this.menuMusics.put(MINECRAFT_DEFAULT_CONFIG_KEY, MINECRAFT_DEFAULT_CONFIG);
            this.gameMusics.put(MINECRAFT_DEFAULT_CONFIG_KEY, MINECRAFT_DEFAULT_CONFIG);
            this.save();
            ASSConfigScreen.reload();
            return;
        }

        try
        {
            final JsonObject json = JsonParser.parseString(Files.readString(this.configPath)).getAsJsonObject();
            final JsonArray menu = json.getAsJsonArray("menu");
            final JsonArray game = json.getAsJsonArray("game");

            ASSEngine.SOUND_MUSIC_MAP.clear();
            ASSEngine.MUSIC_MAP.clear();
            this.menuMusics.clear();
            this.gameMusics.clear();

            final BiPredicate<String, Map<String, ASSConfigSound>> checkFileExisting = (name, list) -> {
                if(name.equals("MC_DEFAULT"))
                    return true;

                final var path = Path.of("vipsounds").resolve(name + ".ogg");
                if(Files.notExists(path))
                {
                    VIP3.LOGGER.warn("File " + path + " does not exist! Removing from config...");
                    return false;
                }
                return true;
            };

            menu.forEach(jsonElement -> {
                if(jsonElement instanceof JsonPrimitive)
                {
                    final var name = jsonElement.getAsString();
                    if(checkFileExisting.test(name, this.menuMusics))
                        this.menuMusics.put(name, new ASSConfigSound(name, 1.0F));
                    return;
                }

                final var volumeElement = jsonElement.getAsJsonObject().get("volume");
                final float volume = volumeElement instanceof JsonNull ? 1.0F : volumeElement.getAsFloat();

                final var name = jsonElement.getAsJsonObject().get("name").getAsString();
                if(checkFileExisting.test(name, this.menuMusics))
                    this.menuMusics.put(name, new ASSConfigSound(name, volume));
            });

            game.forEach(jsonElement -> {
                if(jsonElement instanceof JsonPrimitive)
                {
                    final var name = jsonElement.getAsString();
                    if(checkFileExisting.test(name, this.gameMusics))
                        this.gameMusics.put(name, new ASSConfigSound(name, 1.0F));
                    return;
                }

                final var volumeElement = jsonElement.getAsJsonObject().get("volume");
                final float volume = volumeElement instanceof JsonNull ? 1.0F : volumeElement.getAsFloat();

                final var name = jsonElement.getAsJsonObject().get("name").getAsString();
                if(checkFileExisting.test(name, this.gameMusics))
                    this.gameMusics.put(name, new ASSConfigSound(name, volume));
            });
            ASSConfigScreen.reload();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private @NotNull JsonObject serialize()
    {
        final JsonObject json = new JsonObject();
        final JsonArray menu = new JsonArray();
        this.menuMusics.forEach((key, assConfigSound) -> this.serializeSound(menu).accept(assConfigSound));
        final JsonArray game = new JsonArray();
        this.gameMusics.forEach((key, assConfigSound) -> this.serializeSound(game).accept(assConfigSound));
        json.add("menu", menu);
        json.add("game", game);
        return json;
    }

    private Consumer<ASSConfigSound> serializeSound(JsonArray array)
    {
        return assConfigSound -> {
            if(assConfigSound.volume() == 1.0f)
            {
                array.add(assConfigSound.name());
                return;
            }

            final JsonObject object = new JsonObject();
            object.addProperty("name", assConfigSound.name());
            object.addProperty("volume", assConfigSound.volume());
            array.add(object);
        };
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

    public Map<String, ASSConfigSound> getGameMusics()
    {
        return this.gameMusics;
    }

    public Map<String, ASSConfigSound> getMenuMusics()
    {
        return this.menuMusics;
    }
}
