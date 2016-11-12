package io.linuxserver.davos.web.selectors;

public enum MethodSelector {

    GET, POST, PUT, DELETE;
    
    public static final MethodSelector[] ALL = { GET, POST, PUT, DELETE };
}
