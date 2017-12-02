package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/2/13.
 */

public class RedDetailsEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"blessWord":"恭喜发财，万事如意","totalAmount":700.8199999999999,"totalCount":3,"openAmount":224.26,"redType":2,"sendRedNo":"20170306180517643401","sendTime":"2017-03-06 18:05:18","receiverList":[{"nickName":"唯多95315","avatar":"","amount":224.26,"receiveTime":"2017-03-06 18:06:51"},{"nickName":"晴雪艾特","avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba562f9555dd0001ba2526","amount":92.92,"receiveTime":"2017-03-06 18:06:48"},{"nickName":"诗诗涵","avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58bd14ee2476620001f4b9cb","amount":383.64,"receiveTime":"2017-03-06 18:06:10"}]}
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
         * totalAmount : 700.8199999999999
         * totalCount : 3
         * openAmount : 224.26
         * redType : 2
         * sendRedNo : 20170306180517643401
         * sendTime : 2017-03-06 18:05:18
         * receiverList : [{"nickName":"唯多95315","avatar":"","amount":224.26,"receiveTime":"2017-03-06 18:06:51"},{"nickName":"晴雪艾特","avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba562f9555dd0001ba2526","amount":92.92,"receiveTime":"2017-03-06 18:06:48"},{"nickName":"诗诗涵","avatar":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58bd14ee2476620001f4b9cb","amount":383.64,"receiveTime":"2017-03-06 18:06:10"}]
         */

        @SerializedName("blessWord")
        public String blessWord;
        @SerializedName("totalCount")
        public int totalCount;
        @SerializedName("openAmount")
        public double openAmount;
        @SerializedName("redType")
        public int redType;
        @SerializedName("sendRedNo")
        public String sendRedNo;
        @SerializedName("sendTime")
        public String sendTime;
        @SerializedName("receiverList")
        public ArrayList<ReceiverListBean> receiverList;

        public static class ReceiverListBean {
            /**
             * nickName : 唯多95315
             * avatar :
             * amount : 224.26
             * receiveTime : 2017-03-06 18:06:51
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
