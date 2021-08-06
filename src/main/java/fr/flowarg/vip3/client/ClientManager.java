package fr.flowarg.vip3.client;

import net.minecraftforge.common.MinecraftForge;

public class ClientManager
{
    public ClientManager()
    {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
