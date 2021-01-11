package fr.flowarg.vipium.common.core;

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
        private final BooleanValue enableRPC;
        private final BooleanValue enableHelmetEffect;
        private final BooleanValue enableChestplateEffect;
        private final BooleanValue enableLeggingsEffect;
        private final BooleanValue enableBootsEffect;
        private final BooleanValue enableFirstFullEffect;
        private final BooleanValue enableSecondFullEffect;

        public Client(@Nonnull Builder builder)
        {
            builder.comment(" Welcome to the Vipium Configuration !")
                   .push("client");

            this.showRealms = builder.comment(" Define if the Realms button is showed or not in the main menu.")
                    .define("showRealms", false);
            this.showUselessOptions = builder.comment(" Define if some useless buttons are showed or not in the echap menu.")
                    .define("showUselessOptions", false);
            this.enableRPC = builder.comment(" Enable/Disable the Discord Rich Presence for VIP.")
                    .define("enableRPC", true);
            this.enableHelmetEffect = builder.comment(" Enable/Disable the effect given by the Pure Vipium Helmet")
                    .define("enableHelmetEffect", true);
            this.enableChestplateEffect = builder.comment(" Enable/Disable the effect given by the Pure Vipium Chestplate")
                    .define("enableChestplateEffect", true);
            this.enableLeggingsEffect = builder.comment(" Enable/Disable the effect given by Pure Vipium Leggings")
                    .define("enableLeggingsEffect", true);
            this.enableBootsEffect = builder.comment(" Enable/Disable the effect given by Pure Vipium Boots")
                    .define("enableBootsEffect", true);
            this.enableFirstFullEffect = builder.comment(" Enable/Disable the first effect given by the complete Pure Vipium Armor")
                    .define("enableFirstFullEffect", true);
            this.enableSecondFullEffect = builder.comment(" Enable/Disable the second effect given by the complete Pure Vipium Armor")
                    .define("enableSecondFullEffect", true);

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

        public BooleanValue getEnableRPC()
        {
            return this.enableRPC;
        }

        public BooleanValue getEnableHelmetEffect()
        {
            return this.enableHelmetEffect;
        }

        public BooleanValue getEnableChestplateEffect()
        {
            return this.enableChestplateEffect;
        }

        public BooleanValue getEnableLeggingsEffect()
        {
            return this.enableLeggingsEffect;
        }

        public BooleanValue getEnableBootsEffect()
        {
            return this.enableBootsEffect;
        }

        public BooleanValue getEnableFirstFullEffect()
        {
            return this.enableFirstFullEffect;
        }

        public BooleanValue getEnableSecondFullEffect()
        {
            return this.enableSecondFullEffect;
        }
    }
}
