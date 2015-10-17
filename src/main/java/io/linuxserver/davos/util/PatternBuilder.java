package io.linuxserver.davos.util;

public class PatternBuilder {

    public static String buildFromFilterString(String filter) {
        return filter.replaceAll("\\?", ".{1}").replaceAll("\\*", ".+");
    }
}
