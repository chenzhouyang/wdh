package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/4/26.
 */

public class ScanCodeTmpEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"tid":118}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * tid : 118
         */

        @SerializedName("orderId")
        public int orderId;
    }
}
