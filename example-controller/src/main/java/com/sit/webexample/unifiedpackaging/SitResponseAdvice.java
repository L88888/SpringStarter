package com.sit.webexample.unifiedpackaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 自定义统一封装响应对象结构，如果返回值对象已经被业务层包装了，就不进行包装
 */
@RestControllerAdvice(basePackages = "com.sit.webexample")
@Slf4j
public class SitResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 返回true则需要对结果进行校验与包装，否则不进行包装
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * response响应数据前对数据报文进行特殊加工处理，这里主要是对响应报文进行统一数据结构封装
     * 对reponse做具体的处理
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        log.info("response body is value=>{}", o);

        // 如果response body对象本身就是SitResult类型的,那么直接返回该响应报文
        if (o instanceof SitResult){
            return o;
        }

        // 否则将响应报文统一封装成SitResult对象在返回
        return SitResult.success(o);
    }
}
