package fr.flowarg.datagenerators;

import fr.flowarg.datagenerators.api.IModelFixer;
import fr.flowarg.datagenerators.api.IModelGenerator;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DataGenerators
{
    private static final Set<IModelGenerator> MODEL_GENERATORS = new HashSet<>();
    private static final Set<IModelFixer> MODEL_FIXERS = new HashSet<>();
    private static final List<String> DOMAINS = new ArrayList<>();

    public static void registerDomain(String modid)
    {
        DOMAINS.add(modid);
        if (DOMAINS.size() == 1) MinecraftForge.EVENT_BUS.register(new ModelLoadingListener());
    }

    public static void addModelFixer(IModelFixer fixer)
    {
        if (MODEL_FIXERS.contains(fixer)) throw new IllegalArgumentException("Given argument already exist!");
        else MODEL_FIXERS.add(fixer);
    }

    public static void addModelGenerator(IModelGenerator generator)
    {
        if (MODEL_GENERATORS.contains(generator)) throw new IllegalArgumentException("Given argument already exist!");
        else MODEL_GENERATORS.add(generator);
    }

    static void generate()
    {
        MODEL_GENERATORS.forEach(generator -> {
            try
            {
                generator.execute();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    static void fix()
    {
        MODEL_FIXERS.forEach(fixer -> {
            try
            {
                fixer.execute();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }
}
