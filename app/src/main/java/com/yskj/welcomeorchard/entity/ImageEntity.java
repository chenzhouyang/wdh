package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/11 0011.
 */
public class ImageEntity  {
    @SerializedName("article_id")
    public String article_id;
    @SerializedName("title")
    public String title;
    @SerializedName("thumb")
    public String thumb;
    @SerializedName("url")
    public String url;
}
