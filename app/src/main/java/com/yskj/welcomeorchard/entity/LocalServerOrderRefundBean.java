package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jianghe on 2016/11/17 0017.
 */
public class LocalServerOrderRefundBean {

    /**
     * code : 0
     * message : 成功
     * data : {"quickRefund":true}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * quickRefund : true
         */

        @SerializedName("quickRefund")
        public boolean quickRefund;
    }
}
