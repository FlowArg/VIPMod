package fr.flowarg.vip3.client;

import fr.flowarg.vip3.features.capabilities.armorconfiguration.ArmorConfigurationCapability;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VConfigureEffectsCheckboxButton extends VCheckbox
{
    private final int[] armorIDs;
    private final int id;

    public VConfigureEffectsCheckboxButton(int pX, int pY, boolean pSelected, int id, int... armorIDs)
    {
        super(pX, pY, 19, 19, TextComponent.EMPTY, pSelected);
        this.id = id;
        this.armorIDs = armorIDs;
    }

    @Override
    public VWidgetStatus status()
    {
        return !this.isEquip() ? VWidgetStatus.DISABLED : super.status();
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

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton)
    {
        if (!this.active || !this.visible)
            return false;

        if (this.isValidClickButton(pButton))
        {
            boolean flag = this.clicked(pMouseX, pMouseY) && this.status() != VWidgetStatus.DISABLED;
            if (flag)
            {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.onClick(pMouseX, pMouseY);
                return true;
            }
        }
        return false;
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
