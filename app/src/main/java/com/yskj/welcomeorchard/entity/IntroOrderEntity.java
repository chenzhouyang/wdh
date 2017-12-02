package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/3/21.
 */

public class IntroOrderEntity {

    /**
     * code : 0
     * message : 成功
     * data : [{"goodName":"套餐:套餐一","amount":2900,"status":2,"createTime":"2017-03-23 14:07:47","orderNo":"LGO_2017032314074747818","remark":"不通过","id":5},{"goodName":"套餐:套餐一","amount":2900,"status":2,"createTime":"2017-03-23 09:40:41","orderNo":"LGO_2017032309404083875","remark":"不通过","id":4}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<DataBean> data;

    public static class DataBean {
        /**
         * goodName : 套餐:套餐一
         * amount : 2900.0
         * status : 2
         * createTime : 2017-03-23 14:07:47
         * orderNo : LGO_2017032314074747818
         * remark : 不通过
         * id : 5
         */

        @SerializedName("goodName")
        public String goodName;
        @SerializedName("amount")
        public double amount;
        @SerializedName("status")
        public int status;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("orderNo")
        public String orderNo;
        @SerializedName("remark")
        public String remark;
        @SerializedName("id")
        public int id;
    }
}
