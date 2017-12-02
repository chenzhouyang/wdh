package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/1/21.
 */

public class LoginPassWordEntity {

    /**
     * code : 0
     * message : 成功
     * data : SUCCESS
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public String data;
}
