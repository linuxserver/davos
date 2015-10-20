package io.linuxserver.davos.dto.converters;

public interface Converter<S, T> {

    T convert(S source);
}
