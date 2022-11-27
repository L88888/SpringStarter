package com.sit.client.tests.builder;

import com.tgl.designpattern.service.builder.IUserBuilder;
import com.tgl.designpattern.service.builder.UserBuilderImpl;
import org.junit.Test;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-10-11 18:43:
 **/
public class BuilderTest {

    @Test
    public void testArrays(){
        IUserBuilder iUserBuilder = new UserBuilderImpl();
        iUserBuilder.getUser();
    }
}
