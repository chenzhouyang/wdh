package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/23.
 */

public class FileEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * img : ["/Public/upload/comment/2017-01-23/5885b297cf642.png","/Public/upload/comment/2017-01-23/5885b297cf7e4.png"]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("img")
    public ArrayList<String> img;
}
