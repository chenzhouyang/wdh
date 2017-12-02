package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class BankListEntity {

    /**
     * code : 0
     * message : 成功
     * data : [{"id":90,"bank":"test","cardNo":"623059113301039731","subbranch":"","name":"test","createTime":"2017-01-18 14:20:52","updatedTime":"2017-01-18 14:20:52","enable":true,"deleted":false,"cardType":0,"userId":1},{"id":91,"bank":"建设银行","cardNo":"6228210259028702172","subbranch":"","name":"test","createTime":"2017-01-18 19:26:11","updatedTime":"2017-01-18 19:26:11","enable":true,"deleted":false,"cardType":0,"userId":1}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * id : 90
         * bank : test
         * cardNo : 623059113301039731
         * subbranch :
         * name : test
         * createTime : 2017-01-18 14:20:52
         * updatedTime : 2017-01-18 14:20:52
         * enable : true
         * deleted : false
         * cardType : 0
         * userId : 1
         */

        @SerializedName("id")
        public int id;
        @SerializedName("bank")
        public String bank;
        @SerializedName("cardNo")
        public String cardNo;
        @SerializedName("subbranch")
        public String subbranch;
        @SerializedName("name")
        public String name;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("updatedTime")
        public String updatedTime;
        @SerializedName("enable")
        public boolean enable;
        @SerializedName("deleted")
        public boolean deleted;
        @SerializedName("cardType")
        public int cardType;
        @SerializedName("userId")
        public int userId;
    }
}
