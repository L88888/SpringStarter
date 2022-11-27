package com.sit.personcar.track.analysis.entity;

/**
 * 响应头对象数据
 */
public class App_header {

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

}
