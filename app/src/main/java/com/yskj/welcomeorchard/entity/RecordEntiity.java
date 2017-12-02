package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class RecordEntiity {

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":24,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:30:13","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":23,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:29:53","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":22,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:29:47","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":21,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:02:20","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":20,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-19 14:49:33","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":19,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-19 13:25:42","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":18,"amount":300,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-19 13:25:14","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"}]}
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
         * list : [{"id":24,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:30:13","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":23,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:29:53","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":22,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:29:47","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":21,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-20 10:02:20","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":20,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-19 14:49:33","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":19,"amount":200,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-19 13:25:42","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"},{"id":18,"amount":300,"note":"事业有成","state":1,"reviewNickName":"","createTime":"2017-01-19 13:25:14","cardNo":"6212261708000959095","cardType":0,"bank":"工商银行借记卡\n"}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public static class ListBean {
            /**
             * id : 24
             * amount : 200
             * note : 事业有成
             * state : 1
             * reviewNickName :
             * createTime : 2017-01-20 10:30:13
             * cardNo : 6212261708000959095
             * cardType : 0
             * bank : 工商银行借记卡

             */

            @SerializedName("id")
            public int id;
            @SerializedName("amount")
            public double amount;
            @SerializedName("note")
            public String note;
            @SerializedName("state")
            public int state;
            @SerializedName("reviewNickName")
            public String reviewNickName;
            @SerializedName("createTime")
            public String createTime;
            @SerializedName("cardNo")
            public String cardNo;
            @SerializedName("cardType")
            public int cardType;
            @SerializedName("bank")
            public String bank;
        }
    }
}
