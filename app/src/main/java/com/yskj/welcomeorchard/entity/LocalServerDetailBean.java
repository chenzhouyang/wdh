package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jianghe on 2016/11/10 0010.
 * 团购详情页面返回的实体类
 */
public class LocalServerDetailBean implements Serializable{
    /**
     * code : 0
     * message : 成功
     * data : {"id":60,"name":"这是套餐名称","cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58479c92f676fd2ae34b934c","profile":"套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明","teamBuyPrice":1000,"rebackRed":300,"validityPeriod":12,"consumeStartTime":0,"consumeEndTime":23,"onlineTime":"2017-01-07","offlineTime":"2018-01-07","purchaseNote":"[{\"content\":\"是\",\"key\":1000,\"title\":\"是否支持极速退款\",\"type\":1,\"val\":\"\"},{\"content\":\"是\",\"key\":1001,\"title\":\"是否支持wifi\",\"type\":1,\"val\":\"\"},{\"content\":\"是\",\"key\":1002,\"title\":\"是否展示发票\",\"type\":1,\"val\":\"\"},{\"content\":\"可以同时享有\",\"key\":1003,\"title\":\"同时享有优惠\",\"type\":-1,\"val\":\"\"},{\"content\":\"是\",\"key\":1004,\"title\":\"是否预约\",\"type\":0,\"val\":\"\"},{\"content\":\"否\",\"key\":1005,\"title\":\"是否限制最高销量（接待量）\",\"type\":0,\"val\":\"\"},{\"content\":\"否\",\"key\":1006,\"title\":\"是否限购团购券\",\"type\":0,\"val\":\"\"},{\"content\":\"否\",\"key\":1007,\"title\":\"是否限用团购券\",\"type\":0,\"val\":\"\"}]","status":2,"label":"","saleCount":3,"localShop":{"shopId":1,"shopName":"牛逼的商铺","mobile":"15225700256","profile":"1","detailAddress":"朝阳区","areaString":"北京市朝阳区","cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b7ec74a47d886668e5","distance":""},"localLifeCategory":{"cateId":12,"parentId":1,"name":"火锅","profile":"火锅","fee":0,"sequence":3,"children":[],"isHot":true},"setMealList":[{"id":8,"name":"水煮鱼","cover":"","price":54,"profile":"","count":1}],"otherLifes":[{"lifeId":1,"lifeName":"卖个球","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":3,"saleCount":29,"isHot":false},{"lifeId":70,"lifeName":"武汉热干面","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584b6a47d232d62cacd74b52","lifeProfile":"ASDF","teamBuyPrice":22,"rebackRed":2,"saleCount":2,"isHot":false},{"lifeId":2,"lifeName":"双蛋龙须面","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fc80bf676fd89045093a1","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":8,"rebackRed":3,"saleCount":5,"isHot":false},{"lifeId":21,"lifeName":"4984","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e693af676fd69b5657e1b","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":484,"rebackRed":102,"saleCount":2,"isHot":false},{"lifeId":40,"lifeName":"花样寿司","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fb4aff676fd890450935e","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":2,"saleCount":1,"isHot":false},{"lifeId":41,"lifeName":"啊","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fb4f4f676fd8904509360","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":1,"rebackRed":2,"saleCount":0,"isHot":false},{"lifeId":42,"lifeName":"啊哈哈","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fb6fef676fd8904509366","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":2,"rebackRed":55,"saleCount":0,"isHot":false},{"lifeId":60,"lifeName":"这是套餐名称","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58479c92f676fd2ae34b934c","lifeProfile":"套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明","teamBuyPrice":1000,"rebackRed":300,"saleCount":3,"isHot":false},{"lifeId":65,"lifeName":"都刚刚好","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58491207f676fd0fd76c4677","lifeProfile":"史上最牛逼的商铺meal_describe=法国 v 比较可靠","teamBuyPrice":12,"rebackRed":12,"saleCount":0,"isHot":true},{"lifeId":67,"lifeName":"呃呃呃","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58491d14f676fd0fd76c4699","lifeProfile":"1111","teamBuyPrice":22,"rebackRed":22,"saleCount":0,"isHot":false},{"lifeId":69,"lifeName":"后台测试1","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584a0637d232d638f4493633","lifeProfile":"后台测试1","teamBuyPrice":22,"rebackRed":2,"saleCount":0,"isHot":false},{"lifeId":85,"lifeName":"测试哈哈哈","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=123456789","lifeProfile":"简介哈哈哈","teamBuyPrice":100,"rebackRed":0,"saleCount":0,"isHot":false}],"isHot":false}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * id : 60
         * name : 这是套餐名称
         * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58479c92f676fd2ae34b934c
         * profile : 套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明
         * teamBuyPrice : 1000.0
         * rebackRed : 300.0
         * validityPeriod : 12
         * consumeStartTime : 0
         *  "cloudOffset": 0,
         "cloudIntPercent": 0,
         * consumeEndTime : 23
         * onlineTime : 2017-01-07
         * offlineTime : 2018-01-07
         * purchaseNote : [{"content":"是","key":1000,"title":"是否支持极速退款","type":1,"val":""},{"content":"是","key":1001,"title":"是否支持wifi","type":1,"val":""},{"content":"是","key":1002,"title":"是否展示发票","type":1,"val":""},{"content":"可以同时享有","key":1003,"title":"同时享有优惠","type":-1,"val":""},{"content":"是","key":1004,"title":"是否预约","type":0,"val":""},{"content":"否","key":1005,"title":"是否限制最高销量（接待量）","type":0,"val":""},{"content":"否","key":1006,"title":"是否限购团购券","type":0,"val":""},{"content":"否","key":1007,"title":"是否限用团购券","type":0,"val":""}]
         * status : 2
         * label :
         * saleCount : 3
         * localShop : {"shopId":1,"shopName":"牛逼的商铺","mobile":"15225700256","profile":"1","detailAddress":"朝阳区","areaString":"北京市朝阳区","cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b7ec74a47d886668e5","distance":""}
         * localLifeCategory : {"cateId":12,"parentId":1,"name":"火锅","profile":"火锅","fee":0,"sequence":3,"children":[],"isHot":true}
         * setMealList : [{"id":8,"name":"水煮鱼","cover":"","price":54,"profile":"","count":1}]
         * otherLifes : [{"lifeId":1,"lifeName":"卖个球","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":3,"saleCount":29,"isHot":false},{"lifeId":70,"lifeName":"武汉热干面","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584b6a47d232d62cacd74b52","lifeProfile":"ASDF","teamBuyPrice":22,"rebackRed":2,"saleCount":2,"isHot":false},{"lifeId":2,"lifeName":"双蛋龙须面","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fc80bf676fd89045093a1","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":8,"rebackRed":3,"saleCount":5,"isHot":false},{"lifeId":21,"lifeName":"4984","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e693af676fd69b5657e1b","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":484,"rebackRed":102,"saleCount":2,"isHot":false},{"lifeId":40,"lifeName":"花样寿司","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fb4aff676fd890450935e","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":15,"rebackRed":2,"saleCount":1,"isHot":false},{"lifeId":41,"lifeName":"啊","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fb4f4f676fd8904509360","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":1,"rebackRed":2,"saleCount":0,"isHot":false},{"lifeId":42,"lifeName":"啊哈哈","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fb6fef676fd8904509366","lifeProfile":"史上最牛逼的商铺","teamBuyPrice":2,"rebackRed":55,"saleCount":0,"isHot":false},{"lifeId":60,"lifeName":"这是套餐名称","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58479c92f676fd2ae34b934c","lifeProfile":"套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明套餐说明","teamBuyPrice":1000,"rebackRed":300,"saleCount":3,"isHot":false},{"lifeId":65,"lifeName":"都刚刚好","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58491207f676fd0fd76c4677","lifeProfile":"史上最牛逼的商铺meal_describe=法国 v 比较可靠","teamBuyPrice":12,"rebackRed":12,"saleCount":0,"isHot":true},{"lifeId":67,"lifeName":"呃呃呃","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58491d14f676fd0fd76c4699","lifeProfile":"1111","teamBuyPrice":22,"rebackRed":22,"saleCount":0,"isHot":false},{"lifeId":69,"lifeName":"后台测试1","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584a0637d232d638f4493633","lifeProfile":"后台测试1","teamBuyPrice":22,"rebackRed":2,"saleCount":0,"isHot":false},{"lifeId":85,"lifeName":"测试哈哈哈","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=123456789","lifeProfile":"简介哈哈哈","teamBuyPrice":100,"rebackRed":0,"saleCount":0,"isHot":false}]
         * isHot : false
         */

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("cover")
        public String cover;
        @SerializedName("profile")
        public String profile;
        @SerializedName("teamBuyPrice")
        public double teamBuyPrice;
        @SerializedName("cloudOffset")
        public double cloudOffset;
        @SerializedName("rebackRed")
        public double rebackRed;
        @SerializedName("validityPeriod")
        public int validityPeriod;
        @SerializedName("consumeStartTime")
        public int consumeStartTime;
        @SerializedName("consumeEndTime")
        public int consumeEndTime;
        @SerializedName("onlineTime")
        public String onlineTime;
        @SerializedName("offlineTime")
        public String offlineTime;
        @SerializedName("purchaseNote")
        public String purchaseNote;
        @SerializedName("status")
        public int status;
        @SerializedName("label")
        public String label;
        @SerializedName("saleCount")
        public int saleCount;
        @SerializedName("localShop")
        public LocalShopBean localShop;
        @SerializedName("localLifeCategory")
        public LocalLifeCategoryBean localLifeCategory;
        @SerializedName("isHot")
        public boolean isHot;
        @SerializedName("setMealList")
        public ArrayList<SetMealListBean> setMealList;
        @SerializedName("otherLifes")
        public ArrayList<OtherLifesBean> otherLifes;


    }
    public static class LocalShopBean implements Serializable{
        /**
         * shopId : 1
         * shopName : 牛逼的商铺
         * mobile : 15225700256
         * profile : 1
         * detailAddress : 朝阳区
         * areaString : 北京市朝阳区
         * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b7ec74a47d886668e5
         * distance :
         */

        @SerializedName("shopId")
        public int shopId;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("profile")
        public String profile;
        @SerializedName("detailAddress")
        public String detailAddress;
        @SerializedName("areaString")
        public String areaString;
        @SerializedName("cover")
        public String cover;
        @SerializedName("distance")
        public String distance;
    }

    public static class LocalLifeCategoryBean implements Serializable{
        /**
         * cateId : 12
         * parentId : 1
         * name : 火锅
         * profile : 火锅
         * fee : 0
         * sequence : 3
         * children : []
         * isHot : true
         */

        @SerializedName("cateId")
        public int cateId;
        @SerializedName("parentId")
        public int parentId;
        @SerializedName("name")
        public String name;
        @SerializedName("profile")
        public String profile;
        @SerializedName("fee")
        public int fee;
        @SerializedName("sequence")
        public int sequence;
        @SerializedName("isHot")
        public boolean isHot;
        @SerializedName("children")
        public ArrayList<?> children;
    }

    public static class SetMealListBean implements Serializable{
        /**
         * id : 8
         * name : 水煮鱼
         * cover :
         * price : 54.0
         * profile :
         * count : 1
         */

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("cover")
        public String cover;
        @SerializedName("price")
        public double price;
        @SerializedName("profile")
        public String profile;
        @SerializedName("count")
        public int count;
    }

    public static class OtherLifesBean implements Serializable{
        /**
         * lifeId : 1
         * lifeName : 卖个球
         * lifeCover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd
         * lifeProfile : 史上最牛逼的商铺
         * teamBuyPrice : 15.0
         * rebackRed : 3.0
         * saleCount : 29
         * isHot : false
         * cloudOffset=50000
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
        public double rebackRed;
        @SerializedName("saleCount")
        public int saleCount;
        @SerializedName("isHot")
        public boolean isHot;
        @SerializedName("cloudOffset")
        public double cloudOffset;
    }
}
