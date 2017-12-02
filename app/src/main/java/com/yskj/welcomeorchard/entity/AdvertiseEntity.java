package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/3/17.
 */

public class AdvertiseEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"totalAmount":301718.5,"nickName":"陈","count":10,"totalAmountCloud":0,"avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f","totalCountCloud":0,"totalCount":10,"adRedVoList":[{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":10,"sendAmount":48.5,"status":1,"id":11,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-17/58cba7e538613.jpg","receiveCount":1,"receiveAmount":6.78},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":485,"status":1,"id":9,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-17/58cba7e538613.jpg","receiveCount":1,"receiveAmount":171.69},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":97,"status":1,"id":8,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":14.35},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":48.5,"status":1,"id":7,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":13.87},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":10,"sendAmount":97000,"status":1,"id":6,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":1455},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":97000,"status":0,"id":5,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":0,"receiveAmount":0},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":106700,"status":1,"id":4,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":42253.2},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":15,"sendAmount":145.5,"status":1,"id":3,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":14.45},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":100,"sendAmount":97,"status":1,"id":2,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-16/58ca096a8c944.jpg","receiveCount":2,"receiveAmount":2.36},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":10,"sendAmount":97,"status":1,"id":1,"accountType":10,"title":"地方撒","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-16/58ca096a8c944.jpg","receiveCount":1,"receiveAmount":13.87}]}
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
         * totalAmount : 301718.5
         * nickName : 陈
         * count : 10
         * totalAmountCloud : 0.0
         * avatar : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58c8bd37af28fd00013a055f
         * totalCountCloud : 0
         * totalCount : 10
         * adRedVoList : [{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":10,"sendAmount":48.5,"status":1,"id":11,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-17/58cba7e538613.jpg","receiveCount":1,"receiveAmount":6.78},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":485,"status":1,"id":9,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-17/58cba7e538613.jpg","receiveCount":1,"receiveAmount":171.69},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":97,"status":1,"id":8,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":14.35},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":48.5,"status":1,"id":7,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":13.87},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":10,"sendAmount":97000,"status":1,"id":6,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":1455},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":97000,"status":0,"id":5,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":0,"receiveAmount":0},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":5,"sendAmount":106700,"status":1,"id":4,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":42253.2},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":15,"sendAmount":145.5,"status":1,"id":3,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png","receiveCount":1,"receiveAmount":14.45},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":100,"sendAmount":97,"status":1,"id":2,"accountType":10,"title":"","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-16/58ca096a8c944.jpg","receiveCount":2,"receiveAmount":2.36},{"cateName":"招聘广告","createTime":"2017-03-17","sendCount":10,"sendAmount":97,"status":1,"id":1,"accountType":10,"title":"地方撒","cover":"http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-16/58ca096a8c944.jpg","receiveCount":1,"receiveAmount":13.87}]
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
             * cateName : 招聘广告
             * createTime : 2017-03-17
             * sendCount : 10
             * sendAmount : 48.5
             * status : 1
             * id : 11
             * accountType : 10
             * title :
             * cover : http://192.168.0.186//Public/upload/app_adredtemplate/2017/03-17/58cba7e538613.jpg
             * receiveCount : 1
             * receiveAmount : 6.78
             */

            @SerializedName("cateName")
            public String cateName;
            @SerializedName("createTime")
            public String createTime;
            @SerializedName("sendCount")
            public int sendCount;
            @SerializedName("sendAmount")
            public double sendAmount;
            @SerializedName("status")
            public int status;
            @SerializedName("id")
            public int id;
            @SerializedName("accountType")
            public int accountType;
            @SerializedName("title")
            public String title;
            @SerializedName("cover")
            public String cover;
            @SerializedName("receiveCount")
            public int receiveCount;
            @SerializedName("receiveAmount")
            public double receiveAmount;
        }
    }
}
