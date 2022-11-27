package com.sit.personcar.track.analysis.vo;

import java.io.Serializable;

/**
 * 查询服务结果集合对象二次封装
 */
public class ResponseViewData implements Serializable {

    public ResponseViewData(){}

    public ResponseViewData(AppHeader app_header, AppBody app_body, String serverId){
        this.app_header = app_header;
        this.app_body = app_body;
        this.serverId = serverId;
    }

    /**
     * 查询服务二次封装后的响应头信息
     */
    private AppHeader app_header;

    /**
     * 查询服务二次封装后的结果信息
     */
    private AppBody app_body;

    /**
     * 查询服务ID
     */
    private String serverId;

    public void setApp_header(AppHeader app_header) {
        this.app_header = app_header;
    }
    public AppHeader getApp_header() {
        return app_header;
    }

    public void setApp_body(AppBody app_body) {
        this.app_body = app_body;
    }
    public AppBody getApp_body() {
        return app_body;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "ResponseViewData{" +
                "app_header=" + app_header +
                ", app_body=" + app_body +
                ", serverId='" + serverId + '\'' +
                '}';
    }
}
