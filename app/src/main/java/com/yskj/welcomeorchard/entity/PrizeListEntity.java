package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/17on 14:28.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class PrizeListEntity {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * list : [{"id":"6","name":"凯卓纯净之水男用香水 30ml","code":"/Public/upload/goods/2016/01-21/56a08bf378af8.jpg","sort":"0","points":"2"},{"id":"5","name":"法国 Blanc des Vosges 缎纹棉缝纫粉色床单","code":"/Public/upload/big_wheel/2017/05-16/591ac3572cdf8.jpeg","sort":"1","points":"2"},{"id":"4","name":"送15积分","code":"/Public/upload/big_wheel/2017/05-16/591ac2e86fc48.png","sort":"2","points":"2"},{"id":"3","name":"送10积分","code":"/Public/upload/big_wheel/2017/05-16/591ac2dbf2df0.png","sort":"3","points":"2"},{"id":"2","name":"送5积分","code":"/Public/upload/big_wheel/2017/05-16/591ac2d032000.jpg","sort":"4","points":"2"},{"id":"1","name":"谢谢惠顾","code":"/Public/upload/big_wheel/2017/05-16/591ac28633b58.png","sort":"5","points":"2"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("list")
    public ArrayList<ListBean> list;

    public static class ListBean {
        /**
         * id : 6
         * name : 凯卓纯净之水男用香水 30ml
         * code : /Public/upload/goods/2016/01-21/56a08bf378af8.jpg
         * sort : 0
         * points : 2
         */

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("code")
        public String code;
        @SerializedName("sort")
        public String sort;
        @SerializedName("points")
        public String points;
    }
}
