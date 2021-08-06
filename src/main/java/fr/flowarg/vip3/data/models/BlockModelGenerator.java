package fr.flowarg.vip3.data.models;

import fr.flowarg.vip3.VIP3;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelGenerator extends BlockStateProvider
{
    public BlockModelGenerator(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, VIP3.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {

    }
}
