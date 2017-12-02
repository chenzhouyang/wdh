package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class ComPanEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"cardNo":"622202000000","cardOwner":"test","cardOpenBank":"中国银行"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * cardNo : 622202000000
         * cardOwner : test
         * cardOpenBank : 中国银行
         */

        @SerializedName("cardNo")
        public String cardNo;
        @SerializedName("cardOwner")
        public String cardOwner;
        @SerializedName("cardOpenBank")
        public String cardOpenBank;
    }
}
