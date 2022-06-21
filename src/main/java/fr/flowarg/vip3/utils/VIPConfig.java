package fr.flowarg.vip3.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class VIPConfig {
    public static final ForgeConfigSpec CLIENT_SPECS;
    public static final Client CLIENT;

    public static final ForgeConfigSpec SERVER_SPECS;
    public static final Server SERVER;

    static {
        final Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPECS = clientPair.getRight();
        CLIENT = clientPair.getLeft();

        final Pair<Server, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPECS = serverPair.getRight();
        SERVER = serverPair.getLeft();
    }

    public static class Client {
        private final ForgeConfigSpec.BooleanValue firstLaunch;
        private final ForgeConfigSpec.BooleanValue enableRPC;
        private final ForgeConfigSpec.IntValue pauseMediaKey;
        private final ForgeConfigSpec.IntValue stopMediaKey;
        private final ForgeConfigSpec.IntValue skipMediaKey;

        private Client(@NotNull ForgeConfigSpec.Builder builder)
        {
            builder.comment(" Welcome to the VIP 3 Configuration !")
                    .push("client");

            this.firstLaunch = builder.comment(" Set to true if this is the first time you launch the client (will be set to false after the first launch).")
                    .define("firstLaunch", true);

            this.enableRPC = builder.comment(" Enable/Disable the VIP's Discord Rich Presence.")
                    .define("enableRPC", true);

            this.pauseMediaKey = builder.comment(" The key's scancode used to pause the background music.")
                    .defineInRange("pauseMediaKey", -1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            this.stopMediaKey = builder.comment(" The key's scancode used to stop the background music.")
                    .defineInRange("stopMediaKey", -1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            this.skipMediaKey = builder.comment(" The key's scancode used to skip the background music.")
                    .defineInRange("skipMediaKey", -1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop();
        }

        public ForgeConfigSpec.BooleanValue getFirstLaunch()
        {
            return this.firstLaunch;
        }

        public boolean getEnableRPC()
        {
            return this.enableRPC.get();
        }

        public ForgeConfigSpec.IntValue getPauseMediaKey()
        {
            return this.pauseMediaKey;
        }

        public ForgeConfigSpec.IntValue getStopMediaKey()
        {
            return this.stopMediaKey;
        }

        public ForgeConfigSpec.IntValue getSkipMediaKey()
        {
            return this.skipMediaKey;
        }
    }

    public static class Server {
        private final ForgeConfigSpec.BooleanValue enableBot;

        private Server(@NotNull ForgeConfigSpec.Builder builder)
        {
            builder.comment(" Welcome to the VIP 3 Configuration !")
                    .push("server");

            this.enableBot = builder.comment(" Enable/Disable the VIP's Discord Bot.")
                    .define("enableBot", false);

            builder.pop();
        }

        public ForgeConfigSpec.BooleanValue getEnableBot()
        {
            return this.enableBot;
        }
    }
}
