package com.tgl.designpattern.service.responsibilitychain.spring;

import java.io.Serializable;

public class MyParam implements Serializable {

    // 姓名
    private String xm;
    // 性别
    private String xb;
    // 人员编号
    private String rybh;
    // 出生日期
    private String csrq;
    // 公民身份号码
    private String gmsfhm;
    // 户籍地详址
    private String hjdxz;
    // 人脸图片Base64
    private String imageBase64;
    // 库ID
    private String libId;
    // 人脸ID
    private String faceId;
    // 证件类型（预留）
    private String zjlx;
    // 民族（预留）
    private String mz;
    // 现住址（预留）
    private String xzz;

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getRybh() {
        return rybh;
    }

    public void setRybh(String rybh) {
        this.rybh = rybh;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getGmsfhm() {
        return gmsfhm;
    }

    public void setGmsfhm(String gmsfhm) {
        this.gmsfhm = gmsfhm;
    }

    public String getHjdxz() {
        return hjdxz;
    }

    public void setHjdxz(String hjdxz) {
        this.hjdxz = hjdxz;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getLibId() {
        return libId;
    }

    public void setLibId(String libId) {
        this.libId = libId;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getXzz() {
        return xzz;
    }

    public void setXzz(String xzz) {
        this.xzz = xzz;
    }

    @Override
    public String toString() {
        return "CzryInfo{" +
                "xm='" + xm + '\'' +
                ", xb='" + xb + '\'' +
                ", rybh='" + rybh + '\'' +
                ", csrq='" + csrq + '\'' +
                ", gmsfhm='" + gmsfhm + '\'' +
                ", hjdxz='" + hjdxz + '\'' +
                ", imageBase64='" + imageBase64 + '\'' +
                ", libId='" + libId + '\'' +
                ", faceId='" + faceId + '\'' +
                ", zjlx='" + zjlx + '\'' +
                ", mz='" + mz + '\'' +
                ", xzz='" + xzz + '\'' +
                '}';
    }

}
