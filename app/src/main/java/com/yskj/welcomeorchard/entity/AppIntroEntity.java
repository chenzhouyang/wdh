package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/2/13.
 */

public class AppIntroEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"ad_id":"50","ad_name":"自定义广告名称","ad_code":"http://121.40.28.79/Public/upload/ad/2016/11-11/58253306263ae.png","ad_link":"/App/Goods/goodsInfo/id/50.html"},{"ad_id":"49","ad_name":"自定义广告名称","ad_code":"http://121.40.28.79/Public/upload/ad/2016/11-11/58253340ca2b3.png","ad_link":"/App/Goods/goodsInfo/id/49.html"},{"ad_id":"48","ad_name":"自定义广告名称","ad_code":"http://121.40.28.79/Public/upload/ad/2016/11-11/58253360a8c98.jpg","ad_link":"/App/Goods/goodsInfo/id/48.html"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("ad_list")
    public ArrayList<AdListBean> adList;

    public static class AdListBean {
        /**
         * ad_id : 50
         * ad_name : 自定义广告名称
         * ad_code : http://121.40.28.79/Public/upload/ad/2016/11-11/58253306263ae.png
         * ad_link : /App/Goods/goodsInfo/id/50.html
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
