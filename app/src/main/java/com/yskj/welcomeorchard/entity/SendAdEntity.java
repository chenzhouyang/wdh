package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/3/17on 11:25.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class SendAdEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"adRedId":24}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * adRedId : 24
         */

        @SerializedName("adRedId")
        public int adRedId;
    }
}
