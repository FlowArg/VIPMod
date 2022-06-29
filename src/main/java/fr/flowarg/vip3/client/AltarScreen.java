package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.altar.Altar;
import fr.flowarg.vip3.features.altar.ConnectedAtlas;
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

@OnlyIn(Dist.CLIENT)
public class AltarScreen extends Screen implements VWidgetAccessor, VBackgroundScreen
{
    private static final ResourceLocation ALTAR_TEXTURE = new ResourceLocation(VIP3.MOD_ID, "textures/gui/teleportation_altar.png");

    private final int imageWidth = 248, imageHeight = 250;
    private final Altar altar;
    private EditBox title;

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

        this.title = new EditBox(this.font, leftPos + 10, topPos + 10, 130, 10, TextComponent.EMPTY);
        this.title.setValue(this.altar.getName());
        if(!this.altar.getOwner().equals(this.minecraft.player.getStringUUID()))
            this.title.setEditable(false);
        this.title.setBordered(false);
        this.title.setTextColor(0xfcfcfc);
        this.title.setResponder(this.altar::setName);
        this.addRenderableWidget(this.title);

        this.addRenderableWidget(new VButton(leftPos + 222, topPos + 10, VWidgets.ALTAR_MENU, pButton -> {
            // open menu
        }, new VToolTip(this, "Permissions")));

        int y = 31;
        for (ConnectedAtlas value : this.altar.getConnectedAtlases().values())
        {
            this.addRenderableWidget(new VAltarCheckboxButton(leftPos + 126, topPos + y, false, true));
            final var editBox = new EditBox(this.font, leftPos + 15, topPos + y, 96, 18, new TextComponent(value.playerName()));
            editBox.setValue(value.id());
            editBox.setEditable(false);
            editBox.setCanLoseFocus(false);
            this.addRenderableWidget(editBox);

            this.addRenderableWidget(new VButton(leftPos + 161, topPos + y, VWidgets.ALTAR_TRASH, pButton -> {
                // delete line
                this.minecraft.setScreen(new VConfirmScreen(t -> {
                    if(t)
                    {
                        pButton.active = false;
                        this.altar.getConnectedAtlases().remove(value.id());
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
