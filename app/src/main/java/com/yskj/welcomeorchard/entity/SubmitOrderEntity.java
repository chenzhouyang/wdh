package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-JH on 2017/1/21.
 */

public class SubmitOrderEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * order_sn : XXX
     * total_amount : XXX
     * user_money : XXX
     * order_amount : XXX
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("order_sn")
    public String orderSn;
    @SerializedName("total_amount")
    public String totalAmount;
    @SerializedName("user_money")
    public String userMoney;
    @SerializedName("order_amount")
    public String orderAmount;
    @SerializedName("order_id")
    public String orderid;
}
