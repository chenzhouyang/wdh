package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/1/21.
 */

public class UserInfoEntity {


    /**
     * code : 0
     * message : 成功
     * data : {"realnameVo":{"realNameId":2,"name":"dsaf","identityCardNo":"411023199007156538","createTime":""},"cardCount":0,"accountPasswordExist":false,"spreaderVo":{},"userVo":{"id":11,"nickName":"尚客会员65921","avatar":"http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da","mobile":"15936365921","level":0,"fundAccount":0,"cloudAccount":10,"totalAmount":0,"totalMAccount":0,"maccount":20,"gpaccount":0.03,"spreadCode":600011}}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * realnameVo : {"realNameId":2,"name":"dsaf","identityCardNo":"411023199007156538","createTime":""}
         * cardCount : 0
         * accountPasswordExist : false
         * spreaderVo : {}
         * userVo : {"id":11,"nickName":"尚客会员65921","avatar":"http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da","mobile":"15936365921","level":0,"fundAccount":0,"cloudAccount":10,"totalAmount":0,"totalMAccount":0,"maccount":20,"gpaccount":0.03,"spreadCode":600011}
         */

        @SerializedName("realnameVo")
        public RealnameVoBean realnameVo;
        @SerializedName("cardCount")
        public double cardCount;
        @SerializedName("accountPasswordExist")
        public boolean accountPasswordExist;
        @SerializedName("spreaderVo")
        public SpreaderVoBean spreaderVo;
        @SerializedName("userVo")
        public UserVoBean userVo;
        @SerializedName("shopStatus")
        public int shopStatus;

        public static class RealnameVoBean {
            /**
             * realNameId : 2
             * name : dsaf
             * identityCardNo : 411023199007156538
             * createTime :
             */

            @SerializedName("realNameId")
            public int realNameId;
            @SerializedName("name")
            public String name;
            @SerializedName("identityCardNo")
            public String identityCardNo;
            @SerializedName("createTime")
            public String createTime;
        }

        public static class SpreaderVoBean {
            /**
             * nickname : 小主14977
             * avatar : 0
             * mobile : 13938914977
             * level : 0
             */

            @SerializedName("nickname")
            public String nickname;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("mobile")
            public String mobile;
            @SerializedName("level")
            public int level;
        }

        public static class UserVoBean {
            /**
             * id : 11
             * nickName : 尚客会员65921
             * avatar : http://192.168.0.80:8080/mgs/file/download?fid=5885bf7fa7b11b0001f8b0da
             * mobile : 15936365921
             * level : 0
             * fundAccount : 0
             * cloudAccount : 10
             * totalAmount : 0
             * totalMAccount : 0
             * maccount : 20
             * gpaccount : 0.03
             * spreadCode : 600011
             * sex :
             * age:
             */

            @SerializedName("id")
            public int id;
            @SerializedName("nickName")
            public String nickName;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("mobile")
            public String mobile;
            @SerializedName("level")
            public int level;
            @SerializedName("fundAccount")
            public double fundAccount;
            @SerializedName("cloudAccount")
            public double cloudAccount;
            @SerializedName("totalAmount")
            public double totalAmount;
            @SerializedName("totalMAccount")
            public double totalMAccount;
            @SerializedName("maccount")
            public double maccount;
            @SerializedName("gpaccount")
            public double gpaccount;
            @SerializedName("spreadCode")
            public int spreadCode;
            @SerializedName("sex")
            public String sex;
            @SerializedName("age")
            public String age;
        }
    }
}
