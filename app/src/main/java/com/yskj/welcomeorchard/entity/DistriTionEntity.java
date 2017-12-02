package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者： chen
 * 时间： 2017/9/1
 * 描述：分销商品实体类
 */

public class DistriTionEntity {
    /**
     * code : 0
     * message : 成功
     * data : [{"ugsId":1,"goodsId":1,"goodsName":"好看的运动裤","specId":1,"specName":"颜色：白色  尺寸：xl","specPrice":100,"cover":"C:/Users/July/Pictures/n.jpg","stock":10,"millId":1,"millName":"test"}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * ugsId : 1
         * goodsId : 1
         * goodsName : 好看的运动裤
         * specId : 1
         * specName : 颜色：白色  尺寸：xl
         * specPrice : 100.0
         * cover : C:/Users/July/Pictures/n.jpg
         * stock : 10
         * millId : 1
         * millName : test
         */

        @SerializedName("ugsId")
        public int ugsId;
        @SerializedName("goodsId")
        public int goodsId;
        @SerializedName("goodsName")
        public String goodsName;
        @SerializedName("specId")
        public int specId;
        @SerializedName("specName")
        public String specName;
        @SerializedName("specPrice")
        public double specPrice;
        @SerializedName("cover")
        public String cover;
        @SerializedName("stock")
        public int stock;
        @SerializedName("millId")
        public int millId;
        @SerializedName("millName")
        public String millName;
        private int num = 1;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
