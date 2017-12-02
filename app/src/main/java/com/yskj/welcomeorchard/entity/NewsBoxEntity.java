package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/20.
 */

public class NewsBoxEntity{
    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":16,"type":0,"content":"提现申请成功，提现金额200.0，预计48小时内(节假日顺延)到账，扣除手续费3%","hasRead":true,"createTime":"2017-01-19 14:49:34"}]}
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
         * list : [{"id":16,"type":0,"content":"提现申请成功，提现金额200.0，预计48小时内(节假日顺延)到账，扣除手续费3%","hasRead":true,"createTime":"2017-01-19 14:49:34"}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public ArrayList<ListBean> list;
    }
    public static class ListBean {
        /**
         * id : 16
         * type : 0
         * content : 提现申请成功，提现金额200.0，预计48小时内(节假日顺延)到账，扣除手续费3%
         * hasRead : true
         * createTime : 2017-01-19 14:49:34
         */

        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
        @SerializedName("hasRead")
        public boolean hasRead;
        @SerializedName("createTime")
        public String createTime;
    }
}
