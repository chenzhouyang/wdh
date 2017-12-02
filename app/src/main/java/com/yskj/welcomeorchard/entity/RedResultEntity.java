package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YSKJ-02 on 2017/1/22.
 */

public class RedResultEntity implements Serializable {


    /**
     * code : 0
     * message : 成功
     * data : {"redId":1,"openAmount":97.24,"redType":0,"type":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * redId : 1
         * openAmount : 97.24
         * redType : 0
         * type : 0
         */

        @SerializedName("redId")
        public int redId;

        @SerializedName("openAmount")
        public double openAmount;
        @SerializedName("loseAmount")
        public double loseAmount;
        @SerializedName("redType")
        public int redType;
        @SerializedName("type")
        public int type;
    }
}
