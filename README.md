# 一.Spring Boot Starter简介
Starter是Spring Boot中的一个非常重要的概念，Starter相当于模块，它能将模块所需的依赖整合起来并对模块内的Bean根据环境（ 条件）进行自动配置。使用者只需要依赖相应功能的Starter，无需做过多的配置和依赖，Spring Boot就能自动扫描并加载相应的模块。

例如在Maven的依赖中加入spring-boot-starter-web就能使项目支持Spring MVC，并且Spring Boot还为我们做了很多默认配置，无需再依赖spring-web、spring-webmvc等相关包及做相关配置就能够立即使用起来。

# 二.Starter的开发步骤
编写Starter非常简单，与编写一个普通的Spring Boot应用没有太大区别，总结如下：

    1.新建Maven项目，在项目的POM文件中定义使用的依赖；
    2.新建配置类，写好配置项和默认的配置值，指明配置项前缀；
    3.新建自动装配类，使用@Configuration和@Bean来进行自动装配；
    4.新建spring.factories文件，指定Starter的自动装配类；

# 三.Starter的开发示例

**源代码参考提供：**
[开发一个Spring Boot Starter](https://github.com/L88888/springboot-starter-example)


# CompletionService 并行调用实现方案
使用CompletionService 开发一套并行调用的框架组件
1、统一调度类 DispatchIndex
2、统一结果封装类
3、统一结果获取方法
