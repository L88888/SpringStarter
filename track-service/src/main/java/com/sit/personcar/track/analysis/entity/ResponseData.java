package com.sit.personcar.track.analysis.entity;

/**
 * 响应对象
 * // {
 * 	//    "app_header":{
 * 	//        "status":"000",
 * 	//        "statusMsg":"操作成功"
 * 	//    },
 * 	//    "app_body":{
 * 	//        "count":"20",
 * 	//        "totalCount":"94",
 * 	//        "result":Array[21]
 * 	//    }
 * 	//}
 */
public class ResponseData {
    private App_header app_header;
    private App_body app_body;
    public void setApp_header(App_header app_header) {
        this.app_header = app_header;
    }
    public App_header getApp_header() {
        return app_header;
    }

    public void setApp_body(App_body app_body) {
        this.app_body = app_body;
    }
    public App_body getApp_body() {
        return app_body;
    }
}
