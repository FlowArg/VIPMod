package fr.flowarg.vip3.network;

import fr.flowarg.vip3.network.capabilities.ArmorConfiguration;
import fr.flowarg.vip3.network.capabilities.CapabilitiesEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public final class VArmorConfigurationPacket
{
    private final boolean helmetEffect;
    private final boolean chestPlateEffect;
    private final boolean leggingsEffect;
    private final boolean bootsEffect;
    private final boolean fullSet1Effect;
    private final boolean fullSet2Effect;

    public VArmorConfigurationPacket(boolean helmetEffect, boolean chestPlateEffect, boolean leggingsEffect, boolean bootsEffect, boolean fullSet1Effect, boolean fullSet2Effect)
    {
        this.helmetEffect = helmetEffect;
        this.chestPlateEffect = chestPlateEffect;
        this.leggingsEffect = leggingsEffect;
        this.bootsEffect = bootsEffect;
        this.fullSet1Effect = fullSet1Effect;
        this.fullSet2Effect = fullSet2Effect;
    }

    public VArmorConfigurationPacket(ArmorConfiguration armorConfiguration)
    {
        this.helmetEffect = armorConfiguration.helmetEffect();
        this.chestPlateEffect = armorConfiguration.chestPlateEffect();
        this.leggingsEffect = armorConfiguration.leggingsEffect();
        this.bootsEffect = armorConfiguration.bootsEffect();
        this.fullSet1Effect = armorConfiguration.fullSet1Effect();
        this.fullSet2Effect = armorConfiguration.fullSet2Effect();
    }

    public static void encode(VArmorConfigurationPacket pck, FriendlyByteBuf byteBuf)
    {
        byteBuf.writeBoolean(pck.helmetEffect);
        byteBuf.writeBoolean(pck.chestPlateEffect);
        byteBuf.writeBoolean(pck.leggingsEffect);
        byteBuf.writeBoolean(pck.bootsEffect);
        byteBuf.writeBoolean(pck.fullSet1Effect);
        byteBuf.writeBoolean(pck.fullSet2Effect);
    }

    public static VArmorConfigurationPacket decode(FriendlyByteBuf byteBuf)
    {
        return new VArmorConfigurationPacket(byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean(), byteBuf.readBoolean());
    }

    public static void handle(VArmorConfigurationPacket pck, Supplier<NetworkEvent.Context> ctx)
    {
        if(ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
            ctx.get().enqueueWork(() -> handleClientUpdate(pck));
        else update(ctx.get().getSender(), pck);
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientUpdate(VArmorConfigurationPacket pck)
    {
        update(Minecraft.getInstance().player, pck);
    }

    private static void update(Player player, VArmorConfigurationPacket pck)
    {
        player.getCapability(CapabilitiesEventHandler.ARMOR_CONFIGURATION_CAPABILITY)
                .ifPresent(capa -> {
                    capa.helmetEffect(pck.helmetEffect);
                    capa.chestPlateEffect(pck.chestPlateEffect);
                    capa.leggingsEffect(pck.leggingsEffect);
                    capa.bootsEffect(pck.bootsEffect);
                    capa.fullSet1Effect(pck.fullSet1Effect);
                    capa.fullSet2Effect(pck.fullSet2Effect);
                });
    }
}
