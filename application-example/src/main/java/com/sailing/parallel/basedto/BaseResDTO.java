package com.sailing.parallel.basedto;

/**
 * @program: spring-starter
 * @description: 封装统一查询结果dto对象
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 17:01:
 **/
public class BaseResDTO<T extends Object>{

    // 用来区分DTO是具体哪个栏目的唯一标记，例如：新闻栏目newsDTO、用户栏目userDTO
    private String key;

    // 返回的data数据集合对象
    private T data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
