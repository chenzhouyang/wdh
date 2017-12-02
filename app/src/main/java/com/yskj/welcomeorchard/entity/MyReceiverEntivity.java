package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/3/15.
 */

public class MyReceiverEntivity {

    /**
     * data : {"data":"\"测试数据\"","type":1,"sound":"user_shb"}
     */

    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * data : "测试数据"
         * type : 1
         * sound : user_shb
         */

        @SerializedName("data")
        public String data;
        @SerializedName("type")
        public int type;
        @SerializedName("sound")
        public String sound;
    }
}
