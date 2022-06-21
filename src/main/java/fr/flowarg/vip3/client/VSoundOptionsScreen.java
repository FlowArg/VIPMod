package fr.flowarg.vip3.client;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.client.ass.ASSConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class VSoundOptionsScreen extends SoundOptionsScreen
{
    public VSoundOptionsScreen(Screen lastScreen, Options options)
    {
        super(lastScreen, options);
    }

    @Override
    protected void init()
    {
        super.init();
        this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height / 6 - 12 + 22 * (11 >> 1), 150, 20, new TranslatableComponent("vip3.ass.title"), (button) -> {
            try
            {
                VIP3.getClientManager().checkVIPSoundsDir();
                if(!ClientEventHandler.SOUND_FILE_ERROR.isEmpty())
                    this.minecraft.setScreen(new VErrorScreen());
                else Minecraft.getInstance().setScreen(new ASSConfigScreen(this, this.options));
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }));
    }
}
