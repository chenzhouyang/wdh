package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者： chen
 * 时间： 2017/8/31
 * 描述：
 */

public class MillEntivity {
    /**
     * code : 0
     * message : 成功
     * data : {"millId":3,"name":"王婆大虾","url":"shxhdbd","content":"sudjfbr","createTime":"2017-08-26 17:14:52","profile":"大虾火锅"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * millId : 3
         * name : 王婆大虾
         * url : shxhdbd
         * content : sudjfbr
         * createTime : 2017-08-26 17:14:52
         * profile : 大虾火锅
         */

        @SerializedName("millId")
        public int millId;
        @SerializedName("name")
        public String name;
        @SerializedName("url")
        public String url;
        @SerializedName("content")
        public String content;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("profile")
        public String profile;
        @SerializedName("cover")
        public String cover;
    }
}
