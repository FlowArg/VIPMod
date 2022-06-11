package fr.flowarg.vip3.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AtlasScreen extends Screen
{
    public AtlasScreen()
    {
        super(new TranslatableComponent("screen.atlas"));
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
