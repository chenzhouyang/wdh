package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/12on 15:43.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AllFragmentListRecommendEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * cat_id :
     * root_cat_id :
     * page : {"firstRow":0,"listRows":10,"parameter":{"sk":"2ab99a6af22365686e97992df974d5c2","is_recommend":"1"},"totalRows":107,"totalPages":11,"rollPage":11,"lastSuffix":true,"p":"p","url":"","nowPage":1}
     * show_type : 0
     * goods_list : [{"goods_id":"154","cat_id":"940","goods_name":"我就是测试M","shop_price":"10000.00","market_price":"20000.00","original_img":"/Public/upload/goods/2017/04-05/58e493910d5b1.png","original_fu_img":"/Public/upload/goods/2017/04-05/58e4938566779.png","goods_freight":"0.00","farmer_goods":"0","give_integral":"10000","goods_sn":"YS0000154","sales_sum":"677"},{"goods_id":"153","cat_id":"56","goods_name":"测试商品狂买我","shop_price":"10000.00","market_price":"20000.00","original_img":"/Public/upload/goods/2017/02-16/58a554d1303e8.jpg","original_fu_img":"","goods_freight":"0.00","farmer_goods":"0","give_integral":"10000","goods_sn":"TP0000021","sales_sum":"349"},{"goods_id":"152","cat_id":"154","goods_name":"ceshi","shop_price":"0.01","market_price":"799.00","original_img":"/Public/upload/goods/2017/03-29/58db624805914.png","original_fu_img":"","goods_freight":"10.00","farmer_goods":"1","give_integral":"0","goods_sn":"ys1212312","sales_sum":"8"},{"goods_id":"151","cat_id":"123","goods_name":"dsfa","shop_price":"122.00","market_price":"111.00","original_img":"/Public/upload/goods/2017/01-20/5881c04fcdbd7.jpg","original_fu_img":"","goods_freight":"10.00","farmer_goods":"0","give_integral":"20","goods_sn":"esdds","sales_sum":"15"},{"goods_id":"148","cat_id":"2","goods_name":"ceshi","shop_price":"80.00","market_price":"90.00","original_img":"/Public/upload/goods/2017/03-29/58db6a5702070.jpg","original_fu_img":"","goods_freight":"10.00","farmer_goods":"0","give_integral":"10","goods_sn":"TP0000148","sales_sum":"1"},{"goods_id":"143","cat_id":"20","goods_name":"haier海尔 BC-93TMPF 93升单门冰箱","shop_price":"1000.00","market_price":"799.00","original_img":"/Public/upload/goods/2016/11-01/581854b3df6fd.jpg","original_fu_img":"","goods_freight":"0.00","farmer_goods":"2","give_integral":"88","goods_sn":"TP0000143","sales_sum":"268"},{"goods_id":"142","cat_id":"20","goods_name":"海尔（Haier）BCD-251WDGW 251升 无霜两门冰箱（白色）","shop_price":"0.01","market_price":"2799.00","original_img":"/Public/upload/goods/2016/04-22/57199141d9c05.jpg","original_fu_img":"","goods_freight":"0.00","farmer_goods":"1","give_integral":"15","goods_sn":"TP0000142","sales_sum":"445"},{"goods_id":"141","cat_id":"123","goods_name":"三星 Galaxy A9高配版 (A9100) 精灵黑 全网通4G手机 双卡双待","shop_price":"3499.00","market_price":"3599.00","original_img":"/Public/upload/goods/2017/01-20/5881c0870dba3.jpg","original_fu_img":"","goods_freight":"0.00","farmer_goods":"2","give_integral":"12","goods_sn":"TP0000141","sales_sum":"417"},{"goods_id":"140","cat_id":"123","goods_name":"Apple iPhone 6s 16GB 玫瑰金色 移动联通电信4G手机","shop_price":"4858.00","market_price":"4958.00","original_img":"/Public/upload/goods/2017/01-20/5881c09cb89ca.jpg","original_fu_img":"","goods_freight":"8.00","farmer_goods":"1","give_integral":"0","goods_sn":"TP0000140","sales_sum":"50"},{"goods_id":"139","cat_id":"108","goods_name":"天翼小白49元纯流量卡（随机选号 只发辽宁）60元含100元资费","shop_price":"60.00","market_price":"160.00","original_img":"/Public/upload/goods/2016/04-21/57188c82d62a9.jpg","original_fu_img":"","goods_freight":"0.00","farmer_goods":"2","give_integral":"0","goods_sn":"TP0000139","sales_sum":"48"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("cat_id")
    public String catId;
    @SerializedName("root_cat_id")
    public String rootCatId;
    @SerializedName("page")
    public PageBean page;
    @SerializedName("show_type")
    public int showType;
    @SerializedName("goods_list")
    public ArrayList<GoodsListBean> goodsList;

    public static class PageBean {
        /**
         * firstRow : 0
         * listRows : 10
         * parameter : {"sk":"2ab99a6af22365686e97992df974d5c2","is_recommend":"1"}
         * totalRows : 107
         * totalPages : 11
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
             * is_recommend : 1
             */

            @SerializedName("sk")
            public String sk;
            @SerializedName("is_recommend")
            public String isRecommend;
        }
    }

    public static class GoodsListBean {
        /**
         * goods_id : 154
         * cat_id : 940
         * goods_name : 我就是测试M
         * shop_price : 10000.00
         * market_price : 20000.00
         * original_img : /Public/upload/goods/2017/04-05/58e493910d5b1.png
         * original_fu_img : /Public/upload/goods/2017/04-05/58e4938566779.png
         * goods_freight : 0.00
         * farmer_goods : 0
         * give_integral : 10000
         * goods_sn : YS0000154
         * sales_sum : 677
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
