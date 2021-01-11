package fr.flowarg.vipium.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class VIPKeyBindings
{
    private final KeyBinding vipiumPureArmorEffectsGUIBinding;

    public VIPKeyBindings()
    {
        this.vipiumPureArmorEffectsGUIBinding = new KeyBinding("key.vipium_pure_armor", 325, "key.categories.vip");
    }

    public void registerKeyBindings()
    {
        ClientRegistry.registerKeyBinding(this.vipiumPureArmorEffectsGUIBinding);
    }

    public KeyBinding getVipiumPureArmorEffectsGUIBinding()
    {
        return this.vipiumPureArmorEffectsGUIBinding;
    }
}
