package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：陈宙洋
 * 日期：2017/8/14.
 * 描述：微商货源商品详情实体
 */

public class ParticularsEntitiy {

    /**
     * code : 0
     * message : 成功
     * data : {"goodId":1,"name":"好看的运动裤","barCode":"123456789","price":10,"mAccount":5,"volume":10,"cover":null,"millId":1,"imageList":[{"id":1,"source":"C:/Users/July/Pictures/n.jpg","thumbnail":null,"title":"n.jpg","orders":1,"type":1,"goodsId":1},{"id":2,"source":"C:/Users/July/Pictures/n.jpg","thumbnail":null,"title":"n.jpg","orders":2,"type":1,"goodsId":1},{"id":3,"source":"C:/Users/July/Pictures/201708021743390452555.jpg","thumbnail":null,"title":"201708021743390452555.jpg","orders":3,"type":1,"goodsId":1},{"id":4,"source":"C:/Users/July/Pictures/201708021749214946338.jpg","thumbnail":null,"title":"201708021749214946338.jpg","orders":4,"type":1,"goodsId":1}],"contentList":[{"id":1,"type":1,"content":"ceshishdiaehihae","orders":1,"goodsId":1},{"id":2,"type":1,"content":"dahdhafha","orders":2,"goodsId":1},{"id":3,"type":1,"content":"shemennmemeene","orders":3,"goodsId":1}],"parameterList":[{"id":1,"name":"颜色：白色  尺寸：xl","price":100,"stock":100,"mAccount":10,"orders":1}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * goodId : 1
         * name : 好看的运动裤
         * barCode : 123456789
         * price : 10
         * mAccount : 5
         * volume : 10
         * cover : null
         * millId : 1
         * imageList : [{"id":1,"source":"C:/Users/July/Pictures/n.jpg","thumbnail":null,"title":"n.jpg","orders":1,"type":1,"goodsId":1},{"id":2,"source":"C:/Users/July/Pictures/n.jpg","thumbnail":null,"title":"n.jpg","orders":2,"type":1,"goodsId":1},{"id":3,"source":"C:/Users/July/Pictures/201708021743390452555.jpg","thumbnail":null,"title":"201708021743390452555.jpg","orders":3,"type":1,"goodsId":1},{"id":4,"source":"C:/Users/July/Pictures/201708021749214946338.jpg","thumbnail":null,"title":"201708021749214946338.jpg","orders":4,"type":1,"goodsId":1}]
         * contentList : [{"id":1,"type":1,"content":"ceshishdiaehihae","orders":1,"goodsId":1},{"id":2,"type":1,"content":"dahdhafha","orders":2,"goodsId":1},{"id":3,"type":1,"content":"shemennmemeene","orders":3,"goodsId":1}]
         * parameterList : [{"id":1,"name":"颜色：白色  尺寸：xl","price":100,"stock":100,"mAccount":10,"orders":1}]
         */

        @SerializedName("goodId")
        public int goodId;
        @SerializedName("name")
        public String name;
        @SerializedName("barCode")
        public String barCode;
        @SerializedName("price")
        public double price;
        @SerializedName("mAccount")
        public double mAccount;
        @SerializedName("volume")
        public int volume;
        @SerializedName("cover")
        public Object cover;
        @SerializedName("millId")
        public int millId;
        @SerializedName("imageList")
        public ArrayList<ImageListBean> imageList;
        @SerializedName("contentList")
        public List<ContentListBean> contentList;
        @SerializedName("parameterList")
        public List<ParameterListBean> parameterList;

        public static class ImageListBean {
            /**
             * id : 1
             * source : C:/Users/July/Pictures/n.jpg
             * thumbnail : null
             * title : n.jpg
             * orders : 1
             * type : 1
             * goodsId : 1
             */

            @SerializedName("id")
            public int id;
            @SerializedName("source")
            public String source;
            @SerializedName("thumbnail")
            public Object thumbnail;
            @SerializedName("title")
            public String title;
            @SerializedName("orders")
            public int orders;
            @SerializedName("type")
            public int type;
            @SerializedName("goodsId")
            public int goodsId;
        }

        public static class ContentListBean {
            /**
             * id : 1
             * type : 1
             * content : ceshishdiaehihae
             * orders : 1
             * goodsId : 1
             */

            @SerializedName("id")
            public int id;
            @SerializedName("type")
            public int type;
            @SerializedName("content")
            public String content;
            @SerializedName("orders")
            public int orders;
            @SerializedName("goodsId")
            public int goodsId;
        }

        public static class ParameterListBean {
            /**
             * id : 1
             * name : 颜色：白色  尺寸：xl
             * price : 100
             * stock : 100
             * mAccount : 10
             * orders : 1
             */

            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
            @SerializedName("price")
            public double price;
            @SerializedName("stock")
            public int stock;
            @SerializedName("mAccount")
            public double mAccount;
            @SerializedName("orders")
            public int orders;
            private boolean isGroupSelected = false;//是否选中
            private int num = 1;//数量

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public boolean isGroupSelected() {
                return isGroupSelected;
            }

            public void setGroupSelected(boolean groupSelected) {
                isGroupSelected = groupSelected;
            }
        }
    }
}
