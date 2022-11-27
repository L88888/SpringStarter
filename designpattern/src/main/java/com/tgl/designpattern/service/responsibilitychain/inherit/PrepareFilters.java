package com.tgl.designpattern.service.responsibilitychain.inherit;

import org.springframework.stereotype.Service;

/**
 * 预处理链接口
 */
@Service
public abstract class PrepareFilters {

    protected PrepareFilters next;

    public PrepareFilters setNext(PrepareFilters prepareFilter){
        this.next = prepareFilter;
        return next;
    }

    /**
     * 每个责任方需要独立实现的方法
     * @param myParam 链条上的数据对象
     */
    public abstract void doFilter(MyParams myParam);

    /**
     * 责任链的序号,可以控制具体实现类的执行顺序谁先执行，谁后执行。
     * @return
     */
    public Integer getShort(){
        return 0;
    }
}
