package com.tgl.designpattern.service.responsibilitychain.inherit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service("FilterToCopy")
public class FilterTos extends PrepareFilters {

    /**
     * 处理属于自己的业务实现
     * @param myParam 链条上的数据对象
     */
    @Override
    public void doFilter(MyParams myParam) {
        handle(myParam);

        if (!Objects.isNull(this.next)){
            this.next.doFilter(myParam);
        }
    }

    @Override
    public Integer getShort() {
        return 2;
    }

    /**
     * 处理游泳的业务
     * @param myParam
     */
    private void handle(MyParams myParam){
        log.info("开始处理游泳业务s:>{}", myParam);
    }
}
