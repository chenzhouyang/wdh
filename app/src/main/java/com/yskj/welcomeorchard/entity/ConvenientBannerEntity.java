package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class ConvenientBannerEntity {
    @SerializedName("ad_id")
    public String ad_id;
    @SerializedName("ad_name")
    public String ad_name;
    @SerializedName("ad_code") //轮播图url
    public String ad_code;
    @SerializedName("ad_link")
    public String ad_link;
}
