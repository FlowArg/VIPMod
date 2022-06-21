package fr.flowarg.vip3.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.utils.VIPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class RPCManager
{
    private final DiscordRPC rpcLib = DiscordRPC.INSTANCE;
    private final DiscordEventHandlers handlers = new DiscordEventHandlers();
    private final DiscordRichPresence rpc = new DiscordRichPresence();
    private final Map<String, String> names = Map.of("FlowArg", "flow", "EngineTH3F", "engine", "Kot0_", "kot0", "MaxleProt86", "max", "squelletton", "squelletton", "SSHFoxyDirector", "sshdirector", "novissou", "novissou");
    private boolean started = false;

    public void makeChanges(RPCCallback callback)
    {
        if(this.started)
            callback.updateRPC(this.rpc);
    }

    void startRpc(Minecraft mc)
    {
        if (!VIPConfig.CLIENT.getEnableRPC() || this.started) return;

        final var applicationID = "668415343340814336";
        VIP3.LOGGER.info("Starting Discord RPC for app " + applicationID);

        this.handlers.ready = (user) -> VIP3.LOGGER.info(String.format("Discord RPC loaded for %s#%s", user.username, user.discriminator));
        this.rpcLib.Discord_Initialize(applicationID, this.handlers, true, "");

        this.rpc.startTimestamp = System.currentTimeMillis() / 1000;
        this.rpc.largeImageText = "V.I.P Server";
        this.rpc.largeImageKey = "logo";
        this.rpc.details = "Loading game";
        this.rpc.state = "Waiting Minecraft";
        final var name = mc.getUser().getName();
        this.rpc.smallImageText = name;
        this.rpc.smallImageKey = this.names.getOrDefault(name, "ptdr t ki");
        this.rpcLib.Discord_UpdatePresence(this.rpc);
        this.started = true;

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && mc.isRunning() && this.started)
            {
                try
                {
                    if(mc.screen instanceof TitleScreen)
                    {
                        this.makeChanges(rpc -> {
                            rpc.details = "Sur le menu principal";
                            rpc.state = "Idling";
                        });
                    }
                    else if(mc.level != null && mc.getConnection() != null)
                    {
                        final var serverData = mc.getCurrentServer();
                        if(serverData != null)
                        {
                            if(serverData.ip.contains("flowarg"))
                            {
                                this.makeChanges(rpc -> {
                                    rpc.details = "Playing with friends";
                                    rpc.state = "VIP 3";
                                });
                            }
                        }
                    }
                    else if(mc.level != null && mc.getSingleplayerServer() != null)
                    {
                        this.makeChanges(rpc -> {
                            rpc.details = "Playing in Solo";
                            rpc.state = "VIP 3";
                        });
                    }
                    this.rpcLib.Discord_UpdatePresence(rpc);
                    this.rpcLib.Discord_RunCallbacks();
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
            this.rpcLib.Discord_Shutdown();
            this.started = false;
        }, "RPC-Callback-Handler").start();
    }

    @FunctionalInterface
    public interface RPCCallback
    {
        void updateRPC(DiscordRichPresence rpc);
    }
}
