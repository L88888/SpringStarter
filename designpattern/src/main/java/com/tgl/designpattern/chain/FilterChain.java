package com.tgl.designpattern.chain;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 过滤连控制接口，控制过滤链中下游数据的流转引擎。
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-05 21:08
 **/
@Slf4j
public class FilterChain {

    int disNum = 0;

    // 过滤器实现接口集合，这里可以存放任何过滤接口的实现对象
    List<Filter> filters = new ArrayList<>(1000);

    /**
     * 添加过滤器具体的实现对象
     * @param filter
     * @throws Exception
     */
    public void addFilter(Filter filter)throws Exception{
        if (Objects.isNull(filter)){
            throw new Exception("过滤器对象不能为空");
        }
        filters.add(filter);
    }

    /**
     * 控制整个过滤器实现对象的调度与执行
     * @param requestMessage 上一个过滤器对象的请求处理结果
     * @param responseMessage
     * @param filterChain
     */
    public void chain(RequestMessage requestMessage, ResponseMessage responseMessage, FilterChain filterChain){
        if (disNum == filters.size()){
            log.info("Filter链条上的过滤器实现对象已经全部流转完成.开始逆向处理响应报文的数据.");
            return;
        }
        Filter filter = filters.get(disNum);
        // 确定下一个过滤器的实现位置
        disNum++;
        filter.doFilter(requestMessage, responseMessage, filterChain);
    }
}
