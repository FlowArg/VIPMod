package fr.flowarg.vip3.features.capabilities.armorconfiguration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public interface ArmorConfiguration
{
    boolean helmetEffect();
    boolean chestPlateEffect();
    boolean leggingsEffect();
    boolean bootsEffect();
    boolean fullSet1Effect();
    boolean fullSet2Effect();

    void defineConfig(boolean[] config);
    boolean[] getConfig();

    static @NotNull CompoundTag serializeNBT(@NotNull ArmorConfiguration holder)
    {
        final var tag = new CompoundTag();
        tag.putBoolean("Helmet", holder.helmetEffect());
        tag.putBoolean("ChestPlate", holder.chestPlateEffect());
        tag.putBoolean("Leggings", holder.leggingsEffect());
        tag.putBoolean("Boots", holder.bootsEffect());
        tag.putBoolean("Set1", holder.fullSet1Effect());
        tag.putBoolean("Set2", holder.fullSet2Effect());
        return tag;
    }

    static void deserializeNBT(Tag nbt, @NotNull ArmorConfiguration holder)
    {
        final var tag = (CompoundTag)nbt;
        holder.defineConfig(new boolean[]{tag.getBoolean("Helmet"), tag.getBoolean("ChestPlate"), tag.getBoolean("Leggings"), tag.getBoolean("Boots"), tag.getBoolean("Set1"), tag.getBoolean("Set2")});
    }
}
