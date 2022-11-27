package com.tgl.designpattern.service.responsibilitychain.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("FilterOne")
public class FilterOne implements PrepareFilter {

    /**
     * 处理属于自己的业务实现
     * @param myParam 链条上的数据对象
     * @param filterChain 链条集合对象
     */
    @Override
    public void doFilter(MyParam myParam, MyFilterChain filterChain) {
        manage(myParam);

        // 如果下一个链条实现类不想被执行那么直接可以在这里进行return
        if (false){
            return;
        }

        filterChain.doFilter(myParam, filterChain);
    }

    @Override
    public Integer getShort() {
        return 1;
    }

    /**
     * 处理写字的业务
     * @param myParam
     */
    private void manage(MyParam myParam){
        log.info("开始处理写字业务:>{}", myParam);
    }
}
