package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者： chen
 * 时间： 2017/9/2
 * 描述：
 */

public class DistriOrderEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"orderId":118,"totalAmount":8888.5}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * orderId : 118
         * totalAmount : 8888.5
         */

        @SerializedName("orderId")
        public int orderId;
        @SerializedName("totalAmount")
        public double totalAmount;
    }
}
