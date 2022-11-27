package com.tgl.designpattern.chain;

/**
 * @program: spring-starter
 * @description: 请求报文对象
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-05 21:14:
 **/
public class RequestMessage {

    private String reqBodyVal;

    public String getReqBodyVal() {
        return reqBodyVal;
    }

    public void setReqBodyVal(String reqBodyVal) {
        this.reqBodyVal = reqBodyVal;
    }
}
