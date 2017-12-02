package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/2/22.
 */

public class RedSmallEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"redLogs":[{"code":"1-0","realRedId":1,"amount":72.17,"status":1,"type":0,"isDivided":1,"createDate":"2017-02-21 14:06:32"},{"code":"1-1","realRedId":1,"amount":28.08,"status":0,"type":0,"isDivided":1,"createDate":"2017-02-21 14:06:32"}],"count":2}
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
         * redLogs : [{"code":"1-0","realRedId":1,"amount":72.17,"status":1,"type":0,"isDivided":1,"createDate":"2017-02-21 14:06:32"},{"code":"1-1","realRedId":1,"amount":28.08,"status":0,"type":0,"isDivided":1,"createDate":"2017-02-21 14:06:32"}]
         * count : 2
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("redLogs")
        public ArrayList<RedLogsBean> redLogs;

        public static class RedLogsBean {
            /**
             * code : 1-0
             * realRedId : 1
             * amount : 72.17
             * status : 1
             * type : 0
             * isDivided : 1
             * createDate : 2017-02-21 14:06:32
             */

            @SerializedName("code")
            public String code;
            @SerializedName("realRedId")
            public int realRedId;
            @SerializedName("amount")
            public double amount;
            @SerializedName("status")
            public int status;
            @SerializedName("type")
            public int type;
            @SerializedName("isDivided")
            public int isDivided;
            @SerializedName("createDate")
            public String createDate;
        }
    }
}
