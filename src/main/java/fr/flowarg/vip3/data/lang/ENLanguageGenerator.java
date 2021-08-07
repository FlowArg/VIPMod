package fr.flowarg.vip3.data.lang;

import fr.flowarg.vip3.features.VObjects;
import net.minecraft.data.DataGenerator;

public class ENLanguageGenerator extends CommonLanguageProvider
{
    public ENLanguageGenerator(DataGenerator gen)
    {
        super(gen, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        super.addTranslations();
        this.addItem(VObjects.VIPIUM_PICKAXE, "Vipium Pickaxe");
        this.addItem(VObjects.VIPIUM_AXE, "Vipium Axe");
        this.addItem(VObjects.VIPIUM_SHOVEL, "Vipium Shovel");
        this.addItem(VObjects.VIPIUM_HOE, "Vipium Hoe");
        this.addItem(VObjects.VIPIUM_MULTI_TOOL, "Vipium Multi Tool");
        this.addItem(VObjects.VIPIUM_SWORD, "Vipium Sword");

        this.addItem(VObjects.VIPIUM_FRAGMENT, "Vipium Fragment");
        this.addItem(VObjects.VIPIUM_INGOT, "Vipium Ingot");

        this.addItem(VObjects.PURE_VIPIUM_PICKAXE, "Pure Vipium Pickaxe");
        this.addItem(VObjects.PURE_VIPIUM_AXE, "Pure Vipium Axe");
        this.addItem(VObjects.PURE_VIPIUM_SHOVEL, "Pure Vipium Shovel");
        this.addItem(VObjects.PURE_VIPIUM_HOE, "Pure Vipium Hoe");
        this.addItem(VObjects.PURE_VIPIUM_MULTI_TOOL, "Pure Vipium Multi Tool");
        this.addItem(VObjects.PURE_VIPIUM_SWORD, "Pure Vipium Sword");

        this.addItem(VObjects.PURE_VIPIUM_FRAGMENT, "Pure Vipium Fragment");
        this.addItem(VObjects.PURE_VIPIUM_INGOT, "Pure Vipium Ingot");

        this.addItem(VObjects.VIPIUM_HELMET, "Vipium Helmet");
        this.addItem(VObjects.VIPIUM_CHESTPLATE, "Vipium Chestplate");
        this.addItem(VObjects.VIPIUM_LEGGINGS, "Vipium Leggings");
        this.addItem(VObjects.VIPIUM_BOOTS, "Vipium Boots");

        this.addItem(VObjects.PURE_VIPIUM_HELMET, "Pure Vipium Helmet");
        this.addItem(VObjects.PURE_VIPIUM_CHESTPLATE, "Pure Vipium Chestplate");
        this.addItem(VObjects.PURE_VIPIUM_LEGGINGS, "Pure Vipium Leggings");
        this.addItem(VObjects.PURE_VIPIUM_BOOTS, "Pure Vipium Boots");

        this.addItem(VObjects.VIPIUM_APPLE, "Vipium Apple");
        this.addItem(VObjects.PURE_VIPIUM_APPLE, "Pure Vipium Apple");
        this.addItem(VObjects.FRENCH_BAGUETTE, "French Baguette");

        this.addBlock(VObjects.VIPIUM_BLOCK, "Vipium Block");
        this.addBlock(VObjects.PURE_VIPIUM_BLOCK, "Pure Vipium Block");
        this.addBlock(VObjects.VIPIUM_ORE, "Vipium Ore");
        this.addBlock(VObjects.DEEPSLATE_VIPIUM_ORE, "Deepslate Vipium Ore");
    }
}
