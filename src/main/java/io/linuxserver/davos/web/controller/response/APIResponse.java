package io.linuxserver.davos.web.controller.response;

public class APIResponse {

    public String status = "OK";
    public Object body;

    public APIResponse withBody(Object body) {

        this.body = body;
        return this;
    }

    public APIResponse withStatus(String status) {
        this.status = status;
        return this;
    }
}
