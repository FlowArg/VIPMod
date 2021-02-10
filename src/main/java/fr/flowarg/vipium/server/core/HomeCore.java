package fr.flowarg.vipium.server.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.flowarg.vipium.server.Home;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.DEDICATED_SERVER)
public class HomeCore implements IStringUser
{
    /** We are 6 players on server. A file is okay for our usage. A database is recommended for a lot of players */
    private final File homeFiles =  new File(".", "homes.json");

    public HomeCore() throws IOException
    {
        this.homeFiles.getParentFile().mkdirs();
        if(!this.homeFiles.exists()) this.homeFiles.createNewFile();
    }

    public List<Home> listHomes(String playerName)
    {
        final List<Home> result = new ArrayList<>();
        try
        {
            final JsonObject json = new JsonParser().parse(this.toString(Files.readAllLines(this.homeFiles.toPath(), StandardCharsets.UTF_8))).getAsJsonObject();
            final JsonObject homes = json.getAsJsonObject("homes");
            final JsonArray parsedHomes = homes.getAsJsonArray(playerName);
            parsedHomes.forEach(homeElement -> {
                final JsonObject homeObj = homeElement.getAsJsonObject();
                final Home home = new Home(homeObj.get("name").getAsString(), homeObj.get("dimension").getAsInt(), homeObj.get("x").getAsDouble(), homeObj.get("y").getAsDouble(), homeObj.get("z").getAsDouble(), homeObj.get("yaw").getAsFloat(), homeObj.get("pitch").getAsFloat());
                result.add(home);
            });
            return result;
        }
        catch (Exception e)
        {
            return result;
        }
    }

    public Home getHome(String playerName, String homeName, int dim)
    {
        Home result = null;

        for (Home home : listHomes(playerName))
        {
            if(home.getName().equalsIgnoreCase(homeName) && home.getDimension() == dim)
            {
                result = home;
                break;
            }
        }

        return result;
    }

    public int addHome(String playerName, Home toAddHome)
    {
        try
        {
            if(this.getHome(playerName, toAddHome.getName(), toAddHome.getDimension()) != null)
                return 1;

            final String content = this.toString(Files.readAllLines(this.homeFiles.toPath(), StandardCharsets.UTF_8));
            final JsonObject json;
            final JsonObject homes;
            if(content.isEmpty())
            {
                json = new JsonObject();
                json.add("homes", homes = new JsonObject());
            }
            else
            {
                json = new JsonParser().parse(content).getAsJsonObject();
                homes = json.getAsJsonObject("homes");
            }

            final JsonArray playerHomes;

            if(homes.has(playerName)) playerHomes = homes.getAsJsonArray(playerName);
            else playerHomes = new JsonArray();

            final JsonObject home = this.transformHomeToJson(toAddHome);
            playerHomes.add(home);

            if(!homes.has(playerName))
                homes.add(playerName, playerHomes);

            Files.write(this.homeFiles.toPath(), Collections.singleton(json.toString()), StandardCharsets.UTF_8);
            return 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 1;
        }
    }

    public int removeHome(String playerName, String homeName)
    {
        try
        {
            final Home home = this.getHome(playerName, homeName, 0) == null ? (this.getHome(playerName, homeName, 1) == null ? this.getHome(playerName, homeName, -1) : this.getHome(playerName, homeName, 1)) : this.getHome(playerName, homeName, 0);
            if(home == null)
                return 1;
            final JsonObject json = new JsonParser().parse(this.toString(Files.readAllLines(this.homeFiles.toPath(), StandardCharsets.UTF_8))).getAsJsonObject();
            final JsonObject homes = json.getAsJsonObject("homes");
            if(!homes.has(playerName))
                return 1;
            final JsonArray playerHomes = homes.getAsJsonArray(playerName);
            JsonObject toRemove = null;
            for(JsonElement jsonElement : playerHomes)
            {
                final JsonObject iterated = jsonElement.getAsJsonObject();
                if(iterated.get("name").getAsString().equalsIgnoreCase(homeName))
                {
                    toRemove = iterated;
                    break;
                }
            }
            playerHomes.remove(toRemove);
            Files.write(this.homeFiles.toPath(), Collections.singleton(json.toString()), StandardCharsets.UTF_8);
            return 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 1;
        }
    }

    private JsonObject transformHomeToJson(Home home)
    {
        final JsonObject result = new JsonObject();
        result.addProperty("name", home.getName());
        result.addProperty("dimension", home.getDimension());
        result.addProperty("x", home.getX());
        result.addProperty("y", home.getY());
        result.addProperty("z", home.getZ());
        result.addProperty("yaw", home.getYaw());
        result.addProperty("pitch", home.getPitch());
        return result;
    }
}
