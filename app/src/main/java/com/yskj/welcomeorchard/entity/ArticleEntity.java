package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class ArticleEntity extends BaseEntity {
    @SerializedName("article_id")
    public String article_id;
    @SerializedName("publish_time")
    public String publish_time;
    @SerializedName("thumb")
    public String thumb;
    @SerializedName("title")
    public String title;
    @SerializedName("url")
    public String url;

}
