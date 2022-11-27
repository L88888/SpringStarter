package com.sit.personcar.track.analysis.entity;

import org.asynchttpclient.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 请求头heads
 */
public class RequestHeads{

    private String senderId;
    private String serviceId;
    private String endUserName;
    private String endUserIdCard;
    private String endUserDepartment;
    private String endUserCertificate;
    private String endUserDeviceId;
    private String reason;
    private String timeout;
    private String timestamp;

    /**
     * 自定义请求头信息heads
     * sender_id
     * service_id
     * end_user.name
     * end_user.id_card
     * end_user.department
     * end_user.certificate
     * end_user.device_id
     * reason
     * timeout
     * timestamp
     * @param request
     * @return
     */
    public Request getHttpHeaders(Request request){
        request.getHeaders().set("sender_id", getSenderId())
                .set("service_id", getServiceId())
                .set("end_user.name", getEndUserName())
                .set("end_user.id_card", getEndUserIdCard())
                .set("end_user.department", getEndUserDepartment())
                .set("end_user.certificate", getEndUserCertificate())
                .set("end_user.device_id", getEndUserDeviceId())
                .set("timestamp", getTimestamp());
        return request;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
        // URL编码
        // %E5%88%98%E5%B0%8F%E6%B3%95
//        try {
//            this.endUserName =  URLEncoder.encode(endUserName, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            this.endUserName =  "%E5%88%98%E5%B0%8F%E6%B3%95";
//        }
    }

    public String getEndUserIdCard() {
        return endUserIdCard;
    }

    public void setEndUserIdCard(String endUserIdCard) {
        this.endUserIdCard = endUserIdCard;
    }

    public String getEndUserDepartment() {
        return endUserDepartment;
    }

    public void setEndUserDepartment(String endUserDepartment) {
        this.endUserDepartment =  endUserDepartment;
        // URL编码
//        try {
//            this.endUserDepartment =  URLEncoder.encode(endUserDepartment, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            this.endUserDepartment =  endUserDepartment;
//        }
    }

    public String getEndUserCertificate() {
        return endUserCertificate;
    }

    public void setEndUserCertificate(String endUserCertificate) {
        this.endUserCertificate = endUserCertificate;
    }

    public String getEndUserDeviceId() {
        return endUserDeviceId;
    }

    public void setEndUserDeviceId(String endUserDeviceId) {
        this.endUserDeviceId = endUserDeviceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason =  reason;
        // URL编码
//        try {
//            this.reason =  URLEncoder.encode(reason, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            this.reason =  reason;
//        }
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return localDateTime.format(dateTimeFormatter);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}