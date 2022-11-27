package com.test.examples.dispeldoubts;

import org.junit.Test;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Random;

/**
 * @program: spring-starter
 * @description: bitmap应用
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-01-23 23:30:
 **/
public class DispelDoubts5 {

    int codeLen = 400;

    /**
     *  存储区域信息,区域code与区域名称一一对应
     *  区域code范围从100开始到500结束
     *  key：areaCode,123
     *  value: 科技路-123
     */
    private HashMap areaInfo = new HashMap(codeLen);

    /**
     * 存储区域code信息，区域code范围从100开始到500结束
     */
    private int[] areaCode = new int[codeLen];

    int areaCodeStart = 100;
    int areaCodeEnd = 500;

    /**
     * 初始化区域跟区域code,且区域跟区域code是一一对应关系
     */
    private void initAreaInfo(){
        int j = 0;
        for (int i = areaCodeStart;i < areaCodeEnd;i++){
            areaCode[j] = i;
//            areaInfo.put(i, "西影路" + i);
            j++;
        }

        System.out.println("当前内存中存储的区域Code数量:>" + areaCode.length);
        System.out.println("当前内存中存储的区域对象数量:>" + areaInfo.size());
    }

    /**
     * 处理手机号与区域code数据对象存储
     * 存储至bitmap位图中，默认都是false，实际应用过程中通过取反来判断是否存在手机号
     */
    private void processData(){
        // 1300000 -- 1999999
//        int startNumtt = 2100000000;
        int startNum = 1300000;
        int endNum = 1999999;
        endNum = 1300005;
        int len = 700000;
        BitSet bitSet = new BitSet(len);
//        BitSet bitSet = new BitSet(Integer.MAX_VALUE);//hashcode的值域

        Random random = new Random();
        if (true){
            for (int i = startNum;i <= endNum;i++){
                // 随机获取一个区域code值，并且联合手机号一起生成一个hashcode值
//                System.out.println(i + "" + areaCode[random.nextInt(codeLen)]);
//                System.out.println((i + "" + areaCode[random.nextInt(codeLen)]).hashCode());
                bitSet.set((i + areaCode[random.nextInt(codeLen)]));
//                bitSet.set(i);
            }
        }

        String url = "http://baidu.com/a";
        int hashcode1 = url.hashCode() & 0x7FFFFFFF;
        System.out.println("baidu.com/a:>" + hashcode1);
        // 11 2441 6622

//        bitSet.set(endNum);
//        bitSet.set(2000000000);
//        bitSet.set(2000000020);
//        bitSet.set(2000000003);
//        bitSet.set(2000000004);
//        bitSet.set(2000000005);
        System.out.println("当前内存中存储的位图数量:>" + bitSet.cardinality());
        System.out.println("当前内存中存储的数值长度:>" + bitSet.size());
    }

    @Test
    public void testData(){
        // 阻塞主线程
//        if (true){
//            try {
//                Thread.sleep(1000 * 1000 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        // jvm内存总大小
        long total = Runtime.getRuntime().totalMemory();
        // jvm内存已使用大小
        long free = Runtime.getRuntime().freeMemory();
        long beforeVal = total - free;
        System.out.println("beforeUsedMemory:>{%,}"+""+ beforeVal);

        initAreaInfo();
        processData();

        // jvm内存总大小
        long totalAfter = Runtime.getRuntime().totalMemory();
        // jvm内存已使用大小
        long freeAfter = Runtime.getRuntime().freeMemory();
        long afterVal = totalAfter - freeAfter;
        System.out.println("afterUsedMemory:>{%}" + afterVal);
        // 程序目前已使用内存大小
        long use = (afterVal - beforeVal) / 1024 / 1024;

        System.out.println("total={%},free={%},use={%}(MB),"+ total +","+ free+","+use);
        try {
            Thread.sleep(1000 * 1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
