package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class MainNoticeEntity extends BaseEntity {

    @SerializedName("ret_data")
    public RetData retData;

    public static class RetData{
        @SerializedName("cursor")
        public int cursor;
        @SerializedName("total_number")
        public int total_number;
        @SerializedName("articles")
        public ArrayList<Article> arrayList;
    }

    public static class Article{
        @SerializedName("articleId")
        public int articleId;
        @SerializedName("author")
        public String author;
        @SerializedName("cover")
        public String cover;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("desc")
        public String desc;
        @SerializedName("keyWords")
        public String keyWords;
        @SerializedName("title")
        public String title;
    }
}
