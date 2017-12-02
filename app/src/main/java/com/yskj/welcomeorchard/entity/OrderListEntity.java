package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class OrderListEntity extends BaseEntity implements Serializable {


    /**
     * type : WAITPAY
     * search_key :
     * page : {"firstRow":0,"listRows":2,"parameter":{"uid":"32","sk":"2ab99a6af22365686e97992df974d5c2","type":"WAITPAY"},"totalRows":"17","totalPages":9,"rollPage":11,"lastSuffix":true,"p":"p","url":"","nowPage":1}
     * order_list : [{"order_id":"307","order_sn":"201701171722177905","order_status":"0","province":"636","city":"637","district":"639","address":"abcdefg","pay_status":"0","shipping_status":"0","user_money":"4866.00","total_amount":"4866.00","order_amount":"0.00","consignee":"jxy","mobile":"13847569639","add_time":"1484644937","shipping_time":"0","confirm_time":"0","pay_time":"0","shipping_code":"0","shipping_name":"0","shipping_price":"8.00","goods_price":"4858.00","order_status_code":"WAITPAY","order_status_desc":"待支付","pay_btn":1,"cancel_btn":1,"receive_btn":0,"comment_btn":0,"shipping_btn":0,"return_btn":0,"shopName":"绿尚客","invoice_no":0,"full_address":"河北省石家庄市长安区abcdefg","goods_list":[{"rec_id":"338","order_id":"307","goods_id":"140","goods_name":"Apple iPhone 6s 16GB 玫瑰金色 移动联通电信4G手机","goods_sn":"TP0000140","goods_num":"1","market_price":"4958.00","goods_price":"4858.00","cost_price":"0.00","member_goods_price":"4858.00","give_integral":"0","spec_key":"","spec_key_name":"","bar_code":"","is_comment":"0","prom_type":"0","prom_id":"0","is_send":"0","delivery_id":"0","sku":"","farmer_goods":"0","activity_red":null,"rebate_amount":null,"acc_points":"0","original_img":"/Public/upload/goods/2016/04-22/5719843a87434.jpg"}]},{"order_id":"299","order_sn":"201701161121065179","order_status":"0","province":"636","city":"637","district":"639","address":"abcdefg","pay_status":"0","shipping_status":"0","user_money":"1611.00","total_amount":"1611.00","order_amount":"0.00","consignee":"jxy","mobile":"13847569639","add_time":"1484536866","shipping_time":"0","confirm_time":"0","pay_time":"1484554142","shipping_code":"0","shipping_name":"0","shipping_price":"12.00","goods_price":"1599.00","order_status_code":"WAITPAY","order_status_desc":"待支付","pay_btn":1,"cancel_btn":1,"receive_btn":0,"comment_btn":0,"shipping_btn":0,"return_btn":0,"shopName":"绿尚客","invoice_no":0,"full_address":"河北省石家庄市长安区abcdefg","goods_list":[{"rec_id":"327","order_id":"299","goods_id":"143","goods_name":"haier海尔 BC-93TMPF 93升单门冰箱","goods_sn":"TP0000143","goods_num":"1","market_price":"799.00","goods_price":"599.00","cost_price":"0.00","member_goods_price":"599.00","give_integral":"20","spec_key":"","spec_key_name":"","bar_code":"","is_comment":"0","prom_type":"0","prom_id":"0","is_send":"0","delivery_id":"0","sku":"","farmer_goods":"0","activity_red":null,"rebate_amount":null,"acc_points":"0","original_img":"/Public/upload/goods/2016/11-01/581854b3df6fd.jpg"},{"rec_id":"328","order_id":"299","goods_id":"40","goods_name":"荣耀X2 标准版 双卡双待双通 移动/联通双4G 16GB ROM（月光银）","goods_sn":"TP0000040","goods_num":"1","market_price":"2099.00","goods_price":"1000.00","cost_price":"0.00","member_goods_price":"1000.00","give_integral":"0","spec_key":"41_47","spec_key_name":"尺寸:7寸及以下 内存:16G","bar_code":"","is_comment":"0","prom_type":"0","prom_id":"0","is_send":"0","delivery_id":"0","sku":"","farmer_goods":"0","activity_red":null,"rebate_amount":null,"acc_points":"0","original_img":"/Public/upload/goods/2016/01-13/5695bd0ba3d1d.jpg"}]}]
     */

    @SerializedName("type")
    public String type;
    @SerializedName("search_key")
    public String searchKey;
    @SerializedName("page")
    public PageBean page;
    @SerializedName("order_list")
    public ArrayList<OrderListBean> orderList;

    public static class PageBean implements Serializable{
        /**
         * firstRow : 0
         * listRows : 2
         * parameter : {"uid":"32","sk":"2ab99a6af22365686e97992df974d5c2","type":"WAITPAY"}
         * totalRows : 17
         * totalPages : 9
         * rollPage : 11
         * lastSuffix : true
         * p : p
         * url :
         * nowPage : 1
         */

        @SerializedName("firstRow")
        public int firstRow;
        @SerializedName("listRows")
        public int listRows;
        @SerializedName("parameter")
        public ParameterBean parameter;
        @SerializedName("totalRows")
        public String totalRows;
        @SerializedName("totalPages")
        public int totalPages;
        @SerializedName("rollPage")
        public int rollPage;
        @SerializedName("lastSuffix")
        public boolean lastSuffix;
        @SerializedName("p")
        public String p;
        @SerializedName("url")
        public String url;
        @SerializedName("nowPage")
        public int nowPage;

        public static class ParameterBean {
            /**
             * uid : 32
             * sk : 2ab99a6af22365686e97992df974d5c2
             * type : WAITPAY
             */

            @SerializedName("uid")
            public String uid;
            @SerializedName("sk")
            public String sk;
            @SerializedName("type")
            public String type;
        }
    }

    public static  class OrderListBean implements Serializable{
        /**
         * order_id : 307
         * order_sn : 201701171722177905
         * order_status : 0
         * province : 636
         * city : 637
         * district : 639
         * address : abcdefg
         * pay_status : 0
         * shipping_status : 0
         * user_money : 4866.00
         * total_amount : 4866.00
         * order_amount : 0.00
         * consignee : jxy
         * mobile : 13847569639
         * add_time : 1484644937
         * shipping_time : 0
         * confirm_time : 0
         * pay_time : 0
         * shipping_code : 0
         * shipping_name : 0
         * shipping_price : 8.00
         * goods_price : 4858.00
         * order_status_code : WAITPAY
         * order_status_desc : 待支付
         * pay_btn : 1
         * cancel_btn : 1
         * receive_btn : 0
         * comment_btn : 0
         * shipping_btn : 0
         * return_btn : 0
         * shopName : 绿尚客
         * invoice_no : 0
         * full_address : 河北省石家庄市长安区abcdefg
         * goods_list : [{"rec_id":"338","order_id":"307","goods_id":"140","goods_name":"Apple iPhone 6s 16GB 玫瑰金色 移动联通电信4G手机","goods_sn":"TP0000140","goods_num":"1","market_price":"4958.00","goods_price":"4858.00","cost_price":"0.00","member_goods_price":"4858.00","give_integral":"0","spec_key":"","spec_key_name":"","bar_code":"","is_comment":"0","prom_type":"0","prom_id":"0","is_send":"0","delivery_id":"0","sku":"","farmer_goods":"0","activity_red":null,"rebate_amount":null,"acc_points":"0","original_img":"/Public/upload/goods/2016/04-22/5719843a87434.jpg"}]
         */

        @SerializedName("order_id")
        public String orderId;
        @SerializedName("order_sn")
        public String orderSn;
        @SerializedName("order_status")
        public String orderStatus;
        @SerializedName("province")
        public String province;
        @SerializedName("city")
        public String city;
        @SerializedName("district")
        public String district;
        @SerializedName("address")
        public String address;
        @SerializedName("pay_status")
        public String payStatus;
        @SerializedName("shipping_status")
        public String shippingStatus;
        @SerializedName("user_money")
        public String userMoney;
        @SerializedName("total_amount")
        public String totalAmount;
        @SerializedName("order_amount")
        public String orderAmount;
        @SerializedName("consignee")
        public String consignee;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("add_time")
        public String addTime;
        @SerializedName("shipping_time")
        public String shippingTime;
        @SerializedName("confirm_time")
        public String confirmTime;
        @SerializedName("pay_time")
        public String payTime;
        @SerializedName("shipping_code")
        public String shippingCode;
        @SerializedName("shipping_name")
        public String shippingName;
        @SerializedName("shipping_price")
        public String shippingPrice;
        @SerializedName("goods_price")
        public String goodsPrice;
        @SerializedName("order_status_code")
        public String orderStatusCode;
        @SerializedName("order_status_desc")
        public String orderStatusDesc;
        @SerializedName("pay_btn")
        public int payBtn;
        @SerializedName("cancel_btn")
        public int cancelBtn;
        @SerializedName("receive_btn")
        public int receiveBtn;
        @SerializedName("comment_btn")
        public int commentBtn;
        @SerializedName("shipping_btn")
        public int shippingBtn;
        @SerializedName("return_btn")
        public int returnBtn;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("invoice_no")
        public String invoiceNo;
        @SerializedName("full_address")
        public String fullAddress;
        @SerializedName("goods_list")
        public ArrayList<GoodsListBean> goodsList;

        public static class GoodsListBean implements Serializable{
            /**
             * rec_id : 338
             * order_id : 307
             * goods_id : 140
             * goods_name : Apple iPhone 6s 16GB 玫瑰金色 移动联通电信4G手机
             * goods_sn : TP0000140
             * goods_num : 1
             * market_price : 4958.00
             * goods_price : 4858.00
             * cost_price : 0.00
             * member_goods_price : 4858.00
             * give_integral : 0
             * spec_key :
             * spec_key_name :
             * bar_code :
             * is_comment : 0
             * prom_type : 0
             * prom_id : 0
             * is_send : 0
             * delivery_id : 0
             * sku :
             * farmer_goods : 0
             * activity_red : null
             * rebate_amount : null
             * acc_points : 0
             * original_img : /Public/upload/goods/2016/04-22/5719843a87434.jpg
             */

            @SerializedName("rec_id")
            public String recId;
            @SerializedName("order_id")
            public String orderId;
            @SerializedName("goods_id")
            public String goodsId;
            @SerializedName("goods_name")
            public String goodsName;
            @SerializedName("goods_sn")
            public String goodsSn;
            @SerializedName("goods_num")
            public String goodsNum;
            @SerializedName("market_price")
            public String marketPrice;
            @SerializedName("goods_price")
            public String goodsPrice;
            @SerializedName("cost_price")
            public String costPrice;
            @SerializedName("member_goods_price")
            public String memberGoodsPrice;
            @SerializedName("give_integral")
            public String giveIntegral;
            @SerializedName("spec_key")
            public String specKey;
            @SerializedName("spec_key_name")
            public String specKeyName;
            @SerializedName("bar_code")
            public String barCode;
            @SerializedName("is_comment")
            public String isComment;
            @SerializedName("prom_type")
            public String promType;
            @SerializedName("prom_id")
            public String promId;
            @SerializedName("is_send")
            public String isSend;
            @SerializedName("delivery_id")
            public String deliveryId;
            @SerializedName("sku")
            public String sku;
            @SerializedName("farmer_goods")
            public String farmerGoods;
            @SerializedName("activity_red")
            public Object activityRed;
            @SerializedName("rebate_amount")
            public Object rebateAmount;
            @SerializedName("acc_points")
            public String accPoints;
            @SerializedName("original_img")
            public String originalImg;
        }
    }
}
