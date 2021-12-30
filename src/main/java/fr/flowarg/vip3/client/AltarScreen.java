package fr.flowarg.vip3.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class AltarScreen extends Screen
{
    public AltarScreen()
    {
        super(new TranslatableComponent("screen.altar"));
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
