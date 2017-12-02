package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class ArticleListEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"article_id":"22","title":"细胞疗法","publish_time":"2017-03-22 00:00:00","thumb":"http://192.168.0.161/Public/upload/article/2017/03-21/58d0f8b5b2f48.jpg","url":"http://192.168.0.161/App/GoodsPort/goodsInfo/id/155/sk/2ab99a6af22365686e97992df974d5c2"},{"article_id":"16","title":"$合作商家","publish_time":"2016-10-19 00:00:00","thumb":"","url":"http://192.168.0.161/app/Article/articleInfo/article_id/16/sk/2ab99a6af22365686e97992df974d5c2"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("ad_list")
    public ArrayList<AdListBean> adList;

    public static class AdListBean {
        /**
         * article_id : 22
         * title : 细胞疗法
         * publish_time : 2017-03-22 00:00:00
         * thumb : http://192.168.0.161/Public/upload/article/2017/03-21/58d0f8b5b2f48.jpg
         * url : http://192.168.0.161/App/GoodsPort/goodsInfo/id/155/sk/2ab99a6af22365686e97992df974d5c2
         */

        @SerializedName("article_id")
        public String articleId;
        @SerializedName("title")
        public String title;
        @SerializedName("publish_time")
        public String publishTime;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("url")
        public String url;
    }
}
