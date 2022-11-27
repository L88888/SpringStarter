package com.tgl.designpattern.chain;

/**
 * @program: spring-starter
 * @description: 实现Filter接口处理请求与响应数据报文
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-05 21:28:
 **/
public class HelloFilter implements Filter {
    @Override
    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, FilterChain filterChain) {
        // 先处理请求报文数据
        String val = requestMessage.getReqBodyVal();
        val += "sssssssssssssss";
        requestMessage.setReqBodyVal(val);

        // 继续让其他filter处理请求中的报文数据
        filterChain.chain(requestMessage, responseMessage, filterChain);

        // 过滤连上的实现对象都处理完之后开始处理响应报文数据
        String val1 = responseMessage.getResBodyVal();
        val1 += "responseData 认识设计模式之责任链模式.";
        responseMessage.setResBodyVal(val1);
    }
}
