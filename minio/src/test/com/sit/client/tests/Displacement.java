package com.sit.client.tests;

import org.junit.Test;

/**
 * @program: spring-starter
 * @description: 位移操作
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-11-23 10:44:位移操作
 **/
public class Displacement {

    @Test
    public void test1(){
        long t = 8;
        // 相当于 8 * 2 * 2 * 2
        t = t << 3;
        System.out.println(t);

        long t1 = 8;
        // 相当于除以2 8/2/2/2
        t1 = t1 >> 3;
        System.out.println(t1);

        long cf = 1;
        // 计算2的16次方等于65536,2的32次方等于
        // 1(基数cf) * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2
        cf = cf << 31;
        System.out.println("左移31位,结果为:>" + cf);

        long sf = 131072;
        sf = 1073741824;// 4294967296
        // 计算sf的
        sf = sf >> 31;
        System.out.println("右移31位,结果为:>" + sf);
    }

    /**
     * 需要将两个变量值进行调换
     */
    @Test
    public void xorTest1(){
        int i = 0,y = 1;
        System.out.println(i + "," + y);
        i = i ^ y;
        System.out.println(i + "," + y);
        y = i ^ y;
        System.out.println(i + "," + y);
        i = i ^ y;

        System.out.println(i + "," + y);

        int a = 213233,b = 1000;
        a = a ^ b;
        b^=a;
        System.out.println(a + "<<<11111111111>>>" + b);
    }

    @Test
    public void xorTest2(){
        int res = 0;
        int var[] = {2,3,6,5,8,9,7,44,1,5,6,3,2,9,8,1,7};
        for(int v = 0;v < var.length;v++)
        {
            res^=var[v];
            if (res == 0){
                System.out.println(var[v] + "<><>" + res);
            }
        }
    }

    @Test
    public void qd0Test(){
        int temp = nlz(1598348624);
        System.out.println("temp>>>" + temp);
    }

    int nlz(int x)
    {
        int n;
        if (x == 0) return(32);
        n = 1;
        if ((x >> 16) == 0) {
            n = n +16;// n = 17
            x = x <<16; // x = 65536
        }
        if ((x >> 24) == 0) {
            n = n + 8; // n = 25
            x = x << 8; // x = 65536 * 2的8次方,等于65536 * 256 = 16,777,216
        }
        if ((x >> 28) == 0) {
            n = n + 4; // n = 29
            x = x << 4; // x = 16,777,216 * 2的4次方,等于16,777,216 * 16 = 268435456
        }
        if ((x >> 30) == 0) {
            n = n + 2; // n = 31
            x = x << 2; // x = 268435456 * 2的2次方,等于268435456 * 4 = 1,073,741,824
        }
        System.out.println("X>>>" + x + "N>>>" + n);
        n = n - (x >> 31);// n = 31 - (1,073,741,824 >> 31),2的31次方
        return n;
    }
}