package fr.flowarg.vipium.client.data;

import fr.flowarg.datagenerators.api.IModelGenerator;
import fr.flowarg.datagenerators.api.markers.IBlockColumn;
import fr.flowarg.vipium.Main;
import fr.flowarg.vipium.common.handlers.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class ModelGeneratorImpl implements IModelGenerator
{
    private File itemsModels;
    private File blocksModels;
    private File blockStates;
    private final File workDir;

    public ModelGeneratorImpl()
    {
        this.workDir = new File("/home/flow/Documents/Developpement/Java/VIPMod/src/main/resources/assets/vipium/");
        Main.getInstance().getLogger().info(Main.getInstance().getMarker(), String.format("Initializing a new ModelGeneratorImpl at %s working dir...", this.workDir.getPath()));
    }

    @Override
    public void execute() throws IOException
    {
        this.itemsModels = new File(this.workDir, "/models/item/");
        this.blocksModels = new File(this.workDir, "/models/block/");
        this.blockStates = new File(this.workDir, "/blockstates/");
        this.generate(this.workDir);
    }

    @Override
    public void ignore() {}

    @Override
    public void ignore(String text)
    {
        Main.getInstance().getLogger().info(Main.getInstance().getMarker(), text);
    }

    @Override
    public void generate(File workingDir) throws IOException
    {
        for (RegistryObject<Block> blockObj : RegistryHandler.getBlocks().getEntries())
        {
            final Block block = blockObj.get();
            final File blockStatesBlockFile = new File(this.blockStates, block.getRegistryName().getPath() + ".json");
            if (!blockStatesBlockFile.exists())
            {
                final String toWrite = "{\n" +
                        "    \"variants\": {\n" +
                        "        \"\": { \"model\": \"" + Main.MODID + ":block/" + block.getRegistryName().getPath() + "\" }\n" +
                        "    }\n" +
                        "}";
                blockStatesBlockFile.createNewFile();
                this.write(blockStatesBlockFile, toWrite);
            }

            final File modelBlockFile = new File(this.blocksModels, block.getRegistryName().getPath() + ".json");
            if (!modelBlockFile.exists())
            {
                String toWrite;

                if (block instanceof IBlockColumn)
                {
                    toWrite = "{\n" +
                            "    \"parent\": \"block/cube_column\",\n" +
                            "    \"textures\": {\n" +
                            "        \"side\": \"" + Main.MODID + ":block/" + block.getRegistryName().getPath() + "_side\",\n" +
                            "        \"end\": \"" + Main.MODID + ":block/" + block.getRegistryName().getPath() + "_top\"\n" +
                            "    }\n" +
                            "}";
                }
                else
                {
                    toWrite = "{\n" +
                            "    \"parent\": \"block/cube_all\",\n" +
                            "    \"textures\": {\n" +
                            "        \"all\": \"" + Main.MODID + ":block/" + block.getRegistryName().getPath() + "\"\n" +
                            "    }\n" +
                            "}\n";
                }
                modelBlockFile.createNewFile();
                this.write(modelBlockFile, toWrite);
            }

            final File modelBlockItemFile = new File(this.itemsModels, block.getRegistryName().getPath() + ".json");
            if (!modelBlockItemFile.exists())
            {
                final String toWrite = "{\n" +
                        "    \"parent\": \"" + Main.MODID + ":block/" + block.getRegistryName().getPath() + "\"\n" +
                        "}\n";
                modelBlockItemFile.createNewFile();
                this.write(modelBlockItemFile, toWrite);
            }
        }

        for(RegistryObject<Item> itemObject : RegistryHandler.getItems().getEntries())
        {
            final Item item = itemObject.get();
            if (!(item instanceof BlockItem))
            {
                final File modelItemFile = new File(this.itemsModels, item.getRegistryName().getPath() + ".json");
                if (!modelItemFile.exists())
                {
                    String toWrite;
                    if (item instanceof ToolItem)
                    {
                        toWrite = "{\n" +
                                "    \"parent\": \"minecraft:item/handheld\",\n" +
                                "    \"textures\": {\n" +
                                "        \"layer0\": \"" + Main.MODID + ":item/" + item.getRegistryName().getPath() + "\"\n" +
                                "    }\n" +
                                "}";
                    }
                    else toWrite = "{\n" +
                            "    \"parent\": \"minecraft:item/generated\",\n" +
                            "    \"textures\": {\n" +
                            "        \"layer0\": \"" + Main.MODID + ":item/" + item.getRegistryName().getPath() + "\"\n" +
                            "    }\n" +
                            "}";

                    modelItemFile.createNewFile();
                    this.write(modelItemFile, toWrite);
                }
            }
            else this.ignore("Ignoring item-block " + item.getRegistryName().getPath() + "...");
        }
    }

    @Override
    public void write(File file, String content) throws IOException
    {
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }
}
