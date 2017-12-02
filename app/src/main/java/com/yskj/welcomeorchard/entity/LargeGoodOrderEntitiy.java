package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class LargeGoodOrderEntitiy {
    /**
     * code : 0
     * message : 成功
     * data : {"id":2,"orderId":1,"goodName":"商品名称","rechargeCard":"6228480402564890018","cardOwner":"小杨","rechargeAmount":5500.8,"type":0,"credentials":[{"id":"57e650b6ec74a47d886667e6","url":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b6ec74a47d886667e6"},{"id":"57e650b6ec74a47d886667e8","url":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b6ec74a47d886667e8"}],"status":1,"remark":null,"createTime":"2017-03-20 15:52:35"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * id : 2
         * orderId : 1
         * goodName : 商品名称
         * rechargeCard : 6228480402564890018
         * cardOwner : 小杨
         * rechargeAmount : 5500.8
         * type : 0
         * credentials : [{"id":"57e650b6ec74a47d886667e6","url":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b6ec74a47d886667e6"},{"id":"57e650b6ec74a47d886667e8","url":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b6ec74a47d886667e8"}]
         * status : 1
         * remark : null
         * createTime : 2017-03-20 15:52:35
         */

        @SerializedName("id")
        public int id;
        @SerializedName("orderId")
        public String orderId;
        @SerializedName("goodName")
        public String goodName;
        @SerializedName("rechargeCard")
        public String rechargeCard;
        @SerializedName("cardOwner")
        public String cardOwner;
        @SerializedName("rechargeAmount")
        public double rechargeAmount;
        @SerializedName("type")
        public int type;
        @SerializedName("status")
        public int status;
        @SerializedName("remark")
        public Object remark;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("credentials")
        public List<CredentialsBean> credentials;

        public static class CredentialsBean {
            /**
             * id : 57e650b6ec74a47d886667e6
             * url : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b6ec74a47d886667e6
             */

            @SerializedName("id")
            public String id;
            @SerializedName("url")
            public String url;
        }
    }
}
