package com.tgl.designpattern.chain;

/**
 * @program: spring-starter
 * @description: 过滤连接口，定义需要过滤的请求与响应报文对象。
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-05 21:12:
 **/
public interface Filter {

    /**
     * 定义需要过滤的请求与响应报文对象。
     * @param requestMessage 请求对象
     * @param responseMessage 响应对象
     * @param filterChain 过滤连对象
     */
    public void doFilter(RequestMessage requestMessage,
                         ResponseMessage responseMessage,
                         FilterChain filterChain);
}
