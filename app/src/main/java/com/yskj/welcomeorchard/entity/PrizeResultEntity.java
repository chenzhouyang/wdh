package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/5/17on 15:00.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class PrizeResultEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * list : {"id":"6","name":"凯卓纯净之水男用香水 30ml","type":"2","rec_id":"39"}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("list")
    public ListBean list;

    public static class ListBean {
        /**
         * id : 6
         * name : 凯卓纯净之水男用香水 30ml
         * type : 2
         * rec_id : 39
         */

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("type")
        public String type;
        @SerializedName("rec_id")
        public String recId;
        @SerializedName("code")
        public String code;
    }
}
