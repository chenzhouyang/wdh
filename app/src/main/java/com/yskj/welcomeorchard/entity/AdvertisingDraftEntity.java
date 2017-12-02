package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/3/16on 10:30.
 * 描述：草稿实体类
 * 作者：姜贺YSKJ-JH
 */

public class AdvertisingDraftEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * adr_list : [{"ad_red_id":"44","template_id":"1","user_id":"12","ad_state":"1","title":"","key_word":null,"thumb":"/Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png"},{"ad_red_id":"45","template_id":"1","user_id":"12","ad_state":"1","title":"云盛科","key_word":null,"thumb":"/Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("adr_list")
    public ArrayList<AdrListBean> adrList;

    public static  class AdrListBean {
        /**
         * ad_red_id : 44
         * template_id : 1
         * user_id : 12
         * ad_state : 1
         * title :
         * key_word : null
         * thumb : /Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png
         */

        @SerializedName("ad_red_id")
        public String adRedId;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("ad_state")
        public String adState;
        @SerializedName("ad_title")
        public String title;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("create_time")
        public String create_time;
        @SerializedName("class_id")
        public String class_id;
    }
}
