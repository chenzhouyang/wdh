package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/22.
 */

public class RedListEntity {


    /**
     * code : 0
     * message : 成功
     * data : {"redLogs":[{"id":1,"sendId":1,"receiveId":10,"formId":10,"sendRedNo":"","amount":100.25,"status":0,"type":0,"redType":0,"createDate":"2017-02-13 14:06:32","modifyDate":""},{"id":3,"sendId":0,"receiveId":10,"formId":0,"sendRedNo":"20170213153452142701","amount":11.11,"status":0,"type":0,"redType":2,"createDate":"2017-02-13 15:34:53","modifyDate":""}],"count":2,"cursor":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * redLogs : [{"id":1,"sendId":1,"receiveId":10,"formId":10,"sendRedNo":"","amount":100.25,"status":0,"type":0,"redType":0,"createDate":"2017-02-13 14:06:32","modifyDate":""},{"id":3,"sendId":0,"receiveId":10,"formId":0,"sendRedNo":"20170213153452142701","amount":11.11,"status":0,"type":0,"redType":2,"createDate":"2017-02-13 15:34:53","modifyDate":""}]
         * count : 2
         * cursor : 0
         */

        @SerializedName("count")
        public int count;
        @SerializedName("cursor")
        public int cursor;
        @SerializedName("redLogs")
        public ArrayList<RedLogsBean> redLogs;

        public static class RedLogsBean {

            /**
             * id : 1
             * amount : 0
             * status : 0
             * redType : 0
             * createDate : 2017-02-13 14:06:32
             * modifyDate :
             */

            @SerializedName("id")
            public String id;
            @SerializedName("amount")
            public double amount;
            @SerializedName("status")
            public int status;
            @SerializedName("redType")
            public int redType;
            @SerializedName("createDate")
            public String createDate;
            @SerializedName("modifyDate")
            public String modifyDate;
            @SerializedName("redId")
            public String redId;
        }
    }
}
