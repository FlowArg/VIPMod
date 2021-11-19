package fr.flowarg.vip3.features;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OreGeneration
{
    @SubscribeEvent
    public void generateOres(final @NotNull BiomeLoadingEvent event)
    {
        if(event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER)
            this.generateVipiumOre(event);
    }

    private void generateVipiumOre(@NotNull BiomeLoadingEvent event)
    {
        event.getGeneration()
                .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, Feature.ORE
                        .configured(new OreConfiguration(List.of(
                                OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, VObjects.VIPIUM_ORE.get().defaultBlockState()),
                                OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, VObjects.DEEPSLATE_VIPIUM_ORE.get().defaultBlockState())), 8))
                        .rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(20))
                        .squared()
                );
    }
}
