package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/6/5.
 */

public class MoneyInfoEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"fundAccount":400090.9,"cloudAccount":110,"totalAmount":0,"totalMAccount":0,"gpaccount":300,"maccount":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * fundAccount : 400090.9
         * cloudAccount : 110
         * totalAmount : 0
         * totalMAccount : 0
         * gpaccount : 300
         * maccount : 0
         */

        @SerializedName("fundAccount")
        public double fundAccount;
        @SerializedName("cloudAccount")
        public double cloudAccount;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("totalMAccount")
        public double totalMAccount;
        @SerializedName("gpaccount")
        public double gpaccount;
        @SerializedName("maccount")
        public double maccount;
    }
}
