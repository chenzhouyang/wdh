package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class EarnEntity extends BaseEntity {
    @SerializedName("ret_data")
    public retData retData;

    public static class retData{
        @SerializedName("total_ads")
        public int total_ads;
        @SerializedName("current_page")
        public int current_page;
        @SerializedName("ads")
        public ArrayList<ad> arrayList;
    }

    public static class ad{
        @SerializedName("content")
        public String content;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("title")
        public String title;
        @SerializedName("orderNo")
        public int orderNo;
        @SerializedName("clickCount")
        public int clickCount;
        @SerializedName("adId")
        public String adId;
        @SerializedName("link")
        public String link;
        @SerializedName("path")
        public String path;
        @SerializedName("type")
        public int type;
    }
}
