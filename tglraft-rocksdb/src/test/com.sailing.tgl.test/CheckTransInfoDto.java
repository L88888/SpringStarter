package com.sailing.tgl.test;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-10 14:45:
 **/
@Data
public class CheckTransInfoDto {

    /**
     * 交易订单号
     */
    @ExcelProperty(value = "交易订单号")
    private String transOrderNo;

    /**
     * 交易商户编号
     */
    @ExcelProperty(value = "交易商户编号")
    private String transMercId;

    /**
     * 结算商户编号
     */
    @ExcelProperty(value = "结算商户编号")
    private String smtMercId;

    /**
     * 交易银行卡号
     */
    @ExcelProperty(value = "银行卡号")
    private String crdNo;

    // get set 方法


    @Override
    public String toString() {
        return "CheckTransInfoDto{" +
                "transOrderNo='" + transOrderNo + '\'' +
                '}';
    }
}
