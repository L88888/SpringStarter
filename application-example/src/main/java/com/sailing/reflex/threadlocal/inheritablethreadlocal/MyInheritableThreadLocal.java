package com.sailing.reflex.threadlocal.inheritablethreadlocal;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 重写InheritableThreadLocal的set方法
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 15:19:
 **/
public class MyInheritableThreadLocal<T> extends InheritableThreadLocal<T> {

    /**
     * Computes the child's initial value for this inheritable thread-local
     * variable as a function of the parent's value at the time the child
     * thread is created.  This method is called from within the parent
     * thread before the child is started.
     * <p>
     * This method merely returns its input argument, and should be overridden
     * if a different behavior is desired.
     *
     * @param parentValue the parent thread's value
     * @return the child thread's initial value
     */
    protected T childValue(T parentValue) {
        if (Objects.isNull(parentValue)){
            // 无效数据重写回父类的childValue方法，不要破坏父类childValue方法
            super.childValue(parentValue);
            return parentValue;
        }

        // 对参数进行深拷贝，防止多线程下共享对象问题出现
        String tempData = JSONObject.toJSONString(parentValue);
        return (T) JSONObject.parseObject(tempData,parentValue.getClass());
    }
}
