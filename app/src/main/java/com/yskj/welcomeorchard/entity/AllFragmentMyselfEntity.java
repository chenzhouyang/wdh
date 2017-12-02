package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/12on 10:28.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentMyselfEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"article_id":"16","article_type":"2","title":"紧急救援基金会","publish_time":"2016-10-19 00:00:00","thumb":"http://192.168.0.161/Public/upload/article/2017/03-23/58d3a91eaeb57.png","url":"http://192.168.0.161/App/Article/articleInfo/article_id/16/sk/2ab99a6af22365686e97992df974d5c2"},{"article_id":"23","article_type":"0","title":"进入绿尚客","publish_time":"2017-04-11 00:00:00","thumb":"","url":"http://192.168.0.161/App/Article/articleInfo/article_id/23/sk/2ab99a6af22365686e97992df974d5c2"},{"article_id":"22","article_type":"1","title":"细胞疗法","publish_time":"2017-03-22 00:00:00","thumb":"http://192.168.0.161/Public/upload/article/2017/03-23/58d37943d9307.png","url":"http://192.168.0.161/App/GoodsPort/goodsInfo/id/155/sk/2ab99a6af22365686e97992df974d5c2"}]
     * cat_info : {"cat_id":"51","cat_name":"当代的我们","thumb":""}
     */
    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("cat_info")
    public CatInfoBean catInfo;
    @SerializedName("ad_list")
    public ArrayList<AdListBean> adList;

    public static class CatInfoBean {
        /**
         * cat_id : 51
         * cat_name : 当代的我们
         * thumb :
         */

        @SerializedName("cat_id")
        public String catId;
        @SerializedName("cat_name")
        public String catName;
        @SerializedName("thumb")
        public String thumb;
    }

    public static class AdListBean {
        /**
         * article_id : 16
         * article_type : 2
         * title : 紧急救援基金会
         * publish_time : 2016-10-19 00:00:00
         * thumb : http://192.168.0.161/Public/upload/article/2017/03-23/58d3a91eaeb57.png
         * url : http://192.168.0.161/App/Article/articleInfo/article_id/16/sk/2ab99a6af22365686e97992df974d5c2
         */

        @SerializedName("article_id")
        public String articleId;
        @SerializedName("article_type")
        public int articleType;
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
