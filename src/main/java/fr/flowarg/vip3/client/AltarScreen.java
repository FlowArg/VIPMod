package fr.flowarg.vip3.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.flowarg.vip3.VIP3;
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

    private final int imageWidth = 256, imageHeight = 256;

    public AltarScreen()
    {
        super(new TranslatableComponent("screen.altar"));
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
            this.addRenderableWidget(new VButton(leftPos + 161, topPos + y, VWidgets.ALTAR_TRASH, pButton -> {
                // delete line
                this.minecraft.setScreen(new VConfirmScreen(t -> {
                    if(t) this.minecraft.player.sendMessage(new TextComponent("La ligne a été supprimée"), this.minecraft.player.getUUID());
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
}
