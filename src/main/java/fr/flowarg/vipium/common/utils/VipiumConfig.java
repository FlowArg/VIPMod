package fr.flowarg.vipium.common.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class VipiumConfig
{
    public static final ForgeConfigSpec CLIENT_SPECS;
    public static final ForgeConfigSpec SERVER_SPECS;
    public static final Client CLIENT;
    public static final Server SERVER;

    static
    {
        final Pair<Client, ForgeConfigSpec> clientPair = new Builder().configure(Client::new);
        final Pair<Server, ForgeConfigSpec> serverPair = new Builder().configure(Server::new);
        CLIENT_SPECS = clientPair.getRight();
        SERVER_SPECS = serverPair.getRight();
        CLIENT = clientPair.getLeft();
        SERVER = serverPair.getLeft();
    }

    public static class Client
    {
        private final BooleanValue showRealms;

        public Client(@Nonnull Builder builder)
        {
            builder.comment(" Welcome to the Vipium Configuration !")
                   .push("client");

            this.showRealms = builder.comment(" Define if the Realms button is showed or not in the main menu.")
                                     .define("showRealms", false);

            builder.pop();
        }

        public BooleanValue canShowRealms()
        {
            return this.showRealms;
        }
    }

    public static class Server
    {
        private final ForgeConfigSpec.ConfigValue<String> paul;
        private final ForgeConfigSpec.ConfigValue<String> solan;

        public Server(@Nonnull Builder builder)
        {
            builder.comment(" Welcome to the Vipium Configuration !")
                    .push("server");
            this.paul = builder.comment(" Set the Paul pseudo.")
                    .define("paulPseudo", "Paul");
            this.solan = builder.comment(" Set the Solan pseudo.")
                    .define("solanPseudo", "Solan");
            builder.pop();
        }

        public ForgeConfigSpec.ConfigValue<String> getPaul()
        {
            return this.paul;
        }

        public ForgeConfigSpec.ConfigValue<String> getSolan()
        {
            return this.solan;
        }
    }
}
