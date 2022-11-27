package com.tgl.designpattern.service.responsibilitychain;

import java.util.Objects;

/**
 * 第一个处理者
 */
public class OnePassHandler extends AbstractHandler {

    /**
     * 处理第一关卡的所有业务逻辑
     * @return
     */
    private int getScore(){
        return 85;
    }

    @Override
    public int handler() {
        System.out.println("第一关卡的业务处理中-->> OnePassHandler");
        int curentScore = getScore();
        if (curentScore >= 85){
            // 表示下一个处理者是否存在，如果存在直接跳转至下一个处理者实例中
            if (!Objects.isNull(this.next)){
                System.out.println("第一关卡业务处理完成,进入下一个关卡业务处理。");
                return this.next.handler();
            }else {
                System.out.println("最后一个关卡已经处理完毕，业务链条开始退出。");
                return curentScore;
            }
        }
        System.out.println("第一关卡的业务处理异常。");
        return curentScore;
    }
}
