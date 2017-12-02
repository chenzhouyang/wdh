package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/21.
 */

public class ConfirmOrderEntity {


    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("carPrice")
    public CarPriceBean carPrice;
    @SerializedName("cartList")
    public ArrayList<CartListBean> cartList;

    public static class CarPriceBean {


        /**
         * total_fee : 0.02
         * postFee : 0
         * balance : 0
         * payables : 0.02
         * goodsFee : 0.02
         * order_prom_id : 0
         * order_prom_amount : 0
         * anum : 2
         * is_delivery : 1
         */

        @SerializedName("total_fee")
        public double totalFee;
        @SerializedName("postFee")
        public int postFee;
        @SerializedName("balance")
        public String balance;
        @SerializedName("payables")
        public String payables;
        @SerializedName("goodsFee")
        public double goodsFee;
        @SerializedName("order_prom_id")
        public int orderPromId;
        @SerializedName("order_prom_amount")
        public int orderPromAmount;
        @SerializedName("anum")
        public int anum;
        @SerializedName("is_delivery")
        public int isDelivery;
    }

    public static class CartListBean {
        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_sn")
        public String goodsSn;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("market_price")
        public String marketPrice;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("goods_num")
        public String goodsNum;
        @SerializedName("spec_key_name")
        public String specKeyName;
        @SerializedName("give_integral")
        public String giveIntegral;
        @SerializedName("original_img")
        public String originalImg;
        @SerializedName("freight_type")
        public int freightType;
    }
}
