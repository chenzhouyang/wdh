package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

/**
 * Created by YSKJ-02 on 2017/1/18.
 */

public class MainDialogEntiity extends BaseEntity {

    /**
     * news_info : {"article_id":"17","title":"app推广详情","thumb":"/Public/upload/article/2017/01-17/587dee43ec901.png","publish_time":"1479398400"}
     * news_url : App/Article/articleInfo/aid/17
     */

    @SerializedName("news_info")
    public NewsInfoBean newsInfo;
    @SerializedName("news_url")
    public String newsUrl;

    public static class NewsInfoBean {
        /**
         * article_id : 17
         * title : app推广详情
         * thumb : /Public/upload/article/2017/01-17/587dee43ec901.png
         * publish_time : 1479398400
         */

        @SerializedName("article_id")
        public String articleId;
        @SerializedName("title")
        public String title;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("publish_time")
        public String publishTime;
    }
}
