package fr.flowarg.vipium.server;

import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.server.commands.DelHomeCommand;
import fr.flowarg.vipium.server.commands.HomeCommand;
import fr.flowarg.vipium.server.commands.HomesCommand;
import fr.flowarg.vipium.server.commands.SetHomeCommand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.io.IOException;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerManager
{
    private final HomeCore homeCore;

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
    }

    public HomeCore getHomeCore()
    {
        return this.homeCore;
    }
}
