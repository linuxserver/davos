package io.linuxserver.davos.web.selectors;

public enum TransferSelector {

    FILES, RECURSIVE;
    
    public static final TransferSelector[] ALL = { FILES, RECURSIVE };
}
