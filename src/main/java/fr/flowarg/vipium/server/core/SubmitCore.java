package fr.flowarg.vipium.server.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

public class SubmitCore implements IStringUser
{
    /** We are 6 players on server. A file is okay for our usage. A database is recommended for a lot of players */
    private final File submitDir =  new File("submit/");

    public void submitMusic(String music, String playerName) throws Exception
    {
        this.submitDir.mkdirs();
        final File playerFile = new File(this.submitDir, playerName + ".json");
        if(!playerFile.exists())
        {
            playerFile.createNewFile();
            Files.write(playerFile.toPath(), Collections.singleton(String.format("[\"%s\"]", music)), StandardCharsets.UTF_8);
        }
        else
        {
            final String json = this.toString(Files.readAllLines(playerFile.toPath()));
            Files.write(playerFile.toPath(), Collections.singleton(json.split("]")[0] += String.format(", \"%s\"", music) + ']'), StandardCharsets.UTF_8);
        }
    }

    public String getSubmittedByPlayer(String playerName)
    {
        this.submitDir.mkdirs();
        final File playerFile = new File(this.submitDir, playerName + ".json");
        if(!playerFile.exists()) return "No submit was sent.";
        else
        {
            try
            {
                return this.toString(Files.readAllLines(playerFile.toPath(), StandardCharsets.UTF_8));
            } catch (IOException e)
            {
                return e.getMessage();
            }
        }
    }
}
