package fr.flowarg.vip3.data.lang;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.DataGenerator;

public class FRLanguageGenerator extends CommonLanguageProvider
{
    public FRLanguageGenerator(DataGenerator gen)
    {
        super(gen, "fr_fr");
    }

    @Override
    protected void addTranslations()
    {
        super.addTranslations();
        this.addItem(VObjects.VIPIUM_PICKAXE, "Pioche en Vipium");
        this.addItem(VObjects.VIPIUM_AXE, "Hache en Vipium");
        this.addItem(VObjects.VIPIUM_SHOVEL, "Pelle en Vipium");
        this.addItem(VObjects.VIPIUM_HOE, "Houe en Vipium");
        this.addItem(VObjects.VIPIUM_MULTI_TOOL, "Multi Tool en Vipium");
        this.addItem(VObjects.VIPIUM_SWORD, "Épée en Vipium");

        this.addItem(VObjects.VIPIUM_FRAGMENT, "Fragment de Vipium");
        this.addItem(VObjects.VIPIUM_INGOT, "Lingot de Vipium");

        this.addItem(VObjects.PURE_VIPIUM_PICKAXE, "Pioche en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_AXE, "Hache en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_SHOVEL, "Pelle en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_HOE, "Houe en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_MULTI_TOOL, "Multi Tool en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_SWORD, "Épée en Vipium Pur");

        this.addItem(VObjects.PURE_VIPIUM_FRAGMENT, "Fragment de Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_INGOT, "Lingot de Vipium Pur");

        this.addItem(VObjects.VIPIUM_HELMET, "Casque en Vipium");
        this.addItem(VObjects.VIPIUM_CHESTPLATE, "Plastron en Vipium");
        this.addItem(VObjects.VIPIUM_LEGGINGS, "Pantalon en Vipium");
        this.addItem(VObjects.VIPIUM_BOOTS, "Bottes en Vipium");

        this.addItem(VObjects.PURE_VIPIUM_HELMET, "Casque en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_CHESTPLATE, "Plastron en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_LEGGINGS, "Pantalon en Vipium Pur");
        this.addItem(VObjects.PURE_VIPIUM_BOOTS, "Bottes en Vipium Pur");

        this.addItem(VObjects.VIPIUM_APPLE, "Pomme en Vipium");
        this.addItem(VObjects.PURE_VIPIUM_APPLE, "Pomme en Vipium Pur");
        this.addItem(VObjects.FRENCH_BAGUETTE, "Baguette Française");
    }
}
