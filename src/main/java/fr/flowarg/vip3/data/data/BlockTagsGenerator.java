package fr.flowarg.vip3.data.data;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagsGenerator extends BlockTagsProvider
{
    public BlockTagsGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generator, VIP3.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                VObjects.VIPIUM_BLOCK.get(),
                VObjects.PURE_VIPIUM_BLOCK.get(),
                VObjects.VIPIUM_ORE.get(),
                VObjects.DEEPSLATE_VIPIUM_ORE.get(),
                //VObjects.VIPIUM_PURIFIER.get(),
                VObjects.VIPIUM_CRUSHER.get()
        );
    }
}
