package fr.flowarg.vip3.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class VIPConfig
{
    public static final ForgeConfigSpec CLIENT_SPECS;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPECS = clientPair.getRight();
        CLIENT = clientPair.getLeft();
    }

    public static class Client
    {
        private final ForgeConfigSpec.BooleanValue enableRPC;

        public Client(@NotNull ForgeConfigSpec.Builder builder) {
            builder.comment(" Welcome to the VIP 3 Configuration !")
                    .push("client");

            this.enableRPC = builder.comment(" Enable/Disable the VIP's Discord Rich Presence.")
                    .define("enableRPC", true);

            builder.pop();
        }

        public ForgeConfigSpec.BooleanValue getEnableRPC()
        {
            return this.enableRPC;
        }
    }
}
