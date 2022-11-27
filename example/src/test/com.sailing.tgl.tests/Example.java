package com.sailing.tgl.tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-09-28 14:28:
 **/
public class Example {

    int temp = 1;

    @Test
    public void testPj(){
        List<Org> orgs = new ArrayList<>();
        Org org = null;
        for (int i =0; i < 100000;i++){
            org = new Org();
            org.setProvinceId(i + "");
            orgs.add(org);
        }

        long start = System.currentTimeMillis();
        this.buildProvince(orgs);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    StringBuilder sb = new StringBuilder();
    String buildProvince(List<Org> orgs){
        if (temp == 1){
            for(Org org:orgs){
                sb.append(org.getProvinceId());
                sb.append(",");
            }
        }

        if (temp == 2){
            orgs.stream().forEach(v->{
                sb.append(v.getProvinceId());
                sb.append(",");
            });
        }

        if (temp == 3){
            orgs.parallelStream().forEach(v->{
                sb.append(v.getProvinceId());
                sb.append(",");
            });
        }

        sb = sb.delete(sb.lastIndexOf(","), sb.length());
        return sb.toString();
    }
}

class Org{
    private String provinceId;

    public void setProvinceId(String provinceId){
        this.provinceId = provinceId;
    }

    public String getProvinceId(){
        return provinceId;
    }
}