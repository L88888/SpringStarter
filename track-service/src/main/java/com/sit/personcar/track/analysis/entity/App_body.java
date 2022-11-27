package com.sit.personcar.track.analysis.entity;

import java.util.List;

/**
 * 响应报文对象
 */
public class App_body {

    private String count;
    private String totalCount;
    private List<List<String>> result;
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

    public void setResult(List<List<String>> result) {
        this.result = result;
    }
    public List<List<String>> getResult() {
        return result;
    }
}
