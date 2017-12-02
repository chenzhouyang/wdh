package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/2/22.
 */

public class ProfitEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"business":{"prizeName":"2017年3月-创业宝","totalAmount":0,"startDate":"2017-03-01 00:00:00","endDate":"2017-03-31 23:59:59","status":0,"period":31,"periodType":0},"director":{"prizeName":"2017年2季度-董事宝","totalAmount":400,"startDate":"2017-04-01 00:00:00","endDate":"2017-06-30 23:59:59","status":0,"period":91,"periodType":1},"tesco":{"prizeName":"","totalAmount":0,"startDate":"","endDate":"","status":0,"period":0,"periodType":0}}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * business : {"prizeName":"2017年3月-创业宝","totalAmount":0,"startDate":"2017-03-01 00:00:00","endDate":"2017-03-31 23:59:59","status":0,"period":31,"periodType":0}
         * director : {"prizeName":"2017年2季度-董事宝","totalAmount":400,"startDate":"2017-04-01 00:00:00","endDate":"2017-06-30 23:59:59","status":0,"period":91,"periodType":1}
         * tesco : {"prizeName":"","totalAmount":0,"startDate":"","endDate":"","status":0,"period":0,"periodType":0}
         */
        @SerializedName("business")
        public BusinessBean business;
        @SerializedName("director")
        public DirectorBean director;
        @SerializedName("tesco")
        public TescoBean tesco;
    }
    public static class BusinessBean {
        /**
         * prizeName : 2017年3月-创业宝
         * totalAmount : 0
         * startDate : 2017-03-01 00:00:00
         * endDate : 2017-03-31 23:59:59
         * status : 0
         * period : 31
         * periodType : 0
         */

        @SerializedName("prizeName")
        public String prizeName;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("status")
        public int status;
        @SerializedName("period")
        public double period;
        @SerializedName("periodType")
        public int periodType;
    }

    public static class DirectorBean {
        /**
         * prizeName : 2017年2季度-董事宝
         * totalAmount : 400
         * startDate : 2017-04-01 00:00:00
         * endDate : 2017-06-30 23:59:59
         * status : 0
         * period : 91
         * periodType : 1
         */

        @SerializedName("prizeName")
        public String prizeName;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("status")
        public int status;
        @SerializedName("period")
        public double period;
        @SerializedName("periodType")
        public int periodType;
    }

    public static class TescoBean {
        /**
         * prizeName :
         * totalAmount : 0
         * startDate :
         * endDate :
         * status : 0
         * period : 0
         * periodType : 0
         */

        @SerializedName("prizeName")
        public String prizeName;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("status")
        public int status;
        @SerializedName("period")
        public double period;
        @SerializedName("periodType")
        public int periodType;
    }
}
