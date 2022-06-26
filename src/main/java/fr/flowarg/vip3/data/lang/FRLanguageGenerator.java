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
        this.addItem(VObjects.VIPIUM_BOW, "Arc en vipium");
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

        this.addBlock(VObjects.VIPIUM_BLOCK, "Bloc de Vipium");
        this.addBlock(VObjects.PURE_VIPIUM_BLOCK, "Block de Vipium Pur");
        this.addBlock(VObjects.VIPIUM_ORE, "Minerai de Vipium");
        this.addBlock(VObjects.DEEPSLATE_VIPIUM_ORE, "Minerai de Vipium des Abîmes");
        this.addBlock(VObjects.VIPIUM_PURIFIER, "Purificateur de Vipium");
        this.addBlock(VObjects.VIPIUM_CRUSHER, "Concasseur de Vipium");

        this.add("container.vipium_purifier", "Purificateur de Vipium");
        this.add("container.vipium_purifier.stats.total_ingots", "Total des lingots purifiés: %d");

        this.add("container.vipium_crusher", "Concasseur de Vipium");
        this.add("container.vipium_crusher.start", "Démarrer le concasseur");
        this.add("container.vipium_crusher.stop", "Stopper le concasseur");
        this.add("container.vipium_crusher.stats.total_ingots", "Total des lingots concassés: %d");

        this.add("container.common.stats", "Fragments obtenables: ");
        this.add("container.common.stats.total_fragments", "Total des fragments obtenus: %d");
        this.add("container.common.stats.luck", "Votre chance: %s%%");
        this.add("container.common.stats.average_single", "Moyenne des fragments pour 1 lingot: %s");

        this.addBlock(VObjects.TELEPORTATION_ALTAR, "Altar de téléportation");
        this.addItem(VObjects.TELEPORTATION_ATLAS, "Atlas de téléportation");
        this.addItem(VObjects.HOLY_CHICKEN_BIBLE, "Bible du Saint Poulet");
        this.add("screen.configure_effects", "Configurer les effets");
        this.add("screen.atlas", "Atlas de téléportation");
        this.add("screen.altar", "Altar de téléportation");
        this.add("key.configure_effects", "Configurer les effets");
        this.add("vip3.setup", "Premier lancement de VIP3");
        this.add("vip3.setup.previous", "Précédent");
        this.add("vip3.setup.next", "Suivant");
        this.add("vip3.setup.done", "Configuration terminée !");
        this.add("vip3.setup.message.1", "Bienvenue sur la page de configuration de VIP3 !");
        this.add("vip3.setup.message.2", "Cette nouvelle saison apporte de nouvelles fonctionnalités");
        this.add("vip3.setup.message.3", "dont certaines nécessitent une configuration manuelle.");
        this.add("vip3.setup.message.4", "Pas d'inquiétude, c'est très simple !");
        this.add("vip3.setup.message.5", "Laissez vous guider par l'assistant de configuration et");
        this.add("vip3.setup.message.6", "tout se passera bien.");
        this.add("vip3.setup.message.7", "Garanti sans bug !");
        this.add("vip3.setup.message.8", "NOTE : certains paramètres définis ici ne sont pas réversibles.");
        this.add("vip3.setup.ass.intro", "Configurez ici les touches ASS (pause & reprendre / stop)");
        this.add("vip3.setup.ass.hint.1", "Si les touches ne se définissent pas, veillez à fermer les applications");
        this.add("vip3.setup.ass.hint.2", "qui pourraient interférer avec le jeu. (Spotify, YouTube, etc...)");
        this.add("vip3.setup.ass.pause_resume_key", "Pause/Reprendre");
        this.add("vip3.setup.ass.skip_key", "Passer");
        this.add("vip3.ass.title", "Configuration d'ASS");
        this.add("vip3.ass.menu", "Musique du menu");
        this.add("vip3.ass.game", "Musique du jeu");
        this.add("vip3.ass.default", "Par défaut");
        this.add("vip3.ass.error_title", "Erreur au chargement des sons");
        this.add("vip3.ass.error_message.1", "Certains fichiers dans le dossier vipsounds ne sont pas");
        this.add("vip3.ass.error_message.2", "au bon format. Il peut s'agir d'un problème d'extension");
        this.add("vip3.ass.error_message.3", "ou alors de nom tout simplement. Pour rappel, les fichiers");
        this.add("vip3.ass.error_message.4", "doivent respecter une convention de nommage :");
        this.add("vip3.ass.error_message.5", "Seuls les chiffres, minuscules, ' . ', ' _ ' et ' - ' sont autorisés.");
        this.add("vip3.ass.error_message.6", "Le nom du fichier ne doit pas dépasser 24 caractères.");
        this.add("vip3.undefined", "Indéfini");
    }
}
