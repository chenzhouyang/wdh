package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 作者：陈宙洋
 * 日期：2017/8/9.
 * 描述：商家信息实体
 */

public class MeacherEntitiy {
    /**
     * code : 0
     * message : 成功
     * data : {"localShop":{"shopId":1,"shopName":"程序员之家","mobile":"18864659628","profile":"","detailAddress":"芙蓉大道电子商务中心","areaString":"河南省许昌市许昌县芙蓉大道电子商务中心","cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba3c2a9555dd0001ba2524","distance":""}}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * localShop : {"shopId":1,"shopName":"程序员之家","mobile":"18864659628","profile":"","detailAddress":"芙蓉大道电子商务中心","areaString":"河南省许昌市许昌县芙蓉大道电子商务中心","cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba3c2a9555dd0001ba2524","distance":""}
         */

        @SerializedName("localShop")
        public LocalShopBean localShop;

        public static class LocalShopBean {
            /**
             * shopId : 1
             * shopName : 程序员之家
             * mobile : 18864659628
             * profile :
             * detailAddress : 芙蓉大道电子商务中心
             * areaString : 河南省许昌市许昌县芙蓉大道电子商务中心
             * cover : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba3c2a9555dd0001ba2524
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
    }
}
