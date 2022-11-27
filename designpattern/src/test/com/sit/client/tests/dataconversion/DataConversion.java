package com.sit.client.tests.dataconversion;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DataConversion {

    @Test
    public void t1(){
        Map temp1 = new HashMap(10);
        temp1.put("ww1","123asd");
        temp1.put("ww2","123asd");
        temp1.put("ww3","123asd");
        temp1.put("ww4","123asd");
        temp1.put("ww4","123asd");

        List temp2 = new ArrayList(temp1.values());
        log.info("{}", temp2);
    }
}
