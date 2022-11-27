package com.tgl.designpattern.service.responsibilitychain.spring;

/**
 * 预处理链接口
 */
public interface PrepareFilter {

    /**
     * 每个责任方需要独立实现的方法
     * @param myParam 链条上的数据对象
     * @param filterChain 链条集合对象
     */
    public void doFilter(MyParam myParam, MyFilterChain filterChain);

    /**
     * 责任链的序号,可以控制具体实现类的执行顺序谁先执行，谁后执行。
     * @return
     */
    public Integer getShort();
}
