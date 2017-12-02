package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：  陈宙洋
 * 描述：
 * 日期： 2017/11/7.
 */

public class UpdateEntity {
    /**
     * isUpdate : 0
     * url : www.baidu.com
     */

    @SerializedName("isUpdate")
    public int isUpdate;
    @SerializedName("url")
    public String url;
    @SerializedName("desc")
    public String desc;
    @SerializedName("vcode")
    public int vcode;
}
