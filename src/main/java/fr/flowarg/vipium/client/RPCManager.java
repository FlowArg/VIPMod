package fr.flowarg.vipium.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.flowarg.vipium.VIPMod;
import fr.flowarg.vipium.common.core.VipiumConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class RPCManager
{
    private final DiscordRPC rpcLib = DiscordRPC.INSTANCE;
    private final String applicationID = "668415343340814336";
    private final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private final DiscordRichPresence rpc = new DiscordRichPresence();
    private final Map<String, String> names = new HashMap<>();
    private boolean started = false;

    public RPCManager()
    {
        this.names.putIfAbsent("FlowArg", "flow");
        this.names.putIfAbsent("EngineTH3F", "engine");
        this.names.putIfAbsent("Kot0_", "kot0");
        this.names.putIfAbsent("MaxleProt86", "max");
        this.names.putIfAbsent("squelletton", "squelletton");
        this.names.putIfAbsent("SSHEeveeDirector", "sshdirector");
        VIPMod.LOGGER.info(VIPMod.MARKER, "Registered rich presences assets for application " + this.applicationID + ".");
    }

    public void makeChanges(RPCCallback callback)
    {
        if(this.started)
            callback.updateRPC(this.rpc);
    }

    boolean startRpc(Supplier<Minecraft> mc)
    {
        if (VipiumConfig.CLIENT.getEnableRPC().get() && !this.started)
        {
            this.getLogger().info(VIPMod.MARKER, "Starting Discord RPC for app " + this.applicationID);

            this.handlers.ready = (user) -> this.getLogger().info(String.format("Discord RPC loaded for %s#%s", user.username, user.discriminator));
            this.rpcLib.Discord_Initialize(this.applicationID, this.handlers, true, "");

            this.rpc.startTimestamp = System.currentTimeMillis() / 1000;
            this.rpc.largeImageText = "V.I.P Server";
            this.rpc.largeImageKey = "logo";
            this.rpc.details = "Loading game";
            this.rpc.state = "Waiting Minecraft";
            this.rpc.smallImageText = mc.get().getSession().getUsername();
            this.rpc.smallImageKey = this.names.getOrDefault(mc.get().getSession().getUsername(), "lambda");
            this.rpcLib.Discord_UpdatePresence(this.rpc);
            this.started = true;

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && VipiumConfig.CLIENT.getEnableRPC().get())
                {
                    try
                    {
                        if(mc.get().world != null && mc.get().getConnection() != null)
                        {
                            if(mc.get().getCurrentServerData() != null)
                            {
                                if(mc.get().getCurrentServerData().serverIP.equals("flowargbyfistin.francecentral.cloudapp.azure.com:25565"))
                                {
                                    VIPMod.clientManager.getRpcManager().makeChanges(rpc -> {
                                        rpc.details = "Playing in V.I.P";
                                        rpc.state = "Connected";
                                    });
                                }
                            }
                        }
                        this.rpcLib.Discord_UpdatePresence(rpc);
                        this.rpcLib.Discord_RunCallbacks();
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {}
                }
                this.started = false;
            }, "RPC-Callback-Handler").start();
            return true;
        }
        return false;
    }

    public Logger getLogger()
    {
        return VIPMod.LOGGER;
    }

    @FunctionalInterface
    public interface RPCCallback
    {
        void updateRPC(DiscordRichPresence rpc);
    }
}
