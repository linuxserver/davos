package io.linuxserver.davos.web.selectors;

public enum TransferSelector {

    FILE, RECURSIVE;
    
    public static final TransferSelector[] ALL = { FILE, RECURSIVE };
}
