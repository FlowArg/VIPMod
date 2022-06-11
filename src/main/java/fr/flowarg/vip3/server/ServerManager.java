package fr.flowarg.vip3.server;

import fr.flowarg.vip3.utils.SidedManager;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ServerManager implements SidedManager
{
    @Override
    public void init()
    {
        if(FMLEnvironment.production && VIPConfig.SERVER.getEnableBot().get())
            MinecraftForge.EVENT_BUS.register(new DiscordHandler());
    }
}
