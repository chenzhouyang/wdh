package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 陈宙洋
 * 2017/8/2.
 */

public class SupplyEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"goodsList":[{"goodId":1,"name":"好看的运动裤","barCode":"123456789","price":10,"mAccount":5,"volume":10,"cover":"C:/Users/July/Pictures/n.jpg"},{"goodId":13,"name":"测试商品20170804","barCode":"201708040001","price":1000,"mAccount":200,"volume":0,"cover":"C:/Users/July/Pictures/201708021752423364734.jpg"}],"count":2}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * cursor : 0
         * goodsList : [{"goodId":1,"name":"好看的运动裤","barCode":"123456789","price":10,"mAccount":5,"volume":10,"cover":"C:/Users/July/Pictures/n.jpg"},{"goodId":13,"name":"测试商品20170804","barCode":"201708040001","price":1000,"mAccount":200,"volume":0,"cover":"C:/Users/July/Pictures/201708021752423364734.jpg"}]
         * count : 2
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("goodsList")
        public List<GoodsListBean> goodsList;

        public static class GoodsListBean {
            /**
             * goodId : 1
             * name : 好看的运动裤
             * barCode : 123456789
             * price : 10
             * mAccount : 5
             * volume : 10
             * cover : C:/Users/July/Pictures/n.jpg
             */

            @SerializedName("goodId")
            public int goodId;
            @SerializedName("name")
            public String name;
            @SerializedName("barCode")
            public String barCode;
            @SerializedName("price")
            public double price;
            @SerializedName("mAccount")
            public double mAccount;
            @SerializedName("volume")
            public int volume;
            @SerializedName("cover")
            public String cover;
            @SerializedName("shopName")
            public String shopname;
            @SerializedName("millId")
            public String millId;
        }
    }
}
