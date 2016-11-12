package io.linuxserver.davos.web;

import io.linuxserver.davos.web.selectors.MethodSelector;

public class APIView {

    private Long id;
    private String url;
    private MethodSelector method = MethodSelector.POST;
    private String contentType;
    private String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MethodSelector getMethod() {
        return method;
    }

    public void setMethod(MethodSelector method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
