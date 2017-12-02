package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class CellsEntity {

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("filter_spec")
    public Map<String,ArrayList<PrckBean>>  prcklist;
    @SerializedName("spec_goods_price")
    public Map<String,SpecgoodsBean> specgoodsBeanMap;
    @SerializedName("goods_images_list")
    public ArrayList<ImageGoodBean> imageGoodBeen;
    @SerializedName("goods")
    public GoodsBean goodsBean;


    public ArrayList<PrckBean> getPrcklistX() {
        ArrayList<PrckBean> ret = new ArrayList<>();
        if(prcklist == null || prcklist.isEmpty()){
           return ret;
        }
        for (String key : prcklist.keySet()) {
            return prcklist.get(key);
        }
        return ret;
    }

    public static class PrckBean {
        /**
         * item_id : 148
         * item : 套餐一
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
    public static class SpecgoodsBean{

        /**
         * key : 148
         * key_name : 套餐:套餐一
         * price : 2900.00
         * store_count : 85
         * give_integral : 100
         * description : 1、啊u几点噶USD噶啥
         2、阿萨帝哦哈USD哈撒
         3、阿萨帝哈欧斯达死哦的
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
        public String description;
    }

    public static class ImageGoodBean {

        /**
         * img_id : 744
         * goods_id : 155
         * image_url : /Public/upload/goods/2017/03-23/58d37993520eb.png
         */

        @SerializedName("img_id")
        public String imgId;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("image_url")
        public String imageUrl;
    }

    public static class GoodsBean {

        /**
         * goods_id : 155
         * cat_id : 5
         * goods_sn : YS0000155
         * goods_name : 细胞疗法
         * click_count : 6
         * store_count : 380
         * market_price : 35000.00
         * shop_price : 29000.00
         * goods_remark : 细胞疗法
         * original_img : /Public/upload/goods/2017/03-23/58d379841c77f.png
         * goods_tag : 1|3
         * prom_type : 0
         * rest_time_toEnd :
         * give_integral : 100
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
        @SerializedName("rest_time_toEnd")
        public String restTimeToEnd;
        @SerializedName("give_integral")
        public String giveIntegral;
    }
}
