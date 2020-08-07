package fr.flowarg.datagenerators.api;

import java.io.File;
import java.io.IOException;

@FunctionalInterface
public interface IWriter
{
    void write(File file, String content) throws IOException;
}
