package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by YSKJ-02 on 2017/4/13.
 */

public class CommodityEntity {
    @SerializedName("error_code")
    public String error_code;
    @SerializedName("filter_spec")
    public Map<String, ArrayList<StandsrdBean>> standardlist;
    @SerializedName("spec_goods_price")
    public Map<String, NormsBean> normsBeanMap;
    @SerializedName("goods_images_list")
    public ArrayList<GoogsimageBean> googsimageBean;
    @SerializedName("filter_img")
    public String filterimage;
    @SerializedName("flash_sale_price")
    public String flash_sale_price;
    @SerializedName("goods")
    public GoodsBean goodsBean;
    @SerializedName("goods_attr_list")
    public ArrayList<GoodsAttrBean> goodsAttrBean;
    @SerializedName("purchase_notes")
    public String purchasnotes;
    @SerializedName("rand_goods_list")
    public ArrayList<RandGoodsBean> randGoodsBean;
    public static class StandsrdBean {
        /**
         * item_id : 11
         * item : 4G
         * src : null
         * spec_order : 5
         */
        @SerializedName("item_id")
        public String itemId;
        @SerializedName("item")
        public String item;
        @SerializedName("src")
        public String src;
        @SerializedName("spec_order")
        public String specOrder;
    }

    public static class NormsBean {
        /**
         * key : 11_13_55
         * key_name : 网络:4G 内存:16G 颜色:黑色
         * price : 4858.00
         * store_count : 95
         * give_integral : 65
         * description : null
         */

        @SerializedName("key")
        public String key;
        @SerializedName("key_name")
        public String keyName;
        @SerializedName("price")
        public String price;
        @SerializedName("store_count")
        public String storeCount;
        @SerializedName("give_integral")
        public String giveIntegral;
        @SerializedName("description")
        public Object description;
    }
public static class GoogsimageBean{

    /**
     * img_id : 522
     * goods_id : 140
     * image_url : /Public/upload/goods/thumb/140/goods_sub_thumb_522_400_400.jpeg
     */

    @SerializedName("img_id")
    public String imgId;
    @SerializedName("goods_id")
    public String goodsId;
    @SerializedName("image_url")
    public String imageUrl;
}
    public static class GoodsBean{

        /**
         * goods_id : 140
         * cat_id : 123
         * goods_sn : TP0000140
         * goods_name : Apple iPhone 6s 16GB 玫瑰金色 移动联通电信4G手机
         * click_count : 294
         * store_count : 551
         * market_price : 4958.00
         * shop_price : 4858.00
         * goods_remark : Apple iPhone 6s 16GB 玫瑰金色 移动联通电Apple iPhone 6s 16GB 玫瑰金色 移动联通电Apple iPhone 6s 16GB 玫瑰金色 移动联通电Apple iPhone 6s 16GB 玫瑰金色 移动联通电Apple iPhone 6s 16GB 玫瑰金色 移动联通电
         * original_img : /Public/upload/goods/2017/01-20/5881c09cb89ca.jpg
         * goods_tag : 2
         * prom_type : 0
         * give_integral : 65
         */

        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("cat_id")
        public String catId;
        @SerializedName("goods_sn")
        public String goodsSn;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("click_count")
        public String clickCount;
        @SerializedName("store_count")
        public String storeCount;
        @SerializedName("market_price")
        public String marketPrice;
        @SerializedName("shop_price")
        public String shopPrice;
        @SerializedName("goods_remark")
        public String goodsRemark;
        @SerializedName("original_img")
        public String originalImg;
        @SerializedName("goods_tag")
        public String goodsTag;
        @SerializedName("prom_type")
        public String promType;
        @SerializedName("give_integral")
        public String giveIntegral;
        @SerializedName("rest_time_toEnd")
        public String rest_time_toEnd;
        @SerializedName("sales_sum")
        public String sales_sum;
    }

    public static class GoodsAttrBean {
        /**
         * attr_id : 56
         * attr_value : 移动4G(TD-LTE),联通4G(TD-LTE),电信4G(TD-LTE),联通4G（FDD-LTE）,电信4G（FDD-LTE）
         * attr_name : 网络制式
         */

        @SerializedName("attr_id")
        public String attrId;
        @SerializedName("attr_value")
        public String attrValue;
        @SerializedName("attr_name")
        public String attrName;
    }

    public static class RandGoodsBean {

        /**
         * goods_id : 48
         * cat_id : 123
         * goods_name : 荣耀7 双卡双待双通 移动4G版 16GB存储（冰河银）豪华套装一
         * shop_price : 2099.00
         * market_price : 2199.00
         * original_img : /Public/upload/goods/thumb/48/goods_thumb_48_400_200.jpeg
         * original_fu_img : /Public/upload/goods/thumb/48/goods_thumb_48_200_200.jpeg
         * goods_freight : 0.00
         * farmer_goods : 0
         * give_integral : 0
         * goods_sn : TP0000048
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
    }
}
