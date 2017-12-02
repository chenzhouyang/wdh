package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/3/17on 9:43.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */
public class AdOpenRedDetailEntity {


    /**
     * code : 0
     * message : 成功
     * data : {"avatar":"https://api.wdh158.com/api/v1/mgs/file/download?fid=58fdd56b0eb6370001690ee6","nickName":"陈","categoryName":"招聘广告","mapId":2149046056,"sendCount":1,"sendAmount":0.5,"receiveCount":1,"receiveAmount":0.5,"latitude":0,"longitude":0,"region":500,"period":7,"status":2,"fee":0,"accountType":10,"template":"https://www.wdh158.com/App/Ad/ad_redEdit/act/info/rid/70","adPageId":70,"createTime":"2017-06-07 09:58:20","updateTime":"2017-06-07 09:58:54","receivedRedList":[{"avatar":"https://api.wdh158.com/api/v1/mgs/file/download?fid=58fdd56b0eb6370001690ee6","nickName":"陈","amount":0.5,"receiveTime":"2017-06-07 09:58:54","bestLuck":true}],"thumbUpCount":0,"currUserAmount":0.5,"showSendAmount":true,"thumbUp":false}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * avatar : https://api.wdh158.com/api/v1/mgs/file/download?fid=58fdd56b0eb6370001690ee6
         * nickName : 陈
         * categoryName : 招聘广告
         * mapId : 2149046056
         * sendCount : 1
         * sendAmount : 0.5
         * receiveCount : 1
         * receiveAmount : 0.5
         * latitude : 0
         * longitude : 0
         * region : 500
         * period : 7
         * status : 2
         * fee : 0
         * accountType : 10
         * template : https://www.wdh158.com/App/Ad/ad_redEdit/act/info/rid/70
         * adPageId : 70
         * createTime : 2017-06-07 09:58:20
         * updateTime : 2017-06-07 09:58:54
         * receivedRedList : [{"avatar":"https://api.wdh158.com/api/v1/mgs/file/download?fid=58fdd56b0eb6370001690ee6","nickName":"陈","amount":0.5,"receiveTime":"2017-06-07 09:58:54","bestLuck":true}]
         * thumbUpCount : 0
         * currUserAmount : 0.5
         * showSendAmount : true
         * thumbUp : false
         */

        @SerializedName("avatar")
        public String avatar;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("sendCount")
        public int sendCount;
        @SerializedName("sendAmount")
        public double sendAmount;
        @SerializedName("receiveCount")
        public int receiveCount;
        @SerializedName("receiveAmount")
        public double receiveAmount;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("status")
        public int status;
        @SerializedName("template")
        public String template;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("currUserAmount")
        public double currUserAmount;
        @SerializedName("showSendAmount")
        public boolean showSendAmount;
        @SerializedName("receivedRedList")
        public ArrayList<ReceivedRedListBean> receivedRedList;

        public static class ReceivedRedListBean {
            /**
             * avatar : https://api.wdh158.com/api/v1/mgs/file/download?fid=58fdd56b0eb6370001690ee6
             * nickName : 陈
             * amount : 0.5
             * receiveTime : 2017-06-07 09:58:54
             * bestLuck : true
             */

            @SerializedName("avatar")
            public String avatar;
            @SerializedName("nickName")
            public String nickName;
            @SerializedName("amount")
            public double amount;
            @SerializedName("receiveTime")
            public String receiveTime;
        }
    }
}
