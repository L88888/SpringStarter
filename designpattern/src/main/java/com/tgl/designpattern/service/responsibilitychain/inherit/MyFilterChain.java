package com.tgl.designpattern.service.responsibilitychain.inherit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * 将所有链条穿起来
 */
@Slf4j
@Service
public class MyFilterChain {

    @Bean
    public PrepareFilters autoPrepareFilter(){
        FilterOnes filterOne = new FilterOnes();
        FilterTos filterTo = new FilterTos();
        FilterThrees filterThree = new FilterThrees();

        filterOne.setNext(filterTo).setNext(filterThree);
        return filterOne;
    }
}