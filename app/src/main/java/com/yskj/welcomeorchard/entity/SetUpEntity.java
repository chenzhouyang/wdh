package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/4/26.
 */

public class SetUpEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"userVIPId":45}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * userVIPId : 45
         */

        @SerializedName("orderid")
        public String userVIPId;
    }
}
