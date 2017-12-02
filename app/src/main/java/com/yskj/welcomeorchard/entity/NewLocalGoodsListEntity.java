package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/10on 16:42.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class NewLocalGoodsListEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"localShops":[{"shopId":9,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba853a9555dd0001ba2584","shopName":"乔","saleCount":0,"distanceString":"","areaName":"东城区","areaId":3,"address":"北京市东城区芙蓉大道","keyWord":"","hot":false}],"count":1,"offset":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * localShops : [{"shopId":9,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba853a9555dd0001ba2584","shopName":"乔","saleCount":0,"distanceString":"","areaName":"东城区","areaId":3,"address":"北京市东城区芙蓉大道","keyWord":"","hot":false}]
         * count : 1
         * offset : 0
         */

        @SerializedName("count")
        public int count;
        @SerializedName("offset")
        public int offset;
        @SerializedName("localShops")
        public ArrayList<LocalShopsBean> localShops;

        public static class LocalShopsBean {
            /**
             * shopId : 9
             * cover : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ba853a9555dd0001ba2584
             * shopName : 乔
             * saleCount : 0
             * distanceString :
             * areaName : 东城区
             * areaId : 3
             * address : 北京市东城区芙蓉大道
             * keyWord :
             * hot : false
             */

            @SerializedName("shopId")
            public int shopId;
            @SerializedName("cover")
            public String cover;
            @SerializedName("shopName")
            public String shopName;
            @SerializedName("saleCount")
            public int saleCount;
            @SerializedName("distanceString")
            public String distanceString;
            @SerializedName("areaName")
            public String areaName;
            @SerializedName("areaId")
            public int areaId;
            @SerializedName("address")
            public String address;
            @SerializedName("keyWord")
            public String keyWord;
            @SerializedName("hot")
            public boolean hot;
        }
    }
}
