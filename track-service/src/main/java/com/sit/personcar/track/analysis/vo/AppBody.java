package com.sit.personcar.track.analysis.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 查询服务结果对象二次封装
 */
public class AppBody implements Serializable {

    public AppBody(){}

    public AppBody(String count, String totalCount, List<Map<String, String>> result){
        this.count = count;
        this.totalCount = totalCount;
        this.result = result;
    }

    /**
     * 当前页大小
     */
    private String count;

    /**
     * 总记录数
     */
    private String totalCount;

    /**
     * 响应结果集合
     */
    private List<Map<String, String>> result;
    public void setCount(String count) {
        this.count = count;
    }
    public String getCount() {
        return count;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
    public String getTotalCount() {
        return totalCount;
    }

    public List<Map<String, String>> getResult() {
        return result;
    }

    public void setResult(List<Map<String, String>> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "AppBody{" +
                "count='" + count + '\'' +
                ", totalCount='" + totalCount + '\'' +
                ", result=" + result +
                '}';
    }
}
