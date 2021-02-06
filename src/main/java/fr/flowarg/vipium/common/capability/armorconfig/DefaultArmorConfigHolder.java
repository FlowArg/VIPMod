package fr.flowarg.vipium.common.capability.armorconfig;

public class DefaultArmorConfigHolder implements IArmorConfig
{
    private int[] armorConfig = {1, 1, 1, 1, 1, 1};

    @Override
    public int[] getArmorConfig()
    {
        return this.armorConfig;
    }

    @Override
    public void setArmorConfig(int[] armorConfig)
    {
        this.armorConfig = armorConfig;
    }
}
