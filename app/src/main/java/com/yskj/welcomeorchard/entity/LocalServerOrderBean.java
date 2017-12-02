package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghe on 2016/11/14 0014.
 */
public class LocalServerOrderBean {

    /**
     * code : 0
     * message : 成功
     * data : {"orderVos":[{"cashCouponId":559,"orderId":571,"orderNo":"2017022313181121073","shopId":7,"createTime":"2017-02-23 13:18:11","status":1,"totalAmount":12,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"黄哥内衣旗舰店","goodsList":[{"orderId":571,"shopId":7,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58a5592a64ac351bb47e7c81","goodsName":"大法师","price":12,"deduPoint":0,"count":1}]},{"cashCouponId":342,"orderId":361,"orderNo":"2016112111402166048","shopId":5,"createTime":"2016-11-21 11:40:21","status":1,"totalAmount":380,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"电商","goodsList":[{"orderId":361,"shopId":5,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d9a04f676fd63502d41c1","goodsName":"美女","price":18.22,"deduPoint":0,"count":1}]},{"cashCouponId":341,"orderId":360,"orderNo":"2016112111385942017","shopId":5,"createTime":"2016-11-21 11:39:00","status":1,"totalAmount":380,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"电商","goodsList":[{"orderId":360,"shopId":5,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d9a04f676fd63502d41c1","goodsName":"美女","price":18.22,"deduPoint":0,"count":1}]},{"cashCouponId":339,"orderId":358,"orderNo":"2016112110565331385","shopId":17,"createTime":"2016-11-21 10:56:54","status":1,"totalAmount":1000,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"超大号","goodsList":[{"orderId":358,"shopId":17,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e7880f676fd69b5657e2f","goodsName":"不上档次闷倒驴","price":1000,"deduPoint":0,"count":1}]},{"cashCouponId":321,"orderId":340,"orderNo":"2016111911560088861","shopId":17,"createTime":"2016-11-19 11:56:01","status":1,"totalAmount":1000,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"超大号","goodsList":[{"orderId":340,"shopId":17,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e7880f676fd69b5657e2f","goodsName":"不上档次闷倒驴","price":1000,"deduPoint":0,"count":1}]}],"count":5,"offset":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * orderVos : [{"cashCouponId":559,"orderId":571,"orderNo":"2017022313181121073","shopId":7,"createTime":"2017-02-23 13:18:11","status":1,"totalAmount":12,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"黄哥内衣旗舰店","goodsList":[{"orderId":571,"shopId":7,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58a5592a64ac351bb47e7c81","goodsName":"大法师","price":12,"deduPoint":0,"count":1}]},{"cashCouponId":342,"orderId":361,"orderNo":"2016112111402166048","shopId":5,"createTime":"2016-11-21 11:40:21","status":1,"totalAmount":380,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"电商","goodsList":[{"orderId":361,"shopId":5,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d9a04f676fd63502d41c1","goodsName":"美女","price":18.22,"deduPoint":0,"count":1}]},{"cashCouponId":341,"orderId":360,"orderNo":"2016112111385942017","shopId":5,"createTime":"2016-11-21 11:39:00","status":1,"totalAmount":380,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"电商","goodsList":[{"orderId":360,"shopId":5,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d9a04f676fd63502d41c1","goodsName":"美女","price":18.22,"deduPoint":0,"count":1}]},{"cashCouponId":339,"orderId":358,"orderNo":"2016112110565331385","shopId":17,"createTime":"2016-11-21 10:56:54","status":1,"totalAmount":1000,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"超大号","goodsList":[{"orderId":358,"shopId":17,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e7880f676fd69b5657e2f","goodsName":"不上档次闷倒驴","price":1000,"deduPoint":0,"count":1}]},{"cashCouponId":321,"orderId":340,"orderNo":"2016111911560088861","shopId":17,"createTime":"2016-11-19 11:56:01","status":1,"totalAmount":1000,"totalDeduPoints":0,"dispatchAmount":0,"shopName":"超大号","goodsList":[{"orderId":340,"shopId":17,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e7880f676fd69b5657e2f","goodsName":"不上档次闷倒驴","price":1000,"deduPoint":0,"count":1}]}]
         * count : 5
         * offset : 0
         */

        @SerializedName("count")
        public int count;
        @SerializedName("offset")
        public int offset;
        @SerializedName("orderVos")
        public ArrayList<OrderVosBean> orderVos;

        public static class OrderVosBean {
            /**
             * cashCouponId : 559
             * orderId : 571
             * orderNo : 2017022313181121073
             * shopId : 7
             * createTime : 2017-02-23 13:18:11
             * status : 1
             * totalAmount : 12
             * totalDeduPoints : 0
             * dispatchAmount : 0
             * shopName : 黄哥内衣旗舰店
             * goodsList : [{"orderId":571,"shopId":7,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58a5592a64ac351bb47e7c81","goodsName":"大法师","price":12,"deduPoint":0,"count":1}]
             */

            @SerializedName("cashCouponId")
            public int cashCouponId;
            @SerializedName("orderId")
            public int orderId;
            @SerializedName("orderNo")
            public String orderNo;
            @SerializedName("shopId")
            public int shopId;
            @SerializedName("createTime")
            public String createTime;
            @SerializedName("status")
            public int status;
            @SerializedName("totalAmount")
            public double totalAmount;
            @SerializedName("totalDeduPoints")
            public double totalDeduPoints;
            @SerializedName("dispatchAmount")
            public double dispatchAmount;
            @SerializedName("consumePassword")
            public String consumePassword;
            @SerializedName("shopName")
            public String shopName;
            @SerializedName("goodsList")

            public List<GoodsListBean> goodsList;

            public static class GoodsListBean {
                /**
                 * orderId : 571
                 * shopId : 7
                 * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58a5592a64ac351bb47e7c81
                 * goodsName : 大法师
                 * price : 12
                 * deduPoint : 0
                 * count : 1
                 */

                @SerializedName("orderId")
                public int orderId;
                @SerializedName("shopId")
                public int shopId;
                @SerializedName("cover")
                public String cover;
                @SerializedName("goodsName")
                public String goodsName;
                @SerializedName("price")
                public double price;
                @SerializedName("deduPoint")
                public double deduPoint;
                @SerializedName("count")
                public int count;
            }
        }
    }
}
