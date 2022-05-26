package fr.flowarg.vip3.features;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OreGeneration
{
    private boolean registered = false;
    private VipiumOreGeneration vipiumOreGeneration = null;

    @SubscribeEvent
    public void generateOres(final @NotNull BiomeLoadingEvent event)
    {
        if(!this.registered)
        {
            this.vipiumOreGeneration = new VipiumOreGeneration();
            this.registered = true;
        }

        if(event.getCategory() != Biome.BiomeCategory.THEEND && event.getCategory() != Biome.BiomeCategory.NETHER)
        {
            this.generateVipiumOre(event);
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(this.vipiumOreGeneration.vipiumOrePlaced);
        }
    }

    private void generateVipiumOre(@NotNull BiomeLoadingEvent event)
    {

    }

    private static class VipiumOreGeneration
    {
        private final Holder<PlacedFeature> vipiumOrePlaced;

        public VipiumOreGeneration()
        {
            final OreConfiguration oreConfiguration = new OreConfiguration(List.of(
                    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, VObjects.VIPIUM_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, VObjects.DEEPSLATE_VIPIUM_ORE.get().defaultBlockState())), 6);
            final Holder<ConfiguredFeature<OreConfiguration, ?>> vipiumOre =
                    FeatureUtils.register("vipium_ore", Feature.ORE, oreConfiguration);
            this.vipiumOrePlaced = PlacementUtils.register(
                    "vipium_ore_placed", vipiumOre,
                    List.of(CountPlacement.of(8),
                            InSquarePlacement.spread(),
                            HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(20)),
                            BiomeFilter.biome()));
        }
    }
}
