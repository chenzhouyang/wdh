package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/14.
 */

public class GoodsCategoryListEntity  implements Serializable{

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * goods_category_tree : [{"id":"1","name":"服装箱包","mobile_name":"服装箱包","parent_id":"0","parent_id_path":"0_1","level":"1","sort_order":"43","is_show":"1","image":"/Public/upload/category/2017/01-15/587b107e4fad4.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"12","name":"男装","mobile_name":"男装","parent_id":"1","parent_id_path":"0_1_12","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816bb02ef96.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"13","name":"内衣袜裤","mobile_name":"内衣袜裤","parent_id":"1","parent_id_path":"0_1_13","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588176c557d31.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"14","name":"女装","mobile_name":"女装","parent_id":"1","parent_id_path":"0_1_14","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/5881767564861.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"15","name":"箱包配饰","mobile_name":"箱包配饰","parent_id":"1","parent_id_path":"0_1_15","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1938b0e82.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"16","name":"鞋靴","mobile_name":"鞋靴","parent_id":"1","parent_id_path":"0_1_16","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b195b8340f.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"17","name":"运动户外","mobile_name":"运动户外","parent_id":"1","parent_id_path":"0_1_17","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b198413c66.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"}]},{"id":"4","name":"家居生活","mobile_name":"家居生活","parent_id":"0","parent_id_path":"0_4","level":"1","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-22/5719c41e70959.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"31","name":"厨房用品","mobile_name":"厨房用品","parent_id":"4","parent_id_path":"0_4_31","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b19e773878.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"32","name":"打扫用品","mobile_name":"打扫用品","parent_id":"4","parent_id_path":"0_4_32","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1a20a5158.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"33","name":"家庭清洁","mobile_name":"家庭清洁","parent_id":"4","parent_id_path":"0_4_33","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1a4fcc868.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"34","name":"女性护理","mobile_name":"女性护理","parent_id":"4","parent_id_path":"0_4_34","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1a770a87c.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"35","name":"烹饪工具","mobile_name":"烹饪工具","parent_id":"4","parent_id_path":"0_4_35","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1a9dde963.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"36","name":"水杯餐具","mobile_name":"水杯餐具","parent_id":"4","parent_id_path":"0_4_36","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1acbe130a.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"37","name":"休闲户外","mobile_name":"休闲户外","parent_id":"4","parent_id_path":"0_4_37","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1af171bbe.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"38","name":"纸品湿巾","mobile_name":"纸品湿巾","parent_id":"4","parent_id_path":"0_4_38","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1b3dec5c0.png","is_hot":"0","cat_group":"0","commission_rate":"0"}]},{"id":"5","name":"绿色保健","mobile_name":"绿色保健","parent_id":"0","parent_id_path":"0_5","level":"1","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-22/5719c432da41e.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"937","name":"男保健品","mobile_name":"男保健品","parent_id":"5","parent_id_path":"0_5_937","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/588170b05be27.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"938","name":"女保健品","mobile_name":"女保健品","parent_id":"5","parent_id_path":"0_5_938","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/588170c30c463.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"939","name":"日常保健","mobile_name":"日常保健","parent_id":"5","parent_id_path":"0_5_939","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/588170d8877b6.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"39","name":"科技保健","mobile_name":"科技保健","parent_id":"5","parent_id_path":"0_5_39","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1bad8a0cf.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"40","name":"老年保健","mobile_name":"老年保健","parent_id":"5","parent_id_path":"0_5_40","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1bf3e8e26.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"41","name":"绿色食品","mobile_name":"绿色食品","parent_id":"5","parent_id_path":"0_5_41","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1c2c70bde.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"}]},{"id":"7","name":"美妆个护","mobile_name":"美妆个护","parent_id":"0","parent_id_path":"0_7","level":"1","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-22/5719c445a02ba.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"927","name":"眼霜","mobile_name":"眼霜","parent_id":"7","parent_id_path":"0_7_927","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816f0d58f58.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"928","name":"美容工具","mobile_name":"美容工具","parent_id":"7","parent_id_path":"0_7_928","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816f5b3fb19.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"929","name":"面膜","mobile_name":"面膜","parent_id":"7","parent_id_path":"0_7_929","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816f745cc38.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"930","name":"面霜","mobile_name":"面霜","parent_id":"7","parent_id_path":"0_7_930","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816f8a7eff4.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"931","name":"沐浴清洁","mobile_name":"沐浴清洁","parent_id":"7","parent_id_path":"0_7_931","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816f9c6826e.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"932","name":"其他护理","mobile_name":"其他护理","parent_id":"7","parent_id_path":"0_7_932","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816fb02363c.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"933","name":"乳液","mobile_name":"乳液","parent_id":"7","parent_id_path":"0_7_933","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816fe9588d7.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"934","name":"手足护理","mobile_name":"手足护理","parent_id":"7","parent_id_path":"0_7_934","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58816ffee437b.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"935","name":"洗发护发","mobile_name":"洗发护发","parent_id":"7","parent_id_path":"0_7_935","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58817015532ea.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"936","name":"香水","mobile_name":"香水","parent_id":"7","parent_id_path":"0_7_936","level":"2","sort_order":"0","is_show":"1","image":"/Public/upload/category/2017/01-20/58817026be44b.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"49","name":"面部清洁","mobile_name":"面部清洁","parent_id":"7","parent_id_path":"0_7_49","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816c8d5cb25.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"50","name":"彩妆","mobile_name":"彩妆","parent_id":"7","parent_id_path":"0_7_50","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816d6c8b596.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"51","name":"成人用品","mobile_name":"成人用品","parent_id":"7","parent_id_path":"0_7_51","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816da84b6b0.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"52","name":"防晒","mobile_name":"防晒","parent_id":"7","parent_id_path":"0_7_52","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816dc545fc0.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"53","name":"化妆水","mobile_name":"化妆水","parent_id":"7","parent_id_path":"0_7_53","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816de668561.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"54","name":"精华精油","mobile_name":"精华精油","parent_id":"7","parent_id_path":"0_7_54","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816e211d6b2.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"55","name":"口腔护理","mobile_name":"口腔护理","parent_id":"7","parent_id_path":"0_7_55","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816e62d8c79.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"}]},{"id":"8","name":"母婴玩具","mobile_name":"母婴玩具","parent_id":"0","parent_id_path":"0_8","level":"1","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-22/5719c45078f06.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"56","name":"宝宝用品","mobile_name":"宝宝用品","parent_id":"8","parent_id_path":"0_8_56","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588170f7384cc.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"57","name":"教娱玩具","mobile_name":"教娱玩具","parent_id":"8","parent_id_path":"0_8_57","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/5881712678334.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"58","name":"奶粉辅食","mobile_name":"奶粉辅食","parent_id":"8","parent_id_path":"0_8_58","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/5881715817bf6.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"59","name":"尿裤湿巾","mobile_name":"尿裤湿巾","parent_id":"8","parent_id_path":"0_8_59","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588171829f252.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"60","name":"童装童鞋","mobile_name":"童装童鞋","parent_id":"8","parent_id_path":"0_8_60","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588171a86d423.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"61","name":"婴童洗护","mobile_name":"婴童洗护","parent_id":"8","parent_id_path":"0_8_61","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588171ce6f336.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"62","name":"孕妈专区","mobile_name":"孕妈专区","parent_id":"8","parent_id_path":"0_8_62","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588171ef64ca8.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"}]},{"id":"3","name":"电脑、办公","mobile_name":"电脑","parent_id":"0","parent_id_path":"0_3","level":"1","sort_order":"51","is_show":"1","image":"/Public/upload/category/2016/04-22/5719c40f21341.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"24","name":"网络产品","mobile_name":"网络产品","parent_id":"3","parent_id_path":"0_3_24","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"25","name":"办公设备","mobile_name":"办公设备","parent_id":"3","parent_id_path":"0_3_25","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"26","name":"文具耗材","mobile_name":"文具耗材","parent_id":"3","parent_id_path":"0_3_26","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"27","name":"电脑整机","mobile_name":"电脑整机","parent_id":"3","parent_id_path":"0_3_27","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"28","name":"电脑配件","mobile_name":"电脑配件","parent_id":"3","parent_id_path":"0_3_28","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"29","name":"外设产品","mobile_name":"外设产品","parent_id":"3","parent_id_path":"0_3_29","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"30","name":"游戏设备","mobile_name":"游戏设备","parent_id":"3","parent_id_path":"0_3_30_844_914","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"}]},{"id":"2","name":"家用电器","mobile_name":"家用电器","parent_id":"0","parent_id_path":"0_2","level":"1","sort_order":"52","is_show":"1","image":"/Public/upload/category/2016/04-22/5719c3c5bc052.jpg","is_hot":"1","cat_group":"0","commission_rate":"0","tmenu":[{"id":"19","name":"生活电器","mobile_name":"生活电器","parent_id":"2","parent_id_path":"0_2_19","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"20","name":"大家电","mobile_name":"大家电","parent_id":"2","parent_id_path":"0_2_20_844_914","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"21","name":"厨房电器","mobile_name":"厨房电器","parent_id":"2","parent_id_path":"0_2_21","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"22","name":"个护健康","mobile_name":"个护健康","parent_id":"2","parent_id_path":"0_2_22","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"},{"id":"23","name":"五金交电","mobile_name":"五金交电","parent_id":"2","parent_id_path":"0_2_23","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2016/04-02/56ffa28b12f4f.jpg","is_hot":"0","cat_group":"0","commission_rate":"0"}]}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("goods_category_tree")
    public ArrayList<GoodsCategoryTreeBean> goodsCategoryTree;

    public static class GoodsCategoryTreeBean implements Serializable{
        /**
         * id : 1
         * name : 服装箱包
         * mobile_name : 服装箱包
         * parent_id : 0
         * parent_id_path : 0_1
         * level : 1
         * sort_order : 43
         * is_show : 1
         * image : /Public/upload/category/2017/01-15/587b107e4fad4.jpg
         * is_hot : 1
         * cat_group : 0
         * commission_rate : 0
         * tmenu : [{"id":"12","name":"男装","mobile_name":"男装","parent_id":"1","parent_id_path":"0_1_12","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/58816bb02ef96.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"13","name":"内衣袜裤","mobile_name":"内衣袜裤","parent_id":"1","parent_id_path":"0_1_13","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/588176c557d31.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"14","name":"女装","mobile_name":"女装","parent_id":"1","parent_id_path":"0_1_14","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-20/5881767564861.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"15","name":"箱包配饰","mobile_name":"箱包配饰","parent_id":"1","parent_id_path":"0_1_15","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b1938b0e82.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"16","name":"鞋靴","mobile_name":"鞋靴","parent_id":"1","parent_id_path":"0_1_16","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b195b8340f.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"},{"id":"17","name":"运动户外","mobile_name":"运动户外","parent_id":"1","parent_id_path":"0_1_17","level":"2","sort_order":"50","is_show":"1","image":"/Public/upload/category/2017/01-15/587b198413c66.jpg","is_hot":"1","cat_group":"0","commission_rate":"0"}]
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
        @SerializedName("tmenu")
        public ArrayList<TmenuBean> tmenu;

        public static class TmenuBean implements Serializable{
            /**
             * id : 12
             * name : 男装
             * mobile_name : 男装
             * parent_id : 1
             * parent_id_path : 0_1_12
             * level : 2
             * sort_order : 50
             * is_show : 1
             * image : /Public/upload/category/2017/01-20/58816bb02ef96.jpg
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
        }
    }
}
