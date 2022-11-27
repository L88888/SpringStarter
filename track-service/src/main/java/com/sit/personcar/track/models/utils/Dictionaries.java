package com.sit.personcar.track.models.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 记录查询服务的排序字典数据
 */
public class Dictionaries {
    private volatile static Dictionaries INSTANCE;

    private volatile static Map<String,String> SORTITEMS = new HashMap<>(50);

    static {
        SORTITEMS.put("S90-65000349","czsj-");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");

        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");

        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");

        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");

        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");

        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
        SORTITEMS.put("","");
    }

    private Dictionaries(){}

    /**
     * 采用DCL方式，处理单例在多线程模式下被多次创建的问题
     * cpu指令乱序、重排
     * @return
     */
    public static Dictionaries getInstance(){
        if (INSTANCE == null){
            // 局部枷锁，提升性能 Double Check Lock(DCL)多次检查锁机制
            synchronized (Dictionaries.class){
                if (INSTANCE == null){
                    INSTANCE = new Dictionaries();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 根据服务id获取该服务的排序字段,这块走配置文件获取查询服务的排序字段
     * @param serverId
     * @return
     */
    public String getSort(String serverId){
        if (Tools.isEmpty(serverId)){
            return "";
        }

        return SORTITEMS.get(serverId);
    }
}
