package com.disruptor;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 定义事件对象Order
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-03-08 11:32:
 **/
public class OrderEvent implements Serializable{
    private String id;
    private String name;
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
