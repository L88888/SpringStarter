package com.sailing.tgl.tests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class Immutable {

    /**
     * 不可变容器大小
     */
    @Test
    public void immutableList(){
        List dataTemp = new ArrayList(100);
        dataTemp.add("123qwe");
        dataTemp.add("124qwe");
        dataTemp.add("125qwe");
        dataTemp.add("126qwe");
        modify(dataTemp);
        // 不可变集合框架google.guava
        ImmutableList  res = ImmutableList.copyOf(dataTemp);
        dataTemp.add("127qwsq");
        System.out.println("res data is value:>{}" + res.toString());

//        Lists.asList();
    }

    void modify(List dataTemp){
        dataTemp.add("12312312");
    }
}
