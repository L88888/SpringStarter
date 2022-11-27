package com.tgl.designpattern.finalize;

import com.tgl.designpattern.concurrent.TglRaftThreadHelper;

/**
 * @program: spring-starter
 * @description: GC回收前自救；该方法finalize()只能使用一次
 * @author: LIULEI-TGL
 * @create: 2021-07-25 12:13:
 **/
public class FinalizeEscapeGC {

    private static FinalizeEscapeGC finalizeEscapeGC = null;

    /**
     * 该方法在jvm销毁时只能自救一次，不能二次使用
     * @throws Throwable
     */
    @Override
    protected void finalize()throws Throwable{
        super.finalize();
        System.out.println("gc 后对象自己实例化一次。");
        finalizeEscapeGC = this;
    }

    private void isAlive(){
        System.out.println("我还活着");
    }

    public static void main(String[] agre){
        finalizeEscapeGC = new FinalizeEscapeGC();
        finalizeEscapeGC = null;
        System.gc();

        TglRaftThreadHelper.sleep(500);
        if (finalizeEscapeGC != null){
            finalizeEscapeGC.isAlive();
        }else {
            System.out.println("我已经被jvm 虚拟机销毁了.");
        }

//        finalizeEscapeGC = new FinalizeEscapeGC();
        finalizeEscapeGC = null;
        System.gc();

        TglRaftThreadHelper.sleep(500);
        if (finalizeEscapeGC != null){
            finalizeEscapeGC.isAlive();
        }else {
            System.out.println("我已经被jvm 虚拟机销毁了.");
        }
    }
}
