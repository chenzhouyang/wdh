package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/22on 9:34.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ChooseSampleEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * adr_list : [{"ad_red_id":"41","user_id":"106","ad_state":"0","create_time":"2017-05-15 15:58:27","ad_title":"高薪招聘","thumb":"/Public/upload/app_adredtemplate/2017/05-08/5910357fa0f6e.png","class_id":"1"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("adr_list")
    public ArrayList<AdrListBean> adrList;

    public static class AdrListBean {
        /**
         * ad_red_id : 41
         * user_id : 106
         * ad_state : 0
         * create_time : 2017-05-15 15:58:27
         * ad_title : 高薪招聘
         * thumb : /Public/upload/app_adredtemplate/2017/05-08/5910357fa0f6e.png
         * class_id : 1
         */

        @SerializedName("ad_red_id")
        public String adRedId;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("ad_state")
        public String adState;
        @SerializedName("create_time")
        public String createTime;
        @SerializedName("ad_title")
        public String adTitle;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("class_id")
        public String classId;
    }
}
