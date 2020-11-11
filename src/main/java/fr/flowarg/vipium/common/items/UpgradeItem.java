package fr.flowarg.vipium.common.items;

import fr.flowarg.vipium.common.containers.slots.upgrades.UpgradeType;
import net.minecraft.item.Item;

public class UpgradeItem extends Item
{
    private final UpgradeType upgradeType;

    public UpgradeItem(UpgradeType upgradeType, Properties properties)
    {
        super(properties);
        this.upgradeType = upgradeType;
    }

    public UpgradeType getUpgradeType()
    {
        return this.upgradeType;
    }
}
