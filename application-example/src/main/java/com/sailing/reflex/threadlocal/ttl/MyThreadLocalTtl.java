package com.sailing.reflex.threadlocal.ttl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Objects;

/**
 * @program: spring-starter
 * @description:
 * 1、每次提交任务到线程池都会拷贝父线程的本地变量；
 * 2、默认值传递可以自定义TTL实现深拷贝
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 18:22:
 **/
public class MyThreadLocalTtl<T> extends TransmittableThreadLocal<T> {

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
    @Override
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
