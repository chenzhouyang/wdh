package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/14on 18:18.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class WDHFragmentNewPeopleEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"ad_id":"69","ad_name":"新人专属享受","ad_type":"0","ad_code":"http://192.168.0.161/Public/upload/ad/2017/04-14/58f02ba138406.png","ad_link":""},{"ad_id":"68","ad_name":"新人专属享受","ad_type":"0","ad_code":"http://192.168.0.161/Public/upload/ad/2017/04-14/58f02b91681a6.png","ad_link":""}]
     * position_info : {"position_name":"新人专属特权","thumb":""}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("position_info")
    public PositionInfoBean positionInfo;
    @SerializedName("ad_list")
    public ArrayList<AdListBean> adList;

    public static class PositionInfoBean {
        /**
         * position_name : 新人专属特权
         * thumb :
         */

        @SerializedName("position_name")
        public String positionName;
        @SerializedName("thumb")
        public String thumb;
    }

    public static class AdListBean {
        /**
         * ad_id : 69
         * ad_name : 新人专属享受
         * ad_type : 0
         * ad_code : http://192.168.0.161/Public/upload/ad/2017/04-14/58f02ba138406.png
         * ad_link :
         */

        @SerializedName("ad_id")
        public String adId;
        @SerializedName("ad_name")
        public String adName;
        @SerializedName("ad_type")
        public int adType;
        @SerializedName("ad_code")
        public String adCode;
        @SerializedName("ad_link")
        public String adLink;
    }
}
