package com.sit.client.ckhelper.entity;

import java.io.Serializable;

/**
 * @program: spring-starter
 * @description: 待统计的数据对象
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-15 17:52:待统计的数据对象
 **/
public class Statistics implements Serializable {
    private String title;

    public Statistics() {
    }

    public Statistics(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
