package com.tgl.designpattern.service.responsibilitychain.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("FilterTo")
public class FilterTo implements PrepareFilter {

    /**
     * 处理属于自己的业务实现
     * @param myParam 链条上的数据对象
     * @param filterChain 链条集合对象
     */
    @Override
    public void doFilter(MyParam myParam, MyFilterChain filterChain) {
        handle(myParam);

        filterChain.doFilter(myParam, filterChain);
    }

    @Override
    public Integer getShort() {
        return 2;
    }

    /**
     * 处理游泳的业务
     * @param myParam
     */
    private void handle(MyParam myParam){
        log.info("开始处理游泳业务:>{}", myParam);
    }
}
