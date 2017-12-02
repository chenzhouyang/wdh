package com.yskj.welcomeorchard.base;

/*
 * @description
 * BaseResult.java
 * classes:com.vcyber.drivingservice.eneity.BaseResult
 * @author yym create at 2014-10-30下午3:49:03
*/

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

/**
 * @description:实体基类
 */
public class BaseEntity {
    @SerializedName("error_code")
    public int error_code;
    @SerializedName("error_msg")
    public String error_msg;


    public void parser(String src) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    public <T> T parserT(String src) {
        // TODO Auto-generated method stub
        if (src == null || src.length() == 0) {
            return null;
        }
        Logger.json(src);

        Gson gson = new Gson();
        return (T) gson.fromJson(src, this.getClass());
    }

}
