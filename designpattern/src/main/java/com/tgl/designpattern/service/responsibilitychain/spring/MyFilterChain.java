package com.tgl.designpattern.service.responsibilitychain.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 将所有链条穿起来
 */
@Slf4j
@Service("MyFilterChain")
public class MyFilterChain implements PrepareFilter, ApplicationContextAware {

    private static List<PrepareFilter> perpareFilterList;

    private static Integer index = 0;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取指定接口的所有实现类对象
        Map<String, PrepareFilter> serviceMap = applicationContext.getBeansOfType(PrepareFilter.class);
        perpareFilterList = new ArrayList<>(serviceMap.values());
        log.info("动态加载过滤链条上所有的实现类对象:>{}", perpareFilterList);

        // 把所有实现类型进行倒序排列
        // stream() --> 并行流遍历
        // sorted() --> 容器内方法或者属性比较排序，默认倒序输出
        // collect() --> 将最终比较排序完的数据进行组装返回组装后的容器集合
        perpareFilterList = perpareFilterList
                .stream()
                .sorted(Comparator.comparing(PrepareFilter::getShort))
                .collect(Collectors.toList());
    }

    /**
     * 执行下一个链条上的实现对象
     * @param myParam 链条上的数据对象
     * @param filterChain 链条集合对象
     */
    @Override
    public void doFilter(MyParam myParam, MyFilterChain filterChain) {
        if (Objects.isNull(perpareFilterList)){
            log.info("当前链条索引不符合规范{}", perpareFilterList.toString());
            return;
        }

        index = ++index;
        if (index < perpareFilterList.size()){
            // 获取下一个链条实现类
            perpareFilterList.get(index).doFilter(myParam, filterChain);
        }else {
            log.info("过滤连条实现类已执行完{}", index);
        }
    }

    /**
     * 主链条下标默认为0，主链条只做引擎调度，同时指向下一个链条实现类
     * @return
     */
    @Override
    public Integer getShort() {
        return 0;
    }
}
