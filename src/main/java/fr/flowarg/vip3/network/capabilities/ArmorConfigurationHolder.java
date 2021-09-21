package fr.flowarg.vip3.network.capabilities;

import org.jetbrains.annotations.NotNull;

public class ArmorConfigurationHolder implements ArmorConfiguration
{
    private boolean helmetEffect;
    private boolean chestPlateEffect;
    private boolean leggingsEffect;
    private boolean bootsEffect;
    private boolean fullSet1Effect;
    private boolean fullSet2Effect;

    @Override
    public boolean helmetEffect()
    {
        return this.helmetEffect;
    }

    @Override
    public boolean chestPlateEffect()
    {
        return this.chestPlateEffect;
    }

    @Override
    public boolean leggingsEffect()
    {
        return this.leggingsEffect;
    }

    @Override
    public boolean bootsEffect()
    {
        return this.bootsEffect;
    }

    @Override
    public boolean fullSet1Effect()
    {
        return this.fullSet1Effect;
    }

    @Override
    public boolean fullSet2Effect()
    {
        return this.fullSet2Effect;
    }

    @Override
    public void defineConfig(boolean @NotNull [] config)
    {
        this.helmetEffect = config[0];
        this.chestPlateEffect = config[1];
        this.leggingsEffect = config[2];
        this.bootsEffect = config[3];
        this.fullSet1Effect = config[4];
        this.fullSet2Effect = config[5];
    }

    @Override
    public boolean[] getConfig()
    {
        return new boolean[]{this.helmetEffect, this.chestPlateEffect, this.leggingsEffect, this.bootsEffect, this.fullSet1Effect, this.fullSet2Effect};
    }

    @Override
    public String toString()
    {
        return "ArmorConfigurationHolder{" + "helmetEffect=" + this.helmetEffect + ", chestPlateEffect=" + this.chestPlateEffect + ", leggingsEffect=" + this.leggingsEffect + ", bootsEffect=" + this.bootsEffect + ", fullSet1Effect=" + this.fullSet1Effect + ", fullSet2Effect=" + this.fullSet2Effect + '}';
    }
}
