package com.tgl.designpattern.service.responsibilitychain;

/**
 * 责任链模式的抽象处理类，定义每个处理对象的抽象类
 */
public abstract class AbstractHandler {

    /**
     * 当前抽象类实例用来接收下一个处理对象，定义责任链中下一个处理对象的抽象实例
     */
    protected AbstractHandler next;

    /**
     * 下一个处理对象的抽象实例
     * @param next
     */
    public AbstractHandler setNext(AbstractHandler next){
        this.next = next;
        return next;
    }

    /**
     * 每个实现类都需要处理的抽象方法
     * @return
     */
    public abstract int handler();
}
