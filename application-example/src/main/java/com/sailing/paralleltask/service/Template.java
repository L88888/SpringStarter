package com.sailing.paralleltask.service;

import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @ClassName Template
 * @Description 从数据库中获取已启用的画像模板配置数据
 * @Author Liulei
 * @Date 2022/11/25 16:09
 * @Version 1.0
 **/
@Data
@Service
public class Template {

    private String lmName;

    /**
     * 设置模板中栏目的名称
     * @param lmName 栏目名称
     * @return Template
     */
    public Template setLmName(String lmName){
        this.lmName = lmName;
        return this;
    }
}
