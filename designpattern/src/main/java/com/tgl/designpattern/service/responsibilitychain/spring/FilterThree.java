package com.tgl.designpattern.service.responsibilitychain.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("FilterThree")
public class FilterThree implements PrepareFilter {

    /**
     * 处理属于自己的业务实现
     * @param myParam 链条上的数据对象
     * @param filterChain 链条集合对象
     */
    @Override
    public void doFilter(MyParam myParam, MyFilterChain filterChain) {
        downImage(myParam);

        filterChain.doFilter(myParam, filterChain);
    }

    @Override
    public Integer getShort() {
        return 3;
    }

    /**
     * 处理下图的业务
     * @param myParam
     */
    private void downImage(MyParam myParam){
        log.info("开始处理下图业务:>{}", myParam);
    }
}
