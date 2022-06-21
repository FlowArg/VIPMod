package fr.flowarg.vip3.data.lang;

import fr.flowarg.vip3.VIP3;
import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class CommonLanguageProvider extends LanguageProvider
{
    public CommonLanguageProvider(DataGenerator gen, String locale)
    {
        super(gen, VIP3.MOD_ID, locale);
    }

    @Override
    protected void addTranslations()
    {
        this.addItem(VObjects.AUBIN_SLAYER, "Aubin Slayer");
        this.add("itemGroup.vip3", "VIP 3");
        this.add("key.categories.vip3", "VIP3");
        this.add("vip3.setup.ass.stop_key", "Stop");
    }
}
