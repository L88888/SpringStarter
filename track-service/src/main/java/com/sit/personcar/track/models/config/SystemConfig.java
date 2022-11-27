package com.sit.personcar.track.models.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gdyang
 * @date 2022/5/18 5:21 下午
 * Spring Boot Configuration Annotation Processor not found in classpath
 */
@Component
@ConfigurationProperties(prefix = "business.track")
public class SystemConfig {

    /**
     * 服务方访问地址
     */
    private String endpoint;

    private String username;

    private String password;

    private String senderId;

    private String endUserName;

    private String endUserIdCard;

    private String endUserDepartment;

    private String endUserCertificate;

    private String endUserDeviceId;

    private List<KeysConfigs> configs;


    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<KeysConfigs> getConfigs() {
        return configs;
    }

    public void setConfigs(List<KeysConfigs> configs) {
        this.configs = configs;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
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
        this.endUserDepartment = endUserDepartment;
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

    public static class KeysConfigs {
        /**
         * 查询服务Id
         */
        private String serviceId;

        /**
         * 查询服务主键字段集合，air_seg_flt_nbr@air_seg_dpt_dt_lcl@pas_id_nbr
         */
        private String keyFileds;

        /**
         * 查询服务的排序字段，默认倒序：air_seg_dpt_dt_lcl-
         */
        private String sortFiled;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getKeyFileds() {
            return keyFileds;
        }

        public void setKeyFileds(String keyFileds) {
            this.keyFileds = keyFileds;
        }

        public String getSortFiled() {
            return sortFiled;
        }

        public void setSortFiled(String sortFiled) {
            this.sortFiled = sortFiled;
        }

        @Override
        public String toString() {
            return "KeysConfigs{" +
                    "serviceId='" + serviceId + '\'' +
                    ", keyFileds='" + keyFileds + '\'' +
                    '}';
        }
    }
}
