package com.test.examples.dispeldoubts;

import org.junit.Test;

/**
 * @program: spring-starter
 * @description: 构造函数的初始化声明异常处理
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-01-20 17:51:
 **/
public class DispelDoubts3 {

//    private static Class engineClass = classForName().getClass();
//
//    private Object classForName(){
//        return Class.forName("TestTgl").newInstance();
//    }

    private TestTgl testTgl = instance();

    private TestTgl instance(){
        try {
            System.out.println("初始化自定义对象TestTgl" + TestTgl.class.getName());
            return (TestTgl)Class.forName(TestTgl.class.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new TestTgl();
    }

    @Test
    public void main(){
        testTgl.setAddress("sssssssss");
        System.out.println(testTgl.getAddress());
    }
}

/**
 * 外部类
 */
 class TestTgl{
    String name;
    String sex;
    String address;
    String org;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
}