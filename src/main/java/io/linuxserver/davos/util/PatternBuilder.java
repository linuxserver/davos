package io.linuxserver.davos.util;

public class PatternBuilder {

    public static String buildFromFilterString(String filter) {
        return filter.replace(".", "\\.").replaceAll("\\?", ".{1}").replaceAll("\\*", ".+");
    }
}
