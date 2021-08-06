package fr.flowarg.vip3.data.data;

import fr.flowarg.vip3.VIP3;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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

    }
}
