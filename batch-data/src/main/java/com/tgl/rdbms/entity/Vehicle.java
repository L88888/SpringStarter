package com.tgl.rdbms.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor
@Setter
@Getter
public class Vehicle {

    private String id;

    private String plate_type;

    private String plate_no;

    private String speed;

    private String appear_time;

    private String mark_time;

    private String device_id;

    private String vehicle_image;

    private String scene_image;

    private String vehicle_color;

    private String area_code;

    private String line_no;

    private String pass_time;

    private String plate_color;

    private String plate_describe;

    private String disappear_time;

    private String vehicle_class;

    /**
     * 定义请求枚举类型put get
     */
    public enum ReqType{
        PUT(0),GET(1),INVALID(-1);

        public int code;
        ReqType(int code){
            this.code = code;
        }

        public static ReqType enumValue(int val){
            for (ReqType reqType : ReqType.values()){
                if (reqType.code == val){
                    return reqType;
                }
            }
            return INVALID;
        }
    }

}
