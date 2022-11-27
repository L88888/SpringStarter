package com.sailing.reflex.threadlocal.threadlocaloom;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 防止多线程间对象共享问题出现，ThreadLocal只有值传递没有引用传递
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 12:40:
 **/
public class MyThreadLocal<T> extends ThreadLocal<T>{

    /**
     * Sets the current thread's copy of this thread-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of thread-locals.
     *
     * @param value the value to be stored in the current thread's copy of
     *        this thread-local.
     */
    public void set(T value) {
        if (Objects.isNull(value)){
            // 无效数据重写回父类的set方法，不要破坏父类set方法
            super.set(value);
            return;
        }
        // 对参数进行深拷贝，防止多线程下共享对象问题出现
        String tempData = JSONObject.toJSONString(value);
        super.set((T) JSONObject.parseObject(tempData,value.getClass()));
    }
}