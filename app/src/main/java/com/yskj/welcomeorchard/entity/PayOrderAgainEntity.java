package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/5/6.
 */

public class PayOrderAgainEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * order : {"order_id":"1748","order_sn":"201705051648016960","total_amount":"1132.00","user_money":"581.98","order_amount":"550.02"}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("order")
    public OrderBean order;

    public static class OrderBean {
        /**
         * order_id : 1748
         * order_sn : 201705051648016960
         * total_amount : 1132.00
         * user_money : 581.98
         * order_amount : 550.02
         */

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_sn")
        public String orderSn;
        @SerializedName("total_amount")
        public String totalAmount;
        @SerializedName("user_money")
        public String userMoney;
        @SerializedName("order_amount")
        public String orderAmount;
    }
}
