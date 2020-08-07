package fr.flowarg.datagenerators.api;

import java.io.File;
import java.io.IOException;

public interface IModelGenerator extends IExecutable, IWriter, IIgnorable
{
    @Override
    void execute() throws Exception;

    @Override
    void ignore(String text);

    void generate(File workingDir) throws IOException;

    @Override
    void write(File file, String content) throws IOException;
}
