package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fengyongge on 2016/7/6 0006.
 */
public class GoodsBean {


    /**
     * code : 0
     * message : 成功
     * data : {"localLifes":[{"lifeId":1,"lifeName":"卖个球","lifeCover":"582e56a1f676fd69b5657ddd","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":0,"saleCount":134,"cloudIntPercent":10,"cloudOffset":1.5,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":60,"lifeName":"这是套餐名称","lifeCover":"58479c92f676fd2ae34b934c","lifeProfile":"套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明","teamBuyPrice":1000,"rebackRed":0,"saleCount":8,"cloudIntPercent":10,"cloudOffset":100,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":40,"lifeName":"花样寿司","lifeCover":"582fb4aff676fd890450935e","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":0,"saleCount":3,"cloudIntPercent":10,"cloudOffset":1.5,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":70,"lifeName":"武汉热干面","lifeCover":"584b6a47d232d62cacd74b52","lifeProfile":"ASDF","teamBuyPrice":22,"rebackRed":0,"saleCount":2,"cloudIntPercent":10,"cloudOffset":2.2,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":86,"lifeName":"测试123","lifeCover":"589a9dcb24b4851d80915426","lifeProfile":"简介哈哈哈","teamBuyPrice":100,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":10,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":85,"lifeName":"测试哈哈哈","lifeCover":"589a9dcb24b4851d80915426","lifeProfile":"简介哈哈哈","teamBuyPrice":100,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":10,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":67,"lifeName":"呃呃呃","lifeCover":"58491d14f676fd0fd76c4699","lifeProfile":"1111","teamBuyPrice":22,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":2.2,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":65,"lifeName":"都刚刚好","lifeCover":"58491207f676fd0fd76c4677","lifeProfile":"史上最牛逼的商铺meal_describe=法国 v 比较可靠","teamBuyPrice":12,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":1.2,"goodStype":1,"count":-1,"frozenCount":0,"isHot":true}],"count":8,"offset":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;


    public static class DataBean {
        /**
         * localLifes : [{"lifeId":1,"lifeName":"卖个球","lifeCover":"582e56a1f676fd69b5657ddd","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":0,"saleCount":134,"cloudIntPercent":10,"cloudOffset":1.5,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":60,"lifeName":"这是套餐名称","lifeCover":"58479c92f676fd2ae34b934c","lifeProfile":"套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明","teamBuyPrice":1000,"rebackRed":0,"saleCount":8,"cloudIntPercent":10,"cloudOffset":100,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":40,"lifeName":"花样寿司","lifeCover":"582fb4aff676fd890450935e","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":0,"saleCount":3,"cloudIntPercent":10,"cloudOffset":1.5,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":70,"lifeName":"武汉热干面","lifeCover":"584b6a47d232d62cacd74b52","lifeProfile":"ASDF","teamBuyPrice":22,"rebackRed":0,"saleCount":2,"cloudIntPercent":10,"cloudOffset":2.2,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":86,"lifeName":"测试123","lifeCover":"589a9dcb24b4851d80915426","lifeProfile":"简介哈哈哈","teamBuyPrice":100,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":10,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":85,"lifeName":"测试哈哈哈","lifeCover":"589a9dcb24b4851d80915426","lifeProfile":"简介哈哈哈","teamBuyPrice":100,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":10,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":67,"lifeName":"呃呃呃","lifeCover":"58491d14f676fd0fd76c4699","lifeProfile":"1111","teamBuyPrice":22,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":2.2,"goodStype":1,"count":-1,"frozenCount":0,"isHot":false},{"lifeId":65,"lifeName":"都刚刚好","lifeCover":"58491207f676fd0fd76c4677","lifeProfile":"史上最牛逼的商铺meal_describe=法国 v 比较可靠","teamBuyPrice":12,"rebackRed":0,"saleCount":0,"cloudIntPercent":10,"cloudOffset":1.2,"goodStype":1,"count":-1,"frozenCount":0,"isHot":true}]
         * count : 8
         * offset : 0
         */

        @SerializedName("count")
        public int count;
        @SerializedName("offset")
        public int offset;
        @SerializedName("localLifes")
        public List<LocalLifesBean> localLifes;

        public static class LocalLifesBean {

            public boolean isHot;
            public int num; // required
            /**
             * lifeId : 23
             * lifeName : 桌球
             * lifeCover : 58bd2ac1a16fe100010aee03
             * lifeProfile :
             * teamBuyPrice : 18.22
             * rebackRed : 0
             * saleCount : 19
             * cloudIntPercent : 0
             * cloudOffset : 0
             * goodStype : 1
             * count : -1
             * frozenCount : 0
             */

            @SerializedName("lifeId")
            public int lifeId;
            @SerializedName("lifeName")
            public String lifeName;
            @SerializedName("lifeCover")
            public String lifeCover;
            @SerializedName("lifeProfile")
            public String lifeProfile;
            @SerializedName("teamBuyPrice")
            public double teamBuyPrice;
            @SerializedName("rebackRed")
            public int rebackRed;
            @SerializedName("saleCount")
            public int saleCount;
            @SerializedName("cloudIntPercent")
            public int cloudIntPercent;
            @SerializedName("cloudOffset")
            public double cloudOffset;
            @SerializedName("goodStype")
            public int goodStype;
            @SerializedName("count")
            public int count;
            @SerializedName("frozenCount")
            public int frozenCount;

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

        }
    }
}
