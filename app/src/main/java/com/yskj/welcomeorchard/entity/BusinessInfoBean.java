package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class BusinessInfoBean implements Serializable {


    /**
     * code : 0
     * message : 成功
     * data : {"name":"姜贺","shopName":"测试","areaId":6521,"areaArr":[5827,6476,6515,6521],"areaName":"连山关镇","categoryId":14,"categoryName":"美食-小吃","areaString":"辽宁省本溪市本溪满族自治县连山关镇地方撒","mobile":"15936365921","cover":"58baa35f9555dd0001ba261c","coverUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58baa35f9555dd0001ba261c","idCardFront":"","idCardFrontUrl":"","idCardBack":"","idCardBackUrl":"","licence":"58baa3659555dd0001ba261e","licenceUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58baa3659555dd0001ba261e","latitude":0,"longitude":0,"detailAddress":"地方撒","approvalOpinion":"城市没有开通此项服务!","createTime":"2017-03-04 19:22:34","updateTime":"2017-06-01 10:35:29","industry":"","profile":"打发士大夫","vipLevel":0,"level":1,"status":2,"totalSendRed":0,"checkStatus":0,"totalProfit":0,"fundAccount":0,"machineStatus":-1}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * name : 姜贺
         * shopName : 测试
         * areaId : 6521
         * areaArr : [5827,6476,6515,6521]
         * areaName : 连山关镇
         * categoryId : 14
         * categoryName : 美食-小吃
         * areaString : 辽宁省本溪市本溪满族自治县连山关镇地方撒
         * mobile : 15936365921
         * cover : 58baa35f9555dd0001ba261c
         * coverUrl : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58baa35f9555dd0001ba261c
         * idCardFront :
         * idCardFrontUrl :
         * idCardBack :
         * idCardBackUrl :
         * licence : 58baa3659555dd0001ba261e
         * licenceUrl : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58baa3659555dd0001ba261e
         * latitude : 0
         * longitude : 0
         * detailAddress : 地方撒
         * approvalOpinion : 城市没有开通此项服务!
         * createTime : 2017-03-04 19:22:34
         * updateTime : 2017-06-01 10:35:29
         * industry :
         * profile : 打发士大夫
         * vipLevel : 0
         * level : 1
         * status : 2
         * totalSendRed : 0
         * checkStatus : 0
         * totalProfit : 0
         * fundAccount : 0
         * machineStatus : -1
         */

        @SerializedName("name")
        public String name;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("areaId")
        public int areaId;
        @SerializedName("areaName")
        public String areaName;
        @SerializedName("categoryId")
        public int categoryId;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("areaString")
        public String areaString;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("cover")
        public String cover;
        @SerializedName("coverUrl")
        public String coverUrl;
        @SerializedName("idCardFront")
        public String idCardFront;
        @SerializedName("idCardFrontUrl")
        public String idCardFrontUrl;
        @SerializedName("idCardBack")
        public String idCardBack;
        @SerializedName("idCardBackUrl")
        public String idCardBackUrl;
        @SerializedName("licence")
        public String licence;
        @SerializedName("licenceUrl")
        public String licenceUrl;
        @SerializedName("latitude")
        public int latitude;
        @SerializedName("longitude")
        public int longitude;
        @SerializedName("detailAddress")
        public String detailAddress;
        @SerializedName("approvalOpinion")
        public String approvalOpinion;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("updateTime")
        public String updateTime;
        @SerializedName("industry")
        public String industry;
        @SerializedName("profile")
        public String profile;
        @SerializedName("vipLevel")
        public int vipLevel;
        @SerializedName("level")
        public int level;
        @SerializedName("status")
        public int status;
        @SerializedName("totalSendRed")
        public int totalSendRed;
        @SerializedName("checkStatus")
        public int checkStatus;
        @SerializedName("totalProfit")
        public int totalProfit;
        @SerializedName("fundAccount")
        public int fundAccount;
        @SerializedName("machineStatus")
        public int machineStatus;
        @SerializedName("areaArr")
        public List<Integer> areaArr;
    }
}
