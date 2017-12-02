package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/2/9.
 */

public class RedDetialEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"blessWord":"恭喜发财，万事如意","totalAmount":33.98,"totalCount":1,"openAmount":33.98,"type":1,"sendRedNo":"7634102381253182","sendTime":"2017-02-08 12:12:25","receiverList":[{"nickName":"我是谁","avatar":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58842ff904e0af4c784519cb","amount":33.98,"receiveTime":"2017-02-08 13:36:54"}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * blessWord : 恭喜发财，万事如意
         * totalAmount : 33.98
         * totalCount : 1
         * openAmount : 33.98
         * type : 1
         * sendRedNo : 7634102381253182
         * sendTime : 2017-02-08 12:12:25
         * receiverList : [{"nickName":"我是谁","avatar":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58842ff904e0af4c784519cb","amount":33.98,"receiveTime":"2017-02-08 13:36:54"}]
         */

        @SerializedName("blessWord")
        public String blessWord;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("totalCount")
        public int totalCount;
        @SerializedName("openAmount")
        public double openAmount;
        @SerializedName("type")
        public int type;
        @SerializedName("sendRedNo")
        public String sendRedNo;
        @SerializedName("sendTime")
        public String sendTime;
        @SerializedName("receiverList")
        public List<ReceiverListBean> receiverList;

        public static class ReceiverListBean {
            /**
             * nickName : 我是谁
             * avatar : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58842ff904e0af4c784519cb
             * amount : 33.98
             * receiveTime : 2017-02-08 13:36:54
             */

            @SerializedName("nickName")
            public String nickName;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("amount")
            public double amount;
            @SerializedName("receiveTime")
            public String receiveTime;
        }
    }
}
