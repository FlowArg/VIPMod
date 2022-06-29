package fr.flowarg.vip3.features.altar;

import fr.flowarg.vip3.client.AtlasScreen;
import net.minecraft.client.Minecraft;

class GuiHack
{
    static void openAtlasScreen()
    {
        Minecraft.getInstance().setScreen(new AtlasScreen());
    }
}
