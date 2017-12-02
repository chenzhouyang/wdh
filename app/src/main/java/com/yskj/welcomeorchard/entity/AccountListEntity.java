package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class AccountListEntity {


    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":4,"senderNick":"尚客会员65921","senderMobile":"15936365921","senderAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da","receiverNick":"云客00234","receiverMobile":"15225700234","receiverAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=58848fce1aa8410001665938","amount":-100,"note":"恭喜发财","createTime":"2017-01-21 09:24:13"},{"id":3,"senderNick":"尚客会员65921","senderMobile":"15936365921","senderAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da","receiverNick":"云客00234","receiverMobile":"15225700234","receiverAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=58848fce1aa8410001665938","amount":-100,"note":"恭喜发财","createTime":"2017-01-21 09:23:14"}]}
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
         * list : [{"id":4,"senderNick":"尚客会员65921","senderMobile":"15936365921","senderAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da","receiverNick":"云客00234","receiverMobile":"15225700234","receiverAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=58848fce1aa8410001665938","amount":-100,"note":"恭喜发财","createTime":"2017-01-21 09:24:13"},{"id":3,"senderNick":"尚客会员65921","senderMobile":"15936365921","senderAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da","receiverNick":"云客00234","receiverMobile":"15225700234","receiverAvatar":"http://192.168.0.80:8080/mgs/file/download?fid=58848fce1aa8410001665938","amount":-100,"note":"恭喜发财","createTime":"2017-01-21 09:23:14"}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public static class ListBean {
            /**
             * id : 4
             * senderNick : 尚客会员65921
             * senderMobile : 15936365921
             * senderAvatar : http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da
             * receiverNick : 云客00234
             * receiverMobile : 15225700234
             * receiverAvatar : http://192.168.0.80:8080/mgs/file/download?fid=58848fce1aa8410001665938
             * amount : -100.0
             * note : 恭喜发财
             * createTime : 2017-01-21 09:24:13
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
}
