package fr.flowarg.vipium.server;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerException extends Exception
{
    public ServerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
