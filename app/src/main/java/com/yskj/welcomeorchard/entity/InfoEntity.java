package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/22.
 */

public class InfoEntity {
    /**
     * code : 0
     * message : 成功
     * data : ["58846a1804e0af80486fab29"]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<String> data;
}
