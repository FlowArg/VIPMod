package fr.flowarg.vipium.server.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class SubmitCore implements IStringUser
{
    /** We are 6 players on server. A file is okay for our usage. A database is recommended for a lot of players */
    private final File submitFiles =  new File(".", "submit.json");

    public SubmitCore() throws IOException
    {
        this.submitFiles.getParentFile().mkdirs();
        if(!this.submitFiles.exists())
        {
            this.submitFiles.createNewFile();
            Files.write(this.submitFiles.toPath(), Collections.singleton("[]"), StandardCharsets.UTF_8);
        }
    }

    public SubmitResult submitMusic(String music, String playerName) throws Exception
    {
        final JsonArray array = new JsonParser().parse(this.toString(Files.readAllLines(this.submitFiles.toPath(), StandardCharsets.UTF_8))).getAsJsonArray();
        final AtomicBoolean musicExist = new AtomicBoolean(false);
        array.forEach(baseElem -> {
            final JsonObject obj = baseElem.getAsJsonObject();
            JsonObject playerObj = obj.getAsJsonObject(playerName);
            if(playerObj == null)
            {
                playerObj = new JsonObject();
                playerObj.add("submitted", new JsonArray());
                obj.add(playerName, playerObj);
            }

            final JsonArray content = playerObj.getAsJsonArray("submitted");
            content.forEach(baseContentElem -> {
                if(baseContentElem.isJsonPrimitive())
                {
                    final JsonPrimitive primitive = baseContentElem.getAsJsonPrimitive();
                    if(primitive.isString())
                    {
                        final String musicName = primitive.getAsString();
                        if(musicName.equalsIgnoreCase(music))
                            musicExist.set(true);
                    }
                }
            });
            if(!musicExist.get())
            {
                try
                {
                    content.add(music);
                    Files.write(this.submitFiles.toPath(), Collections.singleton(array.toString()), StandardCharsets.UTF_8);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return musicExist.get() ? SubmitResult.SUBMIT_EXIST : SubmitResult.SUCCESS;
    }

    public String getSubmittedByPlayer(String playerName) throws Exception
    {
        final JsonArray array = new JsonParser().parse(this.toString(Files.readAllLines(this.submitFiles.toPath(), StandardCharsets.UTF_8))).getAsJsonArray();
        final StringBuilder sb = new StringBuilder("[");
        array.forEach(baseElem -> {
            final JsonObject obj = baseElem.getAsJsonObject();
            final JsonObject playerObj = obj.getAsJsonObject(playerName);
            if(playerObj == null) return;

            final JsonArray content = playerObj.getAsJsonArray("submitted");
            content.forEach(baseContentElem -> {
                if(baseContentElem.isJsonPrimitive())
                {
                    final JsonPrimitive primitive = baseContentElem.getAsJsonPrimitive();
                    if(primitive.isString()) sb.append(primitive.getAsString());
                }
            });
        });
        return sb.toString();
    }

    public enum SubmitResult
    {
        SUCCESS,
        SUBMIT_EXIST
    }
}
