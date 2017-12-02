package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by jianghe on 2016/11/11 0011.
 */
public class LocalServerNearPopBean {
    @SerializedName("code")
    public int error_code;
    @SerializedName("message")
    public String error_msg;
    @SerializedName("data")
    public ArrayList<RetData> ret_data;

    public static class RetData{
        @SerializedName("cateId")
        public String cateId;
        @SerializedName("fee")
        public String fee;
        @SerializedName("isHot")
        public boolean isHot;
        @SerializedName("name")
        public String name;
        @SerializedName("parentId")
        public String parentId;
        @SerializedName("profile")
        public String profile;
        @SerializedName("sequence")
        public String sequence;
        @SerializedName("icon")
        public String icon;
        @SerializedName("children")
        public ArrayList<Children> childrens;
    }
    public static class Children{
        @SerializedName("cateId")
        public String cateId;
        @SerializedName("fee")
        public String fee;
        @SerializedName("isHot")
        public boolean isHot;
        @SerializedName("name")
        public String name;
        @SerializedName("parentId")
        public String parentId;
        @SerializedName("profile")
        public String profile;
        @SerializedName("sequence")
        public String sequence;

        @SerializedName("children")
        public ArrayList<Children> childrens;
    }
}
