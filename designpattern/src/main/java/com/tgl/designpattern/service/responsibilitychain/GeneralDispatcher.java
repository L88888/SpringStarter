package com.tgl.designpattern.service.responsibilitychain;

/**
 * 初始化链条上的处理者集合对象
 */
public class GeneralDispatcher {

    private void init(){
        // 第一个处理者实现
        OnePassHandler onePassHandler = new OnePassHandler();
        // 第二个处理者实现
        ToPassHandler toPassHandler = new ToPassHandler();
        // 第三个处理者实现
        ThreePassHandler threePassHandler = new ThreePassHandler();

        // 初始化链条实例与顺序
        // 按照处理顺序动态绑定具体的处理对象
        onePassHandler.setNext(toPassHandler).setNext(threePassHandler);

        onePassHandler.handler();
    }

    public static void main(String[] arge){
        GeneralDispatcher generalDispatcher = new GeneralDispatcher();
        generalDispatcher.init();
    }
}
