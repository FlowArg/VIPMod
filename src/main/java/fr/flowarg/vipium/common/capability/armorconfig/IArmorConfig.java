package fr.flowarg.vipium.common.capability.armorconfig;

public interface IArmorConfig
{
    int[] DEFAULT = {1, 1, 1, 1, 1, 1};
    int[] getArmorConfig();
    void setArmorConfig(int[] armorConfig);
}
