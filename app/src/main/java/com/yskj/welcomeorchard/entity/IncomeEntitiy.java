package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class IncomeEntitiy {


    /**
     * code : 0
     * message : 成功
     * data : [{"id":45,"nickName":"尚客会员41009","level":1,"totalMAccount":425000,"mobile":"18607141009","createTime":"2017-02-14 10:33:14","totalReceiveRed":0}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * id : 45
         * nickName : 尚客会员41009
         * level : 1
         * totalMAccount : 425000
         * mobile : 18607141009
         * createTime : 2017-02-14 10:33:14
         * totalReceiveRed : 0
         */

        @SerializedName("id")
        public int id;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("level")
        public int level;
        @SerializedName("totalMAccount")
        public double totalMAccount;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("totalReceiveRed")
        public double totalReceiveRed;
    }
}
