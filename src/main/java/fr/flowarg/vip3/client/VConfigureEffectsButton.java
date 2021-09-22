package fr.flowarg.vip3.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.network.VArmorConfigurationPacket;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.capabilities.ArmorConfigurationCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VConfigureEffectsButton extends Checkbox implements VWidget
{
    private final int[] armorIDs;
    private final int id;

    public VConfigureEffectsButton(int pX, int pY, boolean pSelected, int id, int... armorIDs)
    {
        super(pX, pY, 19, 19, TextComponent.EMPTY, pSelected);
        this.id = id;
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
            Minecraft.getInstance().player.getCapability(ArmorConfigurationCapability.ARMOR_CONFIGURATION_CAPABILITY).ifPresent(armorConfiguration -> {
                final var array = armorConfiguration.getConfig().clone();
                array[this.id] = this.selected();
                armorConfiguration.defineConfig(array);
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
