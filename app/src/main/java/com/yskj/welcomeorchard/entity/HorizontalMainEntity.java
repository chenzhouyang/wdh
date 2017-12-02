package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class HorizontalMainEntity extends BaseEntity {
    @SerializedName("ret_data")
    public ArrayList<HorizontalEntity> list;

    public static class HorizontalEntity {
        @SerializedName("goods_id")
        public String goods_id;
        @SerializedName("goods_sn")
        public String goods_sn;
        @SerializedName("goods_name")
        public String goods_name;
        @SerializedName("shop_price")
        public String shop_price;
        @SerializedName("original_img")
        public String original_img;
    }
}
