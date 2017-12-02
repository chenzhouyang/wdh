package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/18.
 */

public class PushMessageEntity extends BaseEntity{

    /**
     * history_info : [{"id":"1","user_id":"32","article_id":"17","count":"1","action":"1","last_time":"1484640389","news_info":{"article_id":"17","title":"app推广详情","thumb":"/Public/upload/article/2017/01-17/587dee43ec901.png","publish_time":"1479398400","click":"1283","news_url":"/App/Article/articleInfo/aid/17"}}]
     */

    @SerializedName("history_info")
    public ArrayList<HistoryInfoBean> historyInfo;

    public static class HistoryInfoBean {
        /**
         * id : 1
         * user_id : 32
         * article_id : 17
         * count : 1
         * action : 1
         * last_time : 1484640389
         * news_info : {"article_id":"17","title":"app推广详情","thumb":"/Public/upload/article/2017/01-17/587dee43ec901.png","publish_time":"1479398400","click":"1283","news_url":"/App/Article/articleInfo/aid/17"}
         */

        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("article_id")
        public String articleId;
        @SerializedName("count")
        public String count;
        @SerializedName("action")
        public String action;
        @SerializedName("last_time")
        public String lastTime;
        @SerializedName("news_info")
        public NewsInfoBean newsInfo;

        public static class NewsInfoBean {
            /**
             * article_id : 17
             * title : app推广详情
             * thumb : /Public/upload/article/2017/01-17/587dee43ec901.png
             * publish_time : 1479398400
             * click : 1283
             * news_url : /App/Article/articleInfo/aid/17
             */

            @SerializedName("article_id")
            public String articleId;
            @SerializedName("title")
            public String title;
            @SerializedName("thumb")
            public String thumb;
            @SerializedName("publish_time")
            public String publishTime;
            @SerializedName("click")
            public String click;
            @SerializedName("news_url")
            public String newsUrl;
        }
    }
}
