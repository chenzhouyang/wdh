package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class AccounsEntity extends BaseEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"id":10,"senderNick":"1","senderMobile":"15103749464","senderAvatar":"1","receiverNick":"云客57549","receiverMobile":"13569457549","receiverAvatar":"","amount":100,"note":"恭喜发财","createTime":"2017-01-19 13:59:15"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * id : 10
         * senderNick : 1
         * senderMobile : 15103749464
         * senderAvatar : 1
         * receiverNick : 云客57549
         * receiverMobile : 13569457549
         * receiverAvatar :
         * amount : 100
         * note : 恭喜发财
         * createTime : 2017-01-19 13:59:15
         */

        @SerializedName("id")
        public int id;
        @SerializedName("senderNick")
        public String senderNick;
        @SerializedName("senderMobile")
        public String senderMobile;
        @SerializedName("senderAvatar")
        public String senderAvatar;
        @SerializedName("receiverNick")
        public String receiverNick;
        @SerializedName("receiverMobile")
        public String receiverMobile;
        @SerializedName("receiverAvatar")
        public String receiverAvatar;
        @SerializedName("amount")
        public double amount;
        @SerializedName("note")
        public String note;
        @SerializedName("createTime")
        public String createTime;
    }
}
