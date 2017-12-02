package com.yskj.welcomeorchard.http;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class RevenueEntiity {
    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":50,"amount":100,"profitType":1000,"createTime":"2017-01-20 12:41:01"},{"id":49,"amount":100,"profitType":1000,"createTime":"2017-01-20 12:37:17"},{"id":48,"amount":200,"profitType":1000,"createTime":"2017-01-20 12:16:55"},{"id":46,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:26:36"},{"id":44,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:25:59"},{"id":42,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:25:45"},{"id":40,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:23:44"},{"id":39,"amount":200,"profitType":1000,"createTime":"2017-01-20 10:30:13"},{"id":38,"amount":200,"profitType":1000,"createTime":"2017-01-20 10:02:23"},{"id":37,"amount":200,"profitType":1000,"createTime":"2017-01-19 14:49:34"},{"id":35,"amount":100,"profitType":1001,"createTime":"2017-01-19 13:59:17"},{"id":33,"amount":100,"profitType":1001,"createTime":"2017-01-19 13:58:03"},{"id":31,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:56:01"},{"id":29,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:55:51"},{"id":27,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:55:41"},{"id":25,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:55:30"},{"id":23,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:53:09"},{"id":21,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:50:45"},{"id":19,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:46:12"},{"id":18,"amount":200,"profitType":1000,"createTime":"2017-01-19 13:25:42"},{"id":17,"amount":300,"profitType":1000,"createTime":"2017-01-19 13:25:15"},{"id":16,"amount":300,"profitType":1000,"createTime":"2017-01-19 12:47:51"}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * cursor : 0
         * count : 10
         * list : [{"id":50,"amount":100,"profitType":1000,"createTime":"2017-01-20 12:41:01"},{"id":49,"amount":100,"profitType":1000,"createTime":"2017-01-20 12:37:17"},{"id":48,"amount":200,"profitType":1000,"createTime":"2017-01-20 12:16:55"},{"id":46,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:26:36"},{"id":44,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:25:59"},{"id":42,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:25:45"},{"id":40,"amount":1,"profitType":1001,"createTime":"2017-01-20 11:23:44"},{"id":39,"amount":200,"profitType":1000,"createTime":"2017-01-20 10:30:13"},{"id":38,"amount":200,"profitType":1000,"createTime":"2017-01-20 10:02:23"},{"id":37,"amount":200,"profitType":1000,"createTime":"2017-01-19 14:49:34"},{"id":35,"amount":100,"profitType":1001,"createTime":"2017-01-19 13:59:17"},{"id":33,"amount":100,"profitType":1001,"createTime":"2017-01-19 13:58:03"},{"id":31,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:56:01"},{"id":29,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:55:51"},{"id":27,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:55:41"},{"id":25,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:55:30"},{"id":23,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:53:09"},{"id":21,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:50:45"},{"id":19,"amount":200,"profitType":1001,"createTime":"2017-01-19 13:46:12"},{"id":18,"amount":200,"profitType":1000,"createTime":"2017-01-19 13:25:42"},{"id":17,"amount":300,"profitType":1000,"createTime":"2017-01-19 13:25:15"},{"id":16,"amount":300,"profitType":1000,"createTime":"2017-01-19 12:47:51"}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public static class ListBean {
            /**
             * id : 50
             * amount : 100
             * profitType : 1000
             * createTime : 2017-01-20 12:41:01
             */

            @SerializedName("id")
            public String id;
            @SerializedName("amount")
            public double amount;
            @SerializedName("profitType")
            public int profitType;
            @SerializedName("createTime")
            public String createTime;
        }
    }
}
