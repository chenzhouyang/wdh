package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/4/14on 18:50.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class WDHFragmentTopRecycleEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * goods_category_tree : [{"id":"1","name":"服装箱包","mobile_name":"服装箱包","parent_id":"0","parent_id_path":"0_1","level":"1","sort_order":"43","is_show":"1","image":"/Public/upload/category/2017/04-13/58ef2d061c331.png","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"16","name":"鞋靴","mobile_name":"鞋靴","parent_id":"1","parent_id_path":"0_1_16","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b195b8340f.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"17","name":"运动户外","mobile_name":"运动户外","parent_id":"1","parent_id_path":"0_1_17","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b198413c66.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"}]}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("goods_category_tree")
    public ArrayList<GoodsCategoryTreeBean> goodsCategoryTree;

    public static  class GoodsCategoryTreeBean {
        /**
         * id : 1
         * name : 服装箱包
         * mobile_name : 服装箱包
         * parent_id : 0
         * parent_id_path : 0_1
         * level : 1
         * sort_order : 43
         * is_show : 1
         * image : /Public/upload/category/2017/04-13/58ef2d061c331.png
         * is_hot : 1
         * cat_group : 0
         * commission_rate : 0
         * tmenu : [{"id":"16","name":"鞋靴","mobile_name":"鞋靴","parent_id":"1","parent_id_path":"0_1_16","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b195b8340f.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"17","name":"运动户外","mobile_name":"运动户外","parent_id":"1","parent_id_path":"0_1_17","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b198413c66.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"}]
         */

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("mobile_name")
        public String mobileName;
        @SerializedName("parent_id")
        public String parentId;
        @SerializedName("parent_id_path")
        public String parentIdPath;
        @SerializedName("level")
        public String level;
        @SerializedName("sort_order")
        public String sortOrder;
        @SerializedName("is_show")
        public String isShow;
        @SerializedName("image")
        public String image;
        @SerializedName("is_hot")
        public String isHot;
        @SerializedName("cat_group")
        public String catGroup;
        @SerializedName("commission_rate")
        public String commissionRate;
        @SerializedName("icon")
        public String icon;
        @SerializedName("tmenu")
        public ArrayList<TmenuBean> tmenu;

        public static  class TmenuBean {
            /**
             * id : 16
             * name : 鞋靴
             * mobile_name : 鞋靴
             * parent_id : 1
             * parent_id_path : 0_1_16
             * level : 2
             * sort_order : 50
             * is_show : 1
             * image : /Public/upload/category/2017/01-15/587b195b8340f.jpg
             * is_hot : 1
             * cat_group : 0
             * commission_rate : 0
             */

            @SerializedName("id")
            public String id;
            @SerializedName("name")
            public String name;
            @SerializedName("mobile_name")
            public String mobileName;
            @SerializedName("parent_id")
            public String parentId;
            @SerializedName("parent_id_path")
            public String parentIdPath;
            @SerializedName("level")
            public String level;
            @SerializedName("sort_order")
            public String sortOrder;
            @SerializedName("is_show")
            public String isShow;
            @SerializedName("image")
            public String image;
            @SerializedName("is_hot")
            public String isHot;
            @SerializedName("cat_group")
            public String catGroup;
            @SerializedName("commission_rate")
            public String commissionRate;
            @SerializedName("icon")
            public String icon;
        }
    }
}
