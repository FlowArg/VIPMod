package fr.flowarg.vipium.common.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class VipiumConfig
{
    public static final ForgeConfigSpec CLIENT_SPECS;
    public static final Client CLIENT;

    static
    {
        final Pair<Client, ForgeConfigSpec> clientPair = new Builder().configure(Client::new);
        CLIENT_SPECS = clientPair.getRight();
        CLIENT = clientPair.getLeft();
    }

    public static class Client
    {
        private final BooleanValue showRealms;
        private final BooleanValue showUselessOptions;

        public Client(@Nonnull Builder builder)
        {
            builder.comment(" Welcome to the Vipium Configuration !")
                   .push("client");

            this.showRealms = builder.comment(" Define if the Realms button is showed or not in the main menu.")
                    .define("showRealms", false);
            this.showUselessOptions = builder.comment(" Define if some useless buttons are showed or not in the echap menu.")
                    .define("showUselessOptions", false);

            builder.pop();
        }

        public BooleanValue canShowRealms()
        {
            return this.showRealms;
        }

        public BooleanValue canShowUselessOptions()
        {
            return this.showUselessOptions;
        }
    }
}
