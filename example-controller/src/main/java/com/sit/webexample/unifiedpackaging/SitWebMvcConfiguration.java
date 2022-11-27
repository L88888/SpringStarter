package com.sit.webexample.unifiedpackaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 解决controller接口直接返回String对象时类型转换异常的问题
 */
@Configuration
public class SitWebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 交换MappingJackson2HttpMessageConverter与第一位元素
     * 让返回值类型为String的接口能正常返回包装结果,String 返回值最终统一使用StringHttpMessageConverter对象进行信息转换。
     * org.springframework.http.converter.StringHttpMessageConverter
     * 而其他数据类型的转换对象是MappingJackson2HttpMessageConverter
     * org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
     *
     * @param converters initially an empty list of converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                        (MappingJackson2HttpMessageConverter) converters.get(i);
                converters.set(i, converters.get(0));
                converters.set(0, mappingJackson2HttpMessageConverter);
                break;
            }
        }
    }
}
