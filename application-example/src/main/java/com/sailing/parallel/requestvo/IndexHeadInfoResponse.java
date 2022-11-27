package com.sailing.parallel.requestvo;

import com.sailing.parallel.basedto.BannerDTO;
import com.sailing.parallel.basedto.LabelDTO;
import com.sailing.parallel.basedto.UserInfoDTO;

import java.util.HashMap;

/**
 * @program: spring-starter
 * @description: 统一封装的响应结果集
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 17:10:
 **/
public class IndexHeadInfoResponse {

    // 定义三个DTO来分别接收三个任务的返回值对象
    private UserInfoDTO userInfoDTO;
    private LabelDTO labelDTO;
    private BannerDTO bannerDTO;
    private HashMap data;

    public IndexHeadInfoResponse(UserInfoDTO userInfoDTO, LabelDTO labelDTO, BannerDTO bannerDTO) {
        this.userInfoDTO = userInfoDTO;
        this.labelDTO = labelDTO;
        this.bannerDTO = bannerDTO;
    }

//    public UserInfoDTO getUserInfoDTO() {
//        return userInfoDTO;
//    }
//
//    public void setUserInfoDTO(UserInfoDTO userInfoDTO) {
//        this.userInfoDTO = userInfoDTO;
//    }
//
//    public LabelDTO getLabelDTO() {
//        return labelDTO;
//    }
//
//    public void setLabelDTO(LabelDTO labelDTO) {
//        this.labelDTO = labelDTO;
//    }
//
//    public BannerDTO getBannerDTO() {
//        return bannerDTO;
//    }
//
//    public void setBannerDTO(BannerDTO bannerDTO) {
//        this.bannerDTO = bannerDTO;
//    }

    public HashMap getData(){
        HashMap resData = new HashMap(3);
        resData.put("userInfoDTO", this.userInfoDTO);
        resData.put("bannerDTO", this.bannerDTO);
        resData.put("labelDTO", this.labelDTO);
        return resData;
    }
}
