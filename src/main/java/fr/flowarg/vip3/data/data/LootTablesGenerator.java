package fr.flowarg.vip3.data.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import fr.flowarg.vip3.VIP3;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTablesGenerator extends LootTableProvider
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final DataGenerator generator;

    public LootTablesGenerator(DataGenerator generator)
    {
        super(generator);
        this.generator = generator;
    }

    @Override
    public void run(@NotNull HashCache cache)
    {
        final var outputFolder = this.generator.getOutputFolder();

        final var map = new HashMap<ResourceLocation, LootTable>();

        this.getTables().forEach((pair) -> pair.getFirst().get().accept((resourceLocation, builder) -> {
            if (map.put(resourceLocation, builder.setParamSet(pair.getSecond()).build()) != null) throw new IllegalStateException("Duplicate loot table " + resourceLocation);
        }));

        map.forEach((key, lootTable) -> {
            if(!key.getNamespace().equals(VIP3.MOD_ID)) return;

            final var path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.save(GSON, cache, net.minecraft.world.level.storage.loot.LootTables.serialize(lootTable), path);
            } catch (IOException e) {
                VIP3.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    @Override
    protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
    {
        return ImmutableList.of(Pair.of(VBlockLoot::new, LootContextParamSets.BLOCK));
    }
}
