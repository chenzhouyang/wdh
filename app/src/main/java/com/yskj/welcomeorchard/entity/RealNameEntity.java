package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/2/11.
 */

public class RealNameEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"name":"wss"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * name : wss
         */

        @SerializedName("name")
        public String name;
    }
}
