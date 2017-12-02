package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class LocalServerMainListBean implements Serializable{

    /**
     * code : 0
     * message : 成功
     * data : {"localLifes":[{"shopId":25,"shopName":"程序员之家","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=getFile?fileId=582ed61bf676fd8056d8d1bb","latitude":34.107834,"longitude":113.84187,"distanceString":"5.56km","shopAreaString":"河南省许昌市许昌县尚集镇芙蓉大道电子商务产业园2号楼","lifeId":33,"lifeName":"桌球","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582ed8a1f676fd8056d8d1bf","teamBuyPrice":50,"rebackRed":0,"saleCount":55,"label":"","cloudIntPercent":10,"cloudOffset":5,"isHot":false},{"shopId":28,"shopName":"万达广场","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fc06df676fd8904509386","latitude":34.098,"longitude":113.856,"distanceString":"4.31km","shopAreaString":"河南省许昌市许昌县河南省许昌市许昌县尚集电商产业园","lifeId":49,"lifeName":"俎常亮买1送1","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fc833f676fd89045093a3","teamBuyPrice":78,"rebackRed":0,"saleCount":11,"label":"","cloudIntPercent":10,"cloudOffset":7.8,"isHot":false},{"shopId":15,"shopName":"大西瓜专卖","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d7d92f676fd63502d41af","latitude":34.095,"longitude":113.858,"distanceString":"3.98km","shopAreaString":"河南省许昌市许昌县河南省许昌市许昌县电子商务产业园","lifeId":58,"lifeName":"西瓜","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5832a123f676fd9b7c74450e","teamBuyPrice":1,"rebackRed":0,"saleCount":8,"label":"","cloudIntPercent":10,"cloudOffset":0.1,"isHot":false},{"shopId":3,"shopName":"小帅店铺","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5850d8365ba6041470a3dbd7","latitude":34.033,"longitude":113.877,"distanceString":"3.51km","shopAreaString":"河南省许昌市魏都区新东街与紫云路交叉口森林半岛","lifeId":68,"lifeName":"人人人","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58491eeef676fd0fd76c469b","teamBuyPrice":33,"rebackRed":0,"saleCount":7,"label":"","cloudIntPercent":10,"cloudOffset":3.3,"isHot":false},{"shopId":10,"shopName":"香菜专卖","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d4dc3f676fd57f8a8cd15","latitude":34.049,"longitude":113.835,"distanceString":"2.23km","shopAreaString":"山东省山东省河南省许昌市许昌县河南省许昌市许昌县河南省许昌市许昌县电子商务产业园","lifeId":56,"lifeName":"美你所美","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5832516cf676fd9271064653","teamBuyPrice":559,"rebackRed":0,"saleCount":4,"label":"","cloudIntPercent":10,"cloudOffset":55.9,"isHot":false},{"shopId":31,"shopName":"伊人美甲1","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5830042af676fd9271064645","latitude":34.034,"longitude":113.858,"distanceString":"2.81km","shopAreaString":"河南省许昌市魏都区新田360二楼2018","lifeId":55,"lifeName":"简易美套餐","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58300b5cf676fd9271064649","teamBuyPrice":65,"rebackRed":0,"saleCount":2,"label":"","cloudIntPercent":10,"cloudOffset":6.5,"isHot":false},{"shopId":54,"shopName":"周记砂锅","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=589a8eaa24b4851f78f3db52","latitude":34.034,"longitude":113.876,"distanceString":"3.36km","shopAreaString":"河南省许昌市魏都区新东街与紫云路交叉口","lifeId":88,"lifeName":"砂锅土豆粉","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58a1656bffc98f146481e36b","teamBuyPrice":9,"rebackRed":0,"saleCount":0,"label":"","cloudIntPercent":10,"cloudOffset":0.9,"isHot":true}],"count":7,"offset":0}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean implements Serializable{
        /**
         * localLifes : [{"shopId":25,"shopName":"程序员之家","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=getFile?fileId=582ed61bf676fd8056d8d1bb","latitude":34.107834,"longitude":113.84187,"distanceString":"5.56km","shopAreaString":"河南省许昌市许昌县尚集镇芙蓉大道电子商务产业园2号楼","lifeId":33,"lifeName":"桌球","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582ed8a1f676fd8056d8d1bf","teamBuyPrice":50,"rebackRed":0,"saleCount":55,"label":"","cloudIntPercent":10,"cloudOffset":5,"isHot":false},{"shopId":28,"shopName":"万达广场","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fc06df676fd8904509386","latitude":34.098,"longitude":113.856,"distanceString":"4.31km","shopAreaString":"河南省许昌市许昌县河南省许昌市许昌县尚集电商产业园","lifeId":49,"lifeName":"俎常亮买1送1","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582fc833f676fd89045093a3","teamBuyPrice":78,"rebackRed":0,"saleCount":11,"label":"","cloudIntPercent":10,"cloudOffset":7.8,"isHot":false},{"shopId":15,"shopName":"大西瓜专卖","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d7d92f676fd63502d41af","latitude":34.095,"longitude":113.858,"distanceString":"3.98km","shopAreaString":"河南省许昌市许昌县河南省许昌市许昌县电子商务产业园","lifeId":58,"lifeName":"西瓜","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5832a123f676fd9b7c74450e","teamBuyPrice":1,"rebackRed":0,"saleCount":8,"label":"","cloudIntPercent":10,"cloudOffset":0.1,"isHot":false},{"shopId":3,"shopName":"小帅店铺","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5850d8365ba6041470a3dbd7","latitude":34.033,"longitude":113.877,"distanceString":"3.51km","shopAreaString":"河南省许昌市魏都区新东街与紫云路交叉口森林半岛","lifeId":68,"lifeName":"人人人","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58491eeef676fd0fd76c469b","teamBuyPrice":33,"rebackRed":0,"saleCount":7,"label":"","cloudIntPercent":10,"cloudOffset":3.3,"isHot":false},{"shopId":10,"shopName":"香菜专卖","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582d4dc3f676fd57f8a8cd15","latitude":34.049,"longitude":113.835,"distanceString":"2.23km","shopAreaString":"山东省山东省河南省许昌市许昌县河南省许昌市许昌县河南省许昌市许昌县电子商务产业园","lifeId":56,"lifeName":"美你所美","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5832516cf676fd9271064653","teamBuyPrice":559,"rebackRed":0,"saleCount":4,"label":"","cloudIntPercent":10,"cloudOffset":55.9,"isHot":false},{"shopId":31,"shopName":"伊人美甲1","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=5830042af676fd9271064645","latitude":34.034,"longitude":113.858,"distanceString":"2.81km","shopAreaString":"河南省许昌市魏都区新田360二楼2018","lifeId":55,"lifeName":"简易美套餐","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58300b5cf676fd9271064649","teamBuyPrice":65,"rebackRed":0,"saleCount":2,"label":"","cloudIntPercent":10,"cloudOffset":6.5,"isHot":false},{"shopId":54,"shopName":"周记砂锅","shopCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=589a8eaa24b4851f78f3db52","latitude":34.034,"longitude":113.876,"distanceString":"3.36km","shopAreaString":"河南省许昌市魏都区新东街与紫云路交叉口","lifeId":88,"lifeName":"砂锅土豆粉","lifeCover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=58a1656bffc98f146481e36b","teamBuyPrice":9,"rebackRed":0,"saleCount":0,"label":"","cloudIntPercent":10,"cloudOffset":0.9,"isHot":true}]
         * count : 7
         * offset : 0
         */

        @SerializedName("count")
        public int count;
        @SerializedName("offset")
        public int offset;
        @SerializedName("localLifes")
        public ArrayList<LocalLifesBean> localLifes;
    }
    public static class LocalLifesBean implements Serializable{
        /**
         * shopId : 25
         * shopName : 程序员之家
         * shopCover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=getFile?fileId=582ed61bf676fd8056d8d1bb
         * latitude : 34.107834
         * longitude : 113.84187
         * distanceString : 5.56km
         * shopAreaString : 河南省许昌市许昌县尚集镇芙蓉大道电子商务产业园2号楼
         * lifeId : 33
         * lifeName : 桌球
         * lifeCover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582ed8a1f676fd8056d8d1bf
         * teamBuyPrice : 50.0
         * rebackRed : 0.0
         * saleCount : 55
         * label :
         * cloudIntPercent : 10
         * cloudOffset : 5.0
         * isHot : false
         */

        @SerializedName("shopId")
        public String shopId;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("shopCover")
        public String shopCover;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("distanceString")
        public String distanceString;
        @SerializedName("shopAreaString")
        public String shopAreaString;
        @SerializedName("lifeId")
        public String lifeId;
        @SerializedName("lifeName")
        public String lifeName;
        @SerializedName("lifeCover")
        public String lifeCover;
        @SerializedName("teamBuyPrice")
        public double teamBuyPrice;
        @SerializedName("rebackRed")
        public double rebackRed;
        @SerializedName("saleCount")
        public int saleCount;
        @SerializedName("label")
        public String label;
        @SerializedName("cloudIntPercent")
        public int cloudIntPercent;
        @SerializedName("cloudOffset")
        public double cloudOffset;
        @SerializedName("isHot")
        public boolean isHot;
    }
}
