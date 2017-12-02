package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：陈宙洋
 * 日期：2017/8/14.
 * 描述：订单实体
 */

public class SupplyOrderEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":6,"orderNo":"zx2017081117061866314","orderStatus":0,"shippingStatus":0,"payStatus":0,"shippingPrice":0,"totalAmount":200,"saleStatus":0,"type":1,"millId":1,"millName":"ceshi","bonusStatus":0,"goodsCount":2,"goodsList":[{"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"specId":1,"specName":"颜色：白色  尺寸：xl","specPrice":100,"cover":""},{"goodsId":13,"goodsName":"测试商品20170804","goodsCount":1,"specId":8,"specName":"白色，x号","specPrice":100,"cover":""}]},{"id":5,"orderNo":"zx2017081016232844824","orderStatus":0,"shippingStatus":2,"payStatus":0,"shippingPrice":0,"totalAmount":100,"saleStatus":0,"type":1,"millId":1,"millName":"ceshi","bonusStatus":2,"goodsCount":1,"goodsList":[{"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"specId":1,"specName":"颜色：白色  尺寸：xl","specPrice":100,"cover":""}]}]}
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
         * count : 10
         * list : [{"id":6,"orderNo":"zx2017081117061866314","orderStatus":0,"shippingStatus":0,"payStatus":0,"shippingPrice":0,"totalAmount":200,"saleStatus":0,"type":1,"millId":1,"millName":"ceshi","bonusStatus":0,"goodsCount":2,"goodsList":[{"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"specId":1,"specName":"颜色：白色  尺寸：xl","specPrice":100,"cover":""},{"goodsId":13,"goodsName":"测试商品20170804","goodsCount":1,"specId":8,"specName":"白色，x号","specPrice":100,"cover":""}]},{"id":5,"orderNo":"zx2017081016232844824","orderStatus":0,"shippingStatus":2,"payStatus":0,"shippingPrice":0,"totalAmount":100,"saleStatus":0,"type":1,"millId":1,"millName":"ceshi","bonusStatus":2,"goodsCount":1,"goodsList":[{"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"specId":1,"specName":"颜色：白色  尺寸：xl","specPrice":100,"cover":""}]}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 6
             * orderNo : zx2017081117061866314
             * orderStatus : 0
             * shippingStatus : 0
             * payStatus : 0
             * shippingPrice : 0.0
             * totalAmount : 200.0
             * saleStatus : 0
             * type : 1
             * millId : 1
             * millName : ceshi
             * bonusStatus : 0
             * goodsCount : 2
             * goodsList : [{"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"specId":1,"specName":"颜色：白色  尺寸：xl","specPrice":100,"cover":""},{"goodsId":13,"goodsName":"测试商品20170804","goodsCount":1,"specId":8,"specName":"白色，x号","specPrice":100,"cover":""}]
             */

            @SerializedName("id")
            public int id;
            @SerializedName("orderNo")
            public String orderNo;
            @SerializedName("orderStatus")
            public int orderStatus;
            @SerializedName("shippingStatus")
            public int shippingStatus;
            @SerializedName("payStatus")
            public int payStatus;
            @SerializedName("shippingPrice")
            public double shippingPrice;
            @SerializedName("totalAmount")
            public double totalAmount;
            @SerializedName("saleStatus")
            public int saleStatus;
            @SerializedName("type")
            public int type;
            @SerializedName("millId")
            public int millId;
            @SerializedName("millName")
            public String millName;
            @SerializedName("bonusStatus")
            public int bonusStatus;
            @SerializedName("goodsCount")
            public int goodsCount;
            @SerializedName("shippingName")
            public String shippingName;
            @SerializedName("shippingType")
            public String shippingType;
            @SerializedName("shippingCode")
            public String shippingCode;
            @SerializedName("goodsList")
            public List<GoodsListBean> goodsList;

            public static class GoodsListBean {
                /**
                 * goodsId : 1
                 * goodsName : 好看的运动裤
                 * goodsCount : 1
                 * specId : 1
                 * specName : 颜色：白色  尺寸：xl
                 * specPrice : 100.0
                 * cover :
                 */

                @SerializedName("goodsId")
                public int goodsId;
                @SerializedName("goodsName")
                public String goodsName;
                @SerializedName("goodsCount")
                public int goodsCount;
                @SerializedName("specId")
                public int specId;
                @SerializedName("specName")
                public String specName;
                @SerializedName("specPrice")
                public double specPrice;
                @SerializedName("cover")
                public String cover;
            }
        }
    }
}
