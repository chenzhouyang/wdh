package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/15.
 */

public class GoodsListEntity extends BaseEntity {
    /**
     * error_code : 000
     * cat_id : 3
     * root_cat_id : 3
     * page : {"firstRow":0,"listRows":6,"parameter":{"sk":"2ab99a6af22365686e97992df974d5c2","id":"3","p":"1"},"totalRows":7,"totalPages":2,"rollPage":11,"lastSuffix":true,"p":"p","url":"","nowPage":1}
     * goods_list : [{"goods_id":"11","cat_id":"26","goods_name":"韩国changsin可拖拉健康收纳盒","shop_price":"88.00","market_price":"96.00","original_img":"/Public/upload/goods/2017/02-17/58a674be96113.jpg","goods_freight":"0.00","farmer_goods":"0","give_integral":"25"},{"goods_id":"10","cat_id":"26","goods_name":"韩国changsin健康调料盒3个装","shop_price":"98.00","market_price":"128.00","original_img":"/Public/upload/goods/2017/02-17/58a6751a4831d.jpg","goods_freight":"0.00","farmer_goods":"0","give_integral":"20"},{"goods_id":"9","cat_id":"26","goods_name":"韩国changsin健康可拆卸筷子笼","shop_price":"58.00","market_price":"88.00","original_img":"/Public/upload/goods/2017/02-17/58a6743591670.jpg","goods_freight":"0.00","farmer_goods":"0","give_integral":"20"},{"goods_id":"8","cat_id":"26","goods_name":"韩国changsin健康任性排水厨房沥水架","shop_price":"148.00","market_price":"151.00","original_img":"/Public/upload/goods/2017/02-17/58a68c41aedb9.jpg","goods_freight":"0.00","farmer_goods":"0","give_integral":"35"},{"goods_id":"7","cat_id":"26","goods_name":"韩国Changsin可拖拉健康置物架夹缝架","shop_price":"158.00","market_price":"169.00","original_img":"/Public/upload/goods/2017/02-17/58a6894de7af9.png","goods_freight":"0.00","farmer_goods":"0","give_integral":"50"},{"goods_id":"6","cat_id":"26","goods_name":"韩国changsin滑动健康米桶中号15kg/大号25kg","shop_price":"128.00","market_price":"135.00","original_img":"/Public/upload/goods/2017/02-17/58a6723197aef.jpg","goods_freight":"0.00","farmer_goods":"0","give_integral":"20"}]
     * filter_param : {"id":"3"}
     */
    @SerializedName("cat_id")
    public String catId;
    @SerializedName("root_cat_id")
    public String rootCatId;
    @SerializedName("page")
    public PageBean page;
    @SerializedName("goods_list")
    public ArrayList<GoodsListBean> goodsList;

    public static class PageBean {
        /**
         * firstRow : 0
         * listRows : 6
         * parameter : {"sk":"2ab99a6af22365686e97992df974d5c2","id":"3","p":"1"}
         * totalRows : 7
         * totalPages : 2
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
        public int totalRows;
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
             * sk : 2ab99a6af22365686e97992df974d5c2
             * id : 3
             * p : 1
             */

            @SerializedName("sk")
            public String sk;
            @SerializedName("id")
            public String id;
            @SerializedName("p")
            public String p;
        }
    }


    public static class GoodsListBean {
        /**
         * goods_id : 11
         * cat_id : 26
         * goods_name : 韩国changsin可拖拉健康收纳盒
         * shop_price : 88.00
         * market_price : 96.00
         * original_img : /Public/upload/goods/2017/02-17/58a674be96113.jpg
         * goods_freight : 0.00
         * farmer_goods : 0
         * give_integral : 25
         * "goods_sn": "ys1212312"
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("cat_id")
        public String catId;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("shop_price")
        public String shopPrice;
        @SerializedName("market_price")
        public String marketPrice;
        @SerializedName("original_img")
        public String originalImg;
        @SerializedName("original_fu_img")
        public String originalFuImg;
        @SerializedName("goods_freight")
        public String goodsFreight;
        @SerializedName("farmer_goods")
        public String farmerGoods;
        @SerializedName("give_integral")
        public String giveIntegral;
        @SerializedName("goods_sn")
        public String goodsSn;
        @SerializedName("sales_sum")
        public String salesSum;
    }
}
