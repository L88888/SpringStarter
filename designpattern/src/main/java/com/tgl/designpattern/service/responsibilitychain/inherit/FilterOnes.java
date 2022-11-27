package com.tgl.designpattern.service.responsibilitychain.inherit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Slf4j
@Service("FilterOneCopy")
public class FilterOnes extends PrepareFilters {

    /**
     * 处理属于自己的业务实现
     * @param myParam 链条上的数据对象
     */
    @Override
    public void doFilter(MyParams myParam) {
        manage(myParam);

        // 如果下一个链条实现类不想被执行那么直接可以在这里进行return
//        if (false){
//            return;
//        }

        if (!Objects.isNull(this.next)){
            this.next.doFilter(myParam);
        }
    }

    @Override
    public Integer getShort() {
        return 1;
    }

    /**
     * 处理写字的业务
     * @param myParam
     */
    private void manage(MyParams myParam){
        log.info("开始处理写字业务s:>{}", myParam);
    }
}
