package com.tgl.binlogevent.concurrent;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Comsys-LIULEI
 * @version V1.0
 * @Title: EnableSpringBean
 * @Package sailing-server-platform-backstage
 * @Description: EnableSpringBean
 * Copyright: Copyright (c) 2011
 * Company:上海熙菱信息技术有限公司
 * @date 2018/1/25 17:01:48下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SpringBean.class)
@Documented
public @interface EnableSpringBean {
}
