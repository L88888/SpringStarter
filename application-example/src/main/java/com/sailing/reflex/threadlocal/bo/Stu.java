package com.sailing.reflex.threadlocal.bo;

/**
 * @program: spring-starter
 * @description: 定义一个测试对象Stu
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 16:59:
 **/
public class Stu{
    private String name;
    private String sex;
    private String add;

    public Stu() {
    }

    public Stu(String name, String sex, String add) {
        this.name = name;
        this.sex = sex;
        this.add = add;
    }

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

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", add='" + add + '\'' +
                '}';
    }
}