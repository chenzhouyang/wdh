package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/3/17.
 */

public class AdvertisingHsaOpenEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"totalAmount":28.81,"nickName":"陈","count":10,"totalAmountCloud":0,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","totalCountCloud":0,"totalCount":3,"adRedVoList":[{"createTime":"2017-03-17","amount":14.45,"sendUserName":"陈","receiveTime":"2017-03-17","adRedId":3,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","accountType":10},{"createTime":"2017-03-17","amount":13.87,"sendUserName":"陈","receiveTime":"2017-03-17","adRedId":1,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","accountType":10},{"createTime":"2017-03-17","amount":0.49,"sendUserName":"陈","receiveTime":"2017-03-17","adRedId":2,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","accountType":10}]}
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
         * totalAmount : 28.81
         * nickName : 陈
         * count : 10
         * totalAmountCloud : 0.0
         * avatar : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f
         * totalCountCloud : 0
         * totalCount : 3
         * adRedVoList : [{"createTime":"2017-03-17","amount":14.45,"sendUserName":"陈","receiveTime":"2017-03-17","adRedId":3,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","accountType":10},{"createTime":"2017-03-17","amount":13.87,"sendUserName":"陈","receiveTime":"2017-03-17","adRedId":1,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","accountType":10},{"createTime":"2017-03-17","amount":0.49,"sendUserName":"陈","receiveTime":"2017-03-17","adRedId":2,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","accountType":10}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("count")
        public int count;
        @SerializedName("totalAmountCloud")
        public double totalAmountCloud;
        @SerializedName("avatar")
        public String avatar;
        @SerializedName("totalCountCloud")
        public int totalCountCloud;
        @SerializedName("totalCount")
        public int totalCount;
        @SerializedName("adRedVoList")
        public ArrayList<AdRedVoListBean> adRedVoList;

        public static class AdRedVoListBean {
            /**
             * createTime : 2017-03-17
             * amount : 14.45
             * sendUserName : 陈
             * receiveTime : 2017-03-17
             * adRedId : 3
             * avatar : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f
             * accountType : 10
             */

            @SerializedName("createTime")
            public String createTime;
            @SerializedName("amount")
            public double amount;
            @SerializedName("sendUserName")
            public String sendUserName;
            @SerializedName("receiveTime")
            public String receiveTime;
            @SerializedName("adRedId")
            public int adRedId;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("accountType")
            public int accountType;
            @SerializedName("title")
            public String title;
            @SerializedName("cover")
            public String cover;
            @SerializedName("template")
            public String template;
        }
    }
}
