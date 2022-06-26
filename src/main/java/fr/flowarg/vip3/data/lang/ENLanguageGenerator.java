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
        this.addItem(VObjects.VIPIUM_BOW, "Vipium Bow");
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
        this.addBlock(VObjects.VIPIUM_PURIFIER, "Vipium Purifier");
        this.addBlock(VObjects.VIPIUM_CRUSHER, "Vipium Crusher");

        this.add("container.vipium_purifier", "Vipium Purifier");
        this.add("container.vipium_purifier.stats.total_ingots", "Total of purified ingots: %d");

        this.add("container.vipium_crusher", "Vipium Crusher");
        this.add("container.vipium_crusher.start", "Start crusher");
        this.add("container.vipium_crusher.stop", "Stop crusher");
        this.add("container.vipium_crusher.stats.total_ingots", "Total of crushed ingots: %d");

        this.add("container.common.stats", "Obtainable fragments: ");
        this.add("container.common.stats.total_fragments", "Total of fragments obtained: %d");
        this.add("container.common.stats.luck", "Your luck: %s%%");
        this.add("container.common.stats.average_single", "Average fragments for 1 ingot: %s");

        this.addBlock(VObjects.TELEPORTATION_ALTAR, "Teleportation Altar");
        this.addItem(VObjects.TELEPORTATION_ATLAS, "Teleportation Atlas");
        this.addItem(VObjects.HOLY_CHICKEN_BIBLE, "Holy Chicken Bible");
        this.add("screen.configure_effects", "Configure effects");
        this.add("screen.atlas", "Teleportation atlas");
        this.add("screen.altar", "Teleportation altar");
        this.add("key.configure_effects", "Configure effects");
        this.add("vip3.setup", "First launch of VIP3");
        this.add("vip3.setup.previous", "Previous");
        this.add("vip3.setup.next", "Next");
        this.add("vip3.setup.done", "Configuration done!");
        this.add("vip3.setup.message.1", "Welcome to the VIP3 configuration page!");
        this.add("vip3.setup.message.2", "This new season brings new features");
        this.add("vip3.setup.message.3", "some of which require manual configuration.");
        this.add("vip3.setup.message.4", "Don't worry, it's very simple!");
        this.add("vip3.setup.message.5", "Let the configuration wizard guide you and");
        this.add("vip3.setup.message.6", "and everything will be fine.");
        this.add("vip3.setup.message.7", "Guaranteed bug free!");
        this.add("vip3.setup.message.8", "NOTE: some parameters defined here are not reversible.");
        this.add("vip3.setup.ass.intro", "Configure here ASS keys (pause & resume / stop)");
        this.add("vip3.setup.ass.hint.1", "If the keys do not set, make sure to close the applications");
        this.add("vip3.setup.ass.hint.2", "that might interfere with the game (Spotify, YouTube, etc.)");
        this.add("vip3.setup.ass.pause_resume_key", "Pause/Resume");
        this.add("vip3.setup.ass.skip_key", "Skip");
        this.add("vip3.ass.title", "ASS Configuration");
        this.add("vip3.ass.menu", "Menu Music");
        this.add("vip3.ass.game", "Game Music");
        this.add("vip3.ass.default", "Default");
        this.add("vip3.ass.error_title", "Error when loading sounds");
        this.add("vip3.ass.error_message.1", "Some files in the vipsounds directory are not");
        this.add("vip3.ass.error_message.2", "in the right format. It might be an extension problem");
        this.add("vip3.ass.error_message.3", "or simply a name problem. Pour rappel, les fichiers");
        this.add("vip3.ass.error_message.4", "As a reminder, files must respect a naming convention:");
        this.add("vip3.ass.error_message.5", "Only numbers, lower case letters, ' . ', ' _ ' and ' - ' are allowed. ");
        this.add("vip3.ass.error_message.6", "The file name must not exceed 24 characters.");
        this.add("vip3.undefined", "Undefined");
    }
}
