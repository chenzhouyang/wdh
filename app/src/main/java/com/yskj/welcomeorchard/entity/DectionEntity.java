package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/5/16.
 */

public class DectionEntity {


    /**
     * code : 0
     * message : 成功
     * data : {"list":[{"id":1,"name":"tete","profile":"teteteazad","type":1,"deleted":false,"price":10.2,"orderNo":1,"createTime":"2017-05-15 17:04:35"}]}
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
             * id : 1
             * name : tete
             * profile : teteteazad
             * type : 1
             * deleted : false
             * price : 10.2
             * orderNo : 1
             * createTime : 2017-05-15 17:04:35
             */

            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
            @SerializedName("profile")
            public String profile;
            @SerializedName("type")
            public int type;
            @SerializedName("deleted")
            public boolean deleted;
            @SerializedName("price")
            public double price;
            @SerializedName("orderNo")
            public int orderNo;
            @SerializedName("createTime")
            public String createTime;
        }
    }
}
