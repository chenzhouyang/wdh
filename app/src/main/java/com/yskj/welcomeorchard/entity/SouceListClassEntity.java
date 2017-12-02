package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 陈宙洋
 * 2017/8/3.
 */

public class SouceListClassEntity {

    /**
     * code : 0
     * message : 成功
     * data : ["服装","小吃","平跟鞋"]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<String> data;
}
