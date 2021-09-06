package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.capabilities.ArmorConfiguration;
import fr.flowarg.vip3.network.capabilities.CapabilitiesEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class VConfigureEffectsButton extends Checkbox implements VWidget
{
    private final int[] armorIDs;
    private final BiConsumer<ArmorConfiguration, Boolean> defineConsumer;

    public VConfigureEffectsButton(int pX, int pY, boolean pSelected, BiConsumer<ArmorConfiguration, Boolean> defineConsumer, int... armorIDs)
    {
        super(pX, pY, 19, 19, TextComponent.EMPTY, pSelected);
        this.defineConsumer = defineConsumer;
        this.armorIDs = armorIDs;
    }

    @Override
    public void renderButton(@NotNull PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks)
    {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.blit(pMatrixStack, this.x, this.y, !isEquip(this.armorIDs) ? 38 : this.isHovered() ? 19 : 0, this.selected() ? 55 : 36, this.width, this.height);
    }

    @Override
    public void onPress()
    {
        if(isEquip(this.armorIDs))
        {
            super.onPress();
            Minecraft.getInstance().player.getCapability(CapabilitiesEventHandler.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                this.defineConsumer.accept(armorConfiguration, this.selected());
                armorConfiguration.notifyChange();
                VNetwork.SYNC_CHANNEL.sendToServer(new VArmorConfigurationPacket(armorConfiguration));
            });
        }
    }

    private static boolean isEquip(int @NotNull ... slots)
    {
        for (int armorID : slots)
        {
            final var player = Minecraft.getInstance().player;
            if(player == null || !player.getInventory().armor.get(armorID).getItem().getRegistryName().getPath().contains("pure"))
                return false;
        }
        return true;
    }
}