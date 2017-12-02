package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class User {
    @SerializedName("mobile")
    public String phoneNum;
    @SerializedName("password")
    public String phonePwd;
    public User(String phoneNum, String phonePwd) {
        this.phoneNum = phoneNum;
        this.phonePwd = phonePwd;
    }
}
