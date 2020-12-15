package fr.flowarg.vipium.common.world;

import fr.flowarg.vipium.common.core.RegistryHandler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGeneration
{
    public void setupVipiumOreGeneration()
    {
        ForgeRegistries.BIOMES.forEach(biome -> {
            final ConfiguredPlacement<CountRangeConfig> placement = Placement.COUNT_RANGE.configure(new CountRangeConfig(4, 3, 3, 19));
            biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE, RegistryHandler.VIPIUM_ORE.get().getDefaultState(), 7)).withPlacement(placement));
        });
    }

    public void setupVipiumBlockGeneration()
    {
        ForgeRegistries.BIOMES.forEach(biome -> {
            if(biome.getCategory() == Biome.Category.MESA)
            {
                final ConfiguredPlacement<CountRangeConfig> placement = Placement.COUNT_RANGE.configure(new CountRangeConfig(1, 1, 1, 4));
                biome.addFeature(Decoration.UNDERGROUND_ORES, Feature.ORE.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE, RegistryHandler.VIPIUM_BLOCK.get().getDefaultState(), 1)).withPlacement(placement));
            }
        });
    }
}
