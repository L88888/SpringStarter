package com.sailing.linkstrack.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className ThirdPartyLog
 * @description 第三方API日志记录
 * @date 2020/8/10 15:46
 **/
@Data
public class ThirdPartyLog implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 接口地址 */
    private String url;

    /** 接口名称 */
//    @NotEmpty(message = "接口名称(name)参数不能为空")
    private String name;

    /** 厂商名称 */
//    @NotEmpty(message = "接口所属厂商(vendor)参数不能为空")
    private String vendor;

    /** 请求描述 */
    private String description;

    /** 请求类型 */
//    @NotEmpty(message = "请求类型(method)参数不能为空")
    private String method;

    /** 请求参数 */
    private String params;

    /** 请求参数(走AOP收集参数) */
    private Object[] paramsAop;

    /** 请求结果 */
    private String result;

    /** 请求结果(走AOP收集返回值) */
    private Object resultAop;

    /** 状态 */
//    @TableField(value = "status", fill = FieldFill.INSERT)
    private boolean status;

    /** 用户Code */
//    @NotEmpty(message = "当前登陆用户Code参数不能为空")
//    @TableField(value = "user_code")
    private String userCode;

    /** 执行时间 */
//    @TableField(value = "exe_time", fill = FieldFill.INSERT)
    private Date exeTime;

//    @TableField(exist = false)
    private String optTime;

    /** 开始时间 */
//    @TableField(exist = false)
    private String beginTime;

    /** 结束时间 */
//    @TableField(exist = false)
    private String endTime;

    private String costs;
}
