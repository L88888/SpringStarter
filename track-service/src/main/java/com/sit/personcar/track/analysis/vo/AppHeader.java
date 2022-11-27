package com.sit.personcar.track.analysis.vo;

import java.io.Serializable;

/**
 * 查询服务响应头对象二次封装
 */
public class AppHeader implements Serializable {

    public AppHeader(){}

    public AppHeader(String status, String statusMsg){
        this.status = status;
        this.statusMsg = statusMsg;
    }

    private String status;
    private String statusMsg;
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
    public String getStatusMsg() {
        return statusMsg;
    }

    @Override
    public String toString() {
        return "AppHeader{" +
                "status='" + status + '\'' +
                ", statusMsg='" + statusMsg + '\'' +
                '}';
    }
}
