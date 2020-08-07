package fr.flowarg.datagenerators.api;

import java.io.File;
import java.io.IOException;

public interface IModelFixer extends IModelGenerator, IIgnorable
{
    @Override
    void ignore();

    @Override
    void execute() throws Exception;

    void fix(File toFix) throws Exception;

    @Override
    void generate(File workingDir) throws IOException;
}
