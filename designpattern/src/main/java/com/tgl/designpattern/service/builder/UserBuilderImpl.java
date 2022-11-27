package com.tgl.designpattern.service.builder;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-10-09 19:20:
 **/
@Slf4j
public class UserBuilderImpl implements IUserBuilder {
    @Override
    public int buildPayCnt() {
        return 0;
    }

    @Override
    public int buildPayAnt() {
        return 0;
    }

    @Override
    public List<String> buildProductType() {
        return null;
    }

    /**
     * 分组聚合
     * @param students
     * @return
     */
    Map<String, Long> groupList(List<Student> students) {
        Map<String, Long> map =
                students.stream().collect(
                        Collectors.groupingBy(item -> item.getHphm() + "_" + item.getHpys() , Collectors.counting()));

        log.info("分组聚合后的结果为:>{}", map);
        return map;
    }

    @Override
    public List<String> buildAntInterval() {
        List<Student> students = new ArrayList<>();
        Student student = new Student();
        student.setHphm("陕A12345");
        student.setHpys("01");
        student.setPasstime("2021-10-11 18");
        students.add(student);

        Student student1 = new Student();
        student1.setHphm("陕A12345");
        student1.setHpys("01");
        student1.setPasstime("2021-10-11 18");
        students.add(student1);

        Student student2 = new Student();
        student2.setHphm("陕A12346");
        student2.setHpys("01");
        student2.setPasstime("2021-10-11 18");
        students.add(student2);

        Student student3 = new Student();
        student3.setHphm("陕A12347");
        student3.setHpys("01");
        student3.setPasstime("2021-10-11 18");
        students.add(student3);

        groupList(students);
        return null;
    }

    @Override
    public User getUser() {
        this.buildAntInterval();
        return null;
    }
}


class Student{
    private String hphm;
    private String hpys;
    private String passtime;

    public String getHphm(){
        return this.hphm;
    }

    public String getHpys(){
        return this.hpys;
    }

    public String getPasstime(){
        return this.passtime;
    }

    public void setPasstime(String passtime){
        this.passtime = passtime;
    }

    public void setHpys(String hpys){
        this.hpys = hpys;
    }

    public void setHphm(String hphm){
        this.hphm = hphm;
    }
}
