package fr.flowarg.datagenerators;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModelLoadingListener
{
    @SubscribeEvent
    public void onModelRegister(final ModelRegistryEvent event)
    {
        DataGenerators.generate();
        DataGenerators.fix();
    }
}
