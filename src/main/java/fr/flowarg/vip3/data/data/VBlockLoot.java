package fr.flowarg.vip3.data.data;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class VBlockLoot extends BlockLoot
{
    @Override
    protected void addTables()
    {
        super.addTables();
        this.dropSelf(VObjects.VIPIUM_BLOCK.get());
        this.dropSelf(VObjects.PURE_VIPIUM_BLOCK.get());
        this.add(VObjects.VIPIUM_ORE.get(), this::createVipiumOreDrops);
        this.add(VObjects.DEEPSLATE_VIPIUM_ORE.get(), this::createVipiumOreDrops);
        this.add(VObjects.VIPIUM_CRUSHER.get(), BlockLoot::createNameableBlockEntityTable);
        this.dropSelf(VObjects.TELEPORTATION_ALTAR.get());
    }

    protected LootTable.Builder createVipiumOreDrops(Block block)
    {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(VObjects.VIPIUM_FRAGMENT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1F, 2F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
}
