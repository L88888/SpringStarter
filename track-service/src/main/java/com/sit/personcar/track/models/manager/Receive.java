package com.sit.personcar.track.models.manager;

/**
 * 用来接收服务段返回的异步结果对象
 * AsyncHttpClient
 */
public class Receive {

    private String body;

    private String serverId;

    private int statusCode;

    private String url;

    public Receive() {}

    public Receive(String body, String serverId) {
        this.body = body;
        this.serverId = serverId;
    }

    public Receive(String body, String serverId, int statusCode) {
        this.body = body;
        this.serverId = serverId;
        this.statusCode = statusCode;
    }

    public Receive(String body, String serverId, int statusCode, String url) {
        this.body = body;
        this.serverId = serverId;
        this.statusCode = statusCode;
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public String getServerId() {
        return serverId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getUrl() {
        return url;
    }

    public Receive setBody(String body) {
        this.body = body;
        return this;
    }

    public Receive setServerId(String serverId) {
        this.serverId = serverId;
        return this;
    }

    public Receive setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Receive setUrl(String url) {
        this.url = url;
        return this;
    }
}
