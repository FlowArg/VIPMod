package fr.flowarg.vipium.common.capability.armorconfig;

import fr.flowarg.vipium.VIPMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = VIPMod.MODID, bus = Bus.FORGE)
public class ArmorConfigCapability
{
    @CapabilityInject(IArmorConfig.class)
    public static final Capability<IArmorConfig> ARMOR_CONFIG_CAPABILITY = null;

    public static final ResourceLocation ARMOR_CONFIG_CAP_KEY = new ResourceLocation(VIPMod.MODID, "armmor_config");
    private static final Map<PlayerEntity, IArmorConfig> INVALIDATED_CAPS = new WeakHashMap<>();

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IArmorConfig.class, new DefaultArmorConfigStorage(), DefaultArmorConfigHolder::new);
    }

    @SubscribeEvent
    public static void attachToPlayers(AttachCapabilitiesEvent<Entity> event)
    {
        IArmorConfig holder;
        if (event.getObject() instanceof PlayerEntity)
        {
            if (event.getObject() instanceof ServerPlayerEntity) holder = new PlayerArmorConfigHolder((ServerPlayerEntity)event.getObject());
            else holder = ARMOR_CONFIG_CAPABILITY.getDefaultInstance();

            final PlayerArmorConfigWrapper wrapper = new PlayerArmorConfigWrapper(holder);
            event.addCapability(ARMOR_CONFIG_CAP_KEY, wrapper);
            event.addListener(() -> wrapper.getCapability(ARMOR_CONFIG_CAPABILITY).ifPresent(cap -> INVALIDATED_CAPS.put((PlayerEntity)event.getObject(), cap)));
        }
    }

    public static class DefaultArmorConfigStorage implements Capability.IStorage<IArmorConfig>
    {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IArmorConfig> capability, IArmorConfig instance, Direction side)
        {
            return new IntArrayNBT(instance.getArmorConfig());
        }

        @Override
        public void readNBT(Capability<IArmorConfig> capability, IArmorConfig instance, Direction side, INBT nbt)
        {
            final IntArrayNBT intArrayNBT = (IntArrayNBT)nbt;
            instance.setArmorConfig(intArrayNBT.getIntArray());
        }
    }
}
