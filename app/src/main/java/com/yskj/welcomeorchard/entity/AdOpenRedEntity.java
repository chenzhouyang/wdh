package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/3/17on 9:28.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AdOpenRedEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"receiveAmount":0.41,"adRedId":22}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * receiveAmount : 0.41
         * adRedId : 22
         */

        @SerializedName("receiveAmount")
        public double receiveAmount;
        @SerializedName("adRedId")
        public int adRedId;
    }
}
