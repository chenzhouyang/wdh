package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者： chen
 * 时间： 2017/8/31
 * 描述：
 */

public class PostTageEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"totalPostage":12}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * totalPostage : 12.0
         */

        @SerializedName("totalPostage")
        public double totalPostage;
    }
}
