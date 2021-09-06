package fr.flowarg.vip3.network.capabilities;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public interface ArmorConfiguration extends ICapabilitySerializable<Tag>
{
    boolean helmetEffect();
    boolean chestPlateEffect();
    boolean leggingsEffect();
    boolean bootsEffect();
    boolean fullSet1Effect();
    boolean fullSet2Effect();

    void helmetEffect(boolean val);
    void chestPlateEffect(boolean val);
    void leggingsEffect(boolean val);
    void bootsEffect(boolean val);
    void fullSet1Effect(boolean val);
    void fullSet2Effect(boolean val);

    void notifyChange();
}