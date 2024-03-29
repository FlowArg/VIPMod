package fr.flowarg.vip3.data.models;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.ManualModel;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Modifier;
import java.util.Objects;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, VIP3.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        final var itemGenerated = this.getExistingFile(this.mcLoc("item/generated"));
        final var itemHandheld = this.getExistingFile(this.mcLoc("item/handheld"));

        try
        {
            for (var field : VObjects.class.getDeclaredFields())
            {
                final var modifiers = field.getModifiers();

                if(!Modifier.isStatic(modifiers)) continue;

                if(!field.canAccess(null))
                    field.setAccessible(true);

                final var object = field.get(null);
                if(object instanceof final RegistryObject<?> registryObject)
                {
                    final var obj = registryObject.get();

                    if((!(obj instanceof Item)) || obj instanceof BlockItem || obj instanceof ManualModel) continue;

                    if (obj instanceof DiggerItem || obj instanceof SwordItem) this.buildSimpleItem(itemHandheld, Objects.requireNonNull(registryObject.getId()).getPath());
                    else this.buildSimpleItem(itemGenerated, Objects.requireNonNull(registryObject.getId()).getPath());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void buildSimpleItem(ModelFile modelFile, String name) {
        final var texture = this.modLoc("item/" + name);
        if(this.existingFileHelper.exists(texture, TEXTURE))
            this.getBuilder(name).parent(modelFile).texture("layer0", texture);
        else VIP3.LOGGER.error("Missing texture: " + texture);
    }
}
