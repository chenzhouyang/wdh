package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/4/18.
 */

public class SupentEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"ad_id":"50","ad_name":"漂浮块广告","ad_type":"1","ad_code":"http://192.168.0.161/Public/upload/ad/2017/04-18/58f5c2fb13838.png","ad_link":"http://192.168.0.161/App/GoodsPort/goodsInfo/id/50/sk/2ab99a6af22365686e97992df974d5c2"}]
     * position_info : {"position_name":"漂浮块广告","thumb":""}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("position_info")
    public PositionInfoBean positionInfo;
    @SerializedName("ad_list")
    public List<AdListBean> adList;

    public static class PositionInfoBean {
        /**
         * position_name : 漂浮块广告
         * thumb :
         */

        @SerializedName("position_name")
        public String positionName;
        @SerializedName("thumb")
        public String thumb;
    }

    public static class AdListBean {
        /**
         * ad_id : 50
         * ad_name : 漂浮块广告
         * ad_type : 1
         * ad_code : http://192.168.0.161/Public/upload/ad/2017/04-18/58f5c2fb13838.png
         * ad_link : http://192.168.0.161/App/GoodsPort/goodsInfo/id/50/sk/2ab99a6af22365686e97992df974d5c2
         */

        @SerializedName("ad_id")
        public String adId;
        @SerializedName("ad_name")
        public String adName;
        @SerializedName("ad_type")
        public String adType;
        @SerializedName("ad_code")
        public String adCode;
        @SerializedName("ad_link")
        public String adLink;
    }
}
