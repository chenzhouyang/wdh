package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class SignInDateEntity extends BaseEntity {
    @SerializedName("ret_data")
    public RetData retData;
    public static class RetData{
        @SerializedName("sign_today")
        public boolean isSign;
        @SerializedName("sign_number")
        public int signNumber;
    }
}
