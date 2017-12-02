package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jianghe on 2016/11/11 0011.
 * 团购详情中使用规则的实体类
 */
public class LocalServerPurchaseNoteBean {
    @SerializedName("key")
    public int key;
    @SerializedName("type")
    public int type;
    @SerializedName("val")
    public String val;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;
}
