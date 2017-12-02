package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/5/15on 14:44.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AdClearEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
}
