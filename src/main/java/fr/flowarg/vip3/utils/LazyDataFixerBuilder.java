package fr.flowarg.vip3.utils;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;

import java.util.concurrent.Executor;

// https://github.com/astei/lazydfu/blob/25ed4a75947bce9796f90a99b4232f7884c1b54b/src/main/java/me/steinborn/lazydfu/mod/LazyDataFixerBuilder.java
@CalledAtRuntime
public class LazyDataFixerBuilder extends DataFixerBuilder
{
    public LazyDataFixerBuilder(int worldVersion)
    {
        super(worldVersion);
    }

    @Override
    public DataFixer build(Executor executor)
    {
        return super.build(command -> {});
    }
}
