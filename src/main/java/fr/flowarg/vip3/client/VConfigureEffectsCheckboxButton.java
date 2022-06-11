package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.features.capabilities.armorconfiguration.ArmorConfigurationCapability;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VConfigureEffectsCheckboxButton extends Checkbox implements VWidget
{
    private final int[] armorIDs;
    private final int id;

    public VConfigureEffectsCheckboxButton(int pX, int pY, boolean pSelected, int id, int... armorIDs)
    {
        super(pX, pY, 19, 19, TextComponent.EMPTY, pSelected, false);
        this.id = id;
        this.armorIDs = armorIDs;
    }

    @Override
    public VWidgets widget()
    {
        return this.selected() ? VWidgets.YES_19_19 : VWidgets.NO_19_19;
    }

    @Override
    public VWidgetStatus status()
    {
        return !this.isEquip() ? VWidgetStatus.DISABLED : VWidget.super.status();
    }

    @Override
    public void renderButton(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        VWidget.super.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void onPress()
    {
        if(this.isEquip())
        {
            super.onPress();
            Minecraft.getInstance().player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                final var array = armorConfiguration.getConfig().clone();
                array[this.id] = this.selected();
                armorConfiguration.defineConfig(array);
                VNetwork.SYNC_CHANNEL.sendToServer(new VArmorConfigurationPacket(armorConfiguration));
            });
        }
    }

    private boolean isEquip()
    {
        for (int armorID : this.armorIDs)
        {
            final var player = Minecraft.getInstance().player;
            if(player == null || !player.getInventory().armor.get(armorID).getItem().getRegistryName().getPath().contains("pure"))
                return false;
        }
        return true;
    }
}
