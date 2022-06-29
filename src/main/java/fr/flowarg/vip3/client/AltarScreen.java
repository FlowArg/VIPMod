package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.altar.Altar;
import fr.flowarg.vip3.network.VNetwork;
import fr.flowarg.vip3.network.VSendAltarPacket;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AltarScreen extends Screen implements VWidgetAccessor, VBackgroundScreen
{
    private static final ResourceLocation ALTAR_TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/teleportation_altar.png");

    private final int imageWidth = 256, imageHeight = 256;
    private final List<Integer> suppressed = new ArrayList<>();
    private final Altar altar;

    public AltarScreen(Altar altar)
    {
        super(new TranslatableComponent("screen.altar"));
        this.altar = altar;
    }

    @Override
    protected void init()
    {
        final int leftPos = (this.width - this.imageWidth) / 2;
        final int topPos = (this.height - this.imageHeight) / 2;

        this.addRenderableWidget(new VButton(leftPos + 222, topPos + 10, VWidgets.ALTAR_MENU, pButton -> {
            // open menu
        }, new VToolTip(this, "Permissions")));

        int y = 31;
        for (int i = 0; i < 5; i++)
        {
            this.addRenderableWidget(new VAltarCheckboxButton(leftPos + 126, topPos + y, false, true));
            final var editBox = new EditBox(this.font, leftPos + 15, topPos + y, 96, 18, new TextComponent("Uwu"));
            editBox.setValue(this.suppressed.contains(i) ? "" : "Uwu");
            editBox.setEditable(false);
            editBox.setCanLoseFocus(false);
            this.addRenderableWidget(editBox);

            int finalI = i;
            this.addRenderableWidget(new VButton(leftPos + 161, topPos + y, VWidgets.ALTAR_TRASH, pButton -> {
                // delete line
                this.minecraft.setScreen(new VConfirmScreen(t -> {
                    if(t)
                    {
                        pButton.active = false;
                        this.suppressed.add(finalI);
                        this.minecraft.player.sendMessage(new TextComponent("La ligne a été supprimée"), this.minecraft.player.getUUID());
                    }
                    this.minecraft.setScreen(this);
                }));
            }, new VToolTip(this, "Supprimer le lien")));
            y += 30;
        }
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick)
    {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        drawString(pPoseStack, this.font, this.altar.getName(), this.width / 2 - this.imageWidth / 2 + 10, this.height / 2 - this.imageHeight / 2 + 5, 0xFFFFFF);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pPoseStack)
    {
        super.renderBackground(pPoseStack);
        this.renderBackground(pPoseStack, ALTAR_TEXTURE, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }


    @Override
    public void removed()
    {
        VNetwork.SYNC_CHANNEL.sendToServer(new VSendAltarPacket(this.altar, false));
    }
}
