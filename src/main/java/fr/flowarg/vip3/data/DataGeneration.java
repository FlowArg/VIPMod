package fr.flowarg.vip3.data;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.data.data.BlockTagsGenerator;
import fr.flowarg.vip3.data.data.LootTablesGenerator;
import fr.flowarg.vip3.data.data.RecipeGenerator;
import fr.flowarg.vip3.data.lang.ENLanguageGenerator;
import fr.flowarg.vip3.data.lang.FRLanguageGenerator;
import fr.flowarg.vip3.data.models.BlockModelGenerator;
import fr.flowarg.vip3.data.models.ItemModelGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = VIP3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final var generator = event.getGenerator();

        final var existingFileHelper = event.getExistingFileHelper();

        if(event.includeClient()) {
            generator.addProvider(new ItemModelGenerator(generator, existingFileHelper));
            generator.addProvider(new BlockModelGenerator(generator, existingFileHelper));
            generator.addProvider(new ENLanguageGenerator(generator));
            generator.addProvider(new FRLanguageGenerator(generator));
        }

        if(event.includeServer()) {
            generator.addProvider(new BlockTagsGenerator(generator, existingFileHelper));
            generator.addProvider(new RecipeGenerator(generator));
            generator.addProvider(new LootTablesGenerator(generator));
        }
    }
}
