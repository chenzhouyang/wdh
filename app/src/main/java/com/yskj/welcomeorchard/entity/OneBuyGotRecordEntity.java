package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/27on 10:35.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OneBuyGotRecordEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"list":[{"activityId":2,"userId":34,"mobile":"186****2627","nickName":"银色的猫","avatar":"http://192.168.0.150:8080/api/v1/mgs/file/download?fid=58a521f664ac351bb47e7c73","rank":1},{"activityId":2,"userId":35,"mobile":"135****7549","nickName":"SHISHIHAN","avatar":"","rank":2}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public static class ListBean {
            /**
             * activityId : 2
             * userId : 34
             * mobile : 186****2627
             * nickName : 银色的猫
             * avatar : http://192.168.0.150:8080/api/v1/mgs/file/download?fid=58a521f664ac351bb47e7c73
             * rank : 1
             */

            @SerializedName("activityId")
            public int activityId;
            @SerializedName("userId")
            public int userId;
            @SerializedName("mobile")
            public String mobile;
            @SerializedName("nickName")
            public String nickName;
            @SerializedName("avatar")
            public String avatar;
            @SerializedName("rank")
            public int rank;
            @SerializedName("refundAmount")
            public double refundAmount;
        }
    }
}
