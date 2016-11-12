package io.linuxserver.davos.converters;

public interface Converter<S, T> {

    T convertTo(S source);
    
    S convertFrom(T source);
}
