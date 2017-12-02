package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class ProveEntitiy {

    /**
     * code : 0
     * message : 成功
     * data : {"realNameId":1,"name":"刘银芳","identityCardNo":"411024198208156229","sex":null,"createTime":"2017-01-18 20:24:20"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * realNameId : 1
         * name : 刘银芳
         * identityCardNo : 411024198208156229
         * sex : null
         * createTime : 2017-01-18 20:24:20
         */

        @SerializedName("realNameId")
        public int realNameId;
        @SerializedName("name")
        public String name;
        @SerializedName("identityCardNo")
        public String identityCardNo;
        @SerializedName("sex")
        public Object sex;
        @SerializedName("createTime")
        public String createTime;
    }
}
