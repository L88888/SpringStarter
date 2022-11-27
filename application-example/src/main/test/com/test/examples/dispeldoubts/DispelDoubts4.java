package com.test.examples.dispeldoubts;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @program: spring-starter
 * @description: 数字计算
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-01-21 12:00:
 **/
public class DispelDoubts4 {

    /**
     * 判断输入数字是否是奇数
     * num % 2 == 1
     */
    private void getJz(int num){
        if (num % 2 == 1){
            System.out.println("输入数字是奇数:" + num);
        }else {
            System.out.println("输入数字不是奇数:" + num);
        }

        // 使用操作符&and的方式进行判断输入数字是否是奇数
        if ((num & 1) != 0){
            System.out.println("输入数字是奇数:" + num);
        }else {
            System.out.println("输入数字不是奇数:" + num);
        }

        int bound = 9;
        int m = bound - 1;
        // todo 判断数字是否可以被2整除
        if ((bound & m) == 0){
            System.out.println("是2的幂等次方:" + bound);
        }else {
            System.out.println("非2的幂等次方:" + bound);
        }
    }

    @Test
    public void getJz(){
        getJz(-2);
    }

    /**
     * 小数减运算
     */
    @Test
    public void jsxs(){
        System.out.println(2.00 - 1.10);

        BigDecimal res = new BigDecimal("2.00").
                subtract(new BigDecimal("1.10"));

        System.out.println("正确的输出结果:>" + res);
    }

    /**
     * 专门用来做小数的(高精准度)减价乘除计算
     */
    @Test
    public void xsAddSubtractMultiplyDivide(){
        String oneVal = "12.304";
        String toVal = "11.304";

        // 小数加法
        BigDecimal res1 = new BigDecimal(oneVal).add(new BigDecimal(toVal));
        System.out.println("小数加法结果为:>" + res1);

        // 小数乘法
        BigDecimal res2 = new BigDecimal(oneVal).multiply(new BigDecimal(toVal));
        System.out.println("小数乘法结果为:>" + res2);

        // 小数除法
        toVal = "0.1";
        BigDecimal res3 = new BigDecimal(oneVal).divide(new BigDecimal(toVal));
        System.out.println("小数除法结果为:>" + res3);
    }

    /**
     * 两个数字进行交换（使用异或的方式对两个数字做互换）
     */
    @Test
    public void getExchange(){
        int i = 900;
        int j = 1234;
        System.out.println("befVal:>>i=" + i + ",j=" + j);

        i = i ^ j; // i = 900 ^ 1234,j = 1234
        j = j ^ i; // j = 1234 ^ 900 ^ 1234, i = 900 ^ 1234;得出：j = 900,i = 900 ^ 1234
        i = j ^ i; // i = 900 ^ 900 ^ 1234; 得出：i = 1234,j = 900

        System.out.println("alterVal:>>i=" + i + ",j=" + j);
    }

    /**
     * 三目运算的结果值必须要保证，"数据结果类型一致"
     */
    @Test
    public void dosEquis(){
        char x = 'X';
        int i = 0;
        System.out.println(true ? x : 0);
        System.out.println(false ? i : x);
        System.out.println(false ? 0 : x);
    }

    /**
     * 类型转换问题
     */
    public void typeCon(){
        short x = 0;
        int i = 123456;
//        x = i;
    }

    /**
     * 字符串拼接
     */
    @Test
    public void zfpj(){
        System.out.println("H" + "aa");
        System.out.println('1' + 'a');
        System.out.println("" + '1' + 'a');
    }

    /**
     * char[] 与字符串拼接
     */
    @Test
    public void zfcpj(){
        String letters = "ABC";
        char[] temp = {'1','2','3'};
        System.out.println(letters + "easy as " + temp);
        System.out.println(letters + "easy as " + String.valueOf(temp));
    }

    @Test
    public void printClassName(){
        System.out.println(DispelDoubts4.class.getName());
        System.out.println(DispelDoubts4.class.isArray());
    }

    /**
     * 随机值获取方式与switch，case的使用方式
     */
    @Test
    public void randomSwitch(){
        Random random = new Random();
        StringBuilder stringBuilder = null;
        switch (random.nextInt(3)){
            case 1: stringBuilder = new StringBuilder("P");
            break;
            case 2: stringBuilder = new StringBuilder("G");
                break;
            default: stringBuilder = new StringBuilder("M");
        }
        stringBuilder.append("a");
        stringBuilder.append("i");
        stringBuilder.append("n");
        System.out.println(stringBuilder.toString());
    }

    /**
     * 循环赋值操作
     * j = j++;
     */
    @Test
    public void xunhuanAssignment(){
        int j = 0;
        for (int i =0;i< 100;i++){
            j = j++;
        }

        // j = j++ 等同于以下程序序列
        // int temp = j;
        // j = j + 1;
        // j = temp;
        System.out.println("循环赋值>>" + j);
    }
}
