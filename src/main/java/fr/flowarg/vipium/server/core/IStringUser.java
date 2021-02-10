package fr.flowarg.vipium.server.core;

import java.util.List;

public interface IStringUser
{
    default String toString(List<String> strings)
    {
        final StringBuilder sb = new StringBuilder();
        strings.forEach(sb::append);
        return sb.toString();
    }
}
