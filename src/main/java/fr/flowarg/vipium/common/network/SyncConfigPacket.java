package fr.flowarg.vipium.common.network;

import fr.flowarg.vipium.common.capability.armorconfig.ArmorConfigCapability;
import fr.flowarg.vipium.common.capability.armorconfig.IArmorConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncConfigPacket
{
    private final int[] armorConfig;

    public SyncConfigPacket(IArmorConfig instance)
    {
        this.armorConfig = instance.getArmorConfig();
    }

    public SyncConfigPacket(int[] armorConfig)
    {
        this.armorConfig = armorConfig;
    }

    public static void encode(SyncConfigPacket pck, PacketBuffer buf)
    {
        buf.writeInt(pck.armorConfig.length);
        for (int i : pck.armorConfig)
            buf.writeInt(i);
    }

    public static SyncConfigPacket decode(PacketBuffer buf)
    {
        final int[] config = new int[buf.readInt()];
        for (int i = 0; i < config.length; i++)
            config[i] = buf.readInt();
        return new SyncConfigPacket(config);
    }

    public static void handle(SyncConfigPacket pck, Supplier<NetworkEvent.Context> ctxSupplier)
    {
        if (ctxSupplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
            ctxSupplier.get().enqueueWork(() -> handleClientUpdate(pck));
        ctxSupplier.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientUpdate(SyncConfigPacket pck)
    {
        final ClientPlayerEntity player = Minecraft.getInstance().player;
        player.getCapability(ArmorConfigCapability.ARMOR_CONFIG_CAPABILITY).ifPresent(iArmorConfig -> iArmorConfig.setArmorConfig(pck.armorConfig));
    }
}
