package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/3/20.
 */

public class CarcuseEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"ad_id":"62","ad_name":"交友","ad_code":"http://192.168.0.161/Public/upload/ad/2017/03-20/58cf42870fc7c.png","ad_link":"javascript:void(0);"},{"ad_id":"9","ad_name":"荣耀4C增强版8GB变16GB","ad_code":"http://192.168.0.161/Public/upload/ad/2016/03-01/56d539e55544a.jpg","ad_link":"javascript:void(0);"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("ad_list")
    public ArrayList<AdListBean> adList;

    public static class AdListBean {
        /**
         * ad_id : 62
         * ad_name : 交友
         * ad_code : http://192.168.0.161/Public/upload/ad/2017/03-20/58cf42870fc7c.png
         * ad_link : javascript:void(0);
         */

        @SerializedName("ad_id")
        public String adId;
        @SerializedName("ad_name")
        public String adName;
        @SerializedName("ad_code")
        public String adCode;
        @SerializedName("ad_link")
        public String adLink;
    }
}
