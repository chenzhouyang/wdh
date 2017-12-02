package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/1/14.
 */

public class ShoppingCartEntity implements Serializable{


    /**
     * error_code : 000
     * error_msg : success
     * select_all : null
     * cartList : [{"id":"1186","user_id":"11","session_id":"049r9uon9kv7a6q2ocrveq59h3","goods_id":"142","goods_sn":"TP0000142","goods_name":"海尔（Haier）BCD-251WDGW 251升 无霜两门冰箱（白色）","market_price":"2799.00","goods_price":"0.01","member_goods_price":"0.01","goods_num":"1","spec_key":"","spec_key_name":"","bar_code":"","selected":"0","add_time":"1485153666","prom_type":"0","prom_id":"0","sku":"","farmer_goods":"0","rebate_amount":"0","activity_red":"0","limited":null,"cost_price":"1000.00","acc_points":"0","give_integral":"15","original_img":"/Public/upload/goods/2016/04-22/57199141d9c05.jpg","goods_freight":"0.00","goods_fee":0.01,"store_count":"951"},{"id":"1187","user_id":"11","session_id":"u4hhppatmht96p5b80q6rsu2a6","goods_id":"87","goods_sn":"TP0000087","goods_name":"沐乃衣T2390-2015冬新款女装韩版中长款西装领茧形毛呢大衣外套 1223","market_price":"329.00","goods_price":"229.00","member_goods_price":"229.00","goods_num":"2","spec_key":"79","spec_key_name":"尺码:L","bar_code":"","selected":"0","add_time":"1485153784","prom_type":"0","prom_id":"0","sku":"","farmer_goods":"0","rebate_amount":"0","activity_red":"0","limited":null,"cost_price":"0.00","acc_points":"0","give_integral":"0","original_img":"/Public/upload/goods/2016/01-20/569f377d0628f.jpg","goods_freight":"0.00","goods_fee":458,"store_count":"100"},{"id":"1190","user_id":"11","session_id":"7m822e444l160s27ppefbkpnv3","goods_id":"153","goods_sn":"M-130","goods_name":"我问问若翁绕弯儿翁绕弯儿翁绕弯儿翁绕弯儿 ","market_price":"200.00","goods_price":"200.00","member_goods_price":"200.00","goods_num":"1","spec_key":"59","spec_key_name":"颜色:黑色","bar_code":"","selected":"1","add_time":"1485154510","prom_type":"0","prom_id":"0","sku":"","farmer_goods":"0","rebate_amount":"0","activity_red":"0","limited":null,"cost_price":"100.00","acc_points":"0","give_integral":"130","original_img":"/Public/upload/goods/2017/01-23/588576c666bd1.png","goods_freight":"10.00","goods_fee":200,"store_count":"3"},{"id":"1191","user_id":"11","session_id":"mgubi5c0f6mqjd0nc1sh51gsu1","goods_id":"153","goods_sn":"M-130","goods_name":"我问问若翁绕弯儿翁绕弯儿翁绕弯儿翁绕弯儿 ","market_price":"200.00","goods_price":"200.00","member_goods_price":"200.00","goods_num":"1","spec_key":"58","spec_key_name":"颜色:白色","bar_code":"","selected":"1","add_time":"1485154636","prom_type":"0","prom_id":"0","sku":"","farmer_goods":"0","rebate_amount":"0","activity_red":"0","limited":null,"cost_price":"100.00","acc_points":"0","give_integral":"130","original_img":"/Public/upload/goods/2017/01-23/588576c666bd1.png","goods_freight":"10.00","goods_fee":200,"store_count":"3"},{"id":"1192","user_id":"11","session_id":"mgubi5c0f6mqjd0nc1sh51gsu1","goods_id":"143","goods_sn":"TP0000143","goods_name":"haier海尔 BC-93TMPF 93升单门冰箱","market_price":"799.00","goods_price":"0.01","member_goods_price":"0.01","goods_num":"1","spec_key":"","spec_key_name":"","bar_code":"","selected":"1","add_time":"1485154653","prom_type":"0","prom_id":"0","sku":"","farmer_goods":"0","rebate_amount":"0","activity_red":"0","limited":null,"cost_price":"0.00","acc_points":"0","give_integral":"20","original_img":"/Public/upload/goods/2016/11-01/581854b3df6fd.jpg","goods_freight":"0.00","goods_fee":0.01,"store_count":"913"}]
     * total_price : {"total_fee":400.01,"cut_fee":798.99,"anum":6}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("select_all")
    public Object selectAll;
    @SerializedName("total_price")
    public TotalPriceBean totalPrice;
    @SerializedName("cartList")
    public ArrayList<CartListBean> cartList;

    public static class TotalPriceBean implements Serializable{
        /**
         * total_fee : 400.01
         * cut_fee : 798.99
         * anum : 6
         */

        @SerializedName("total_fee")
        public double totalFee;
        @SerializedName("cut_fee")
        public double cutFee;
        @SerializedName("anum")
        public int anum;
    }

    public static class CartListBean implements Serializable{
        /**
         * id : 1186
         * user_id : 11
         * session_id : 049r9uon9kv7a6q2ocrveq59h3
         * goods_id : 142
         * goods_sn : TP0000142
         * goods_name : 海尔（Haier）BCD-251WDGW 251升 无霜两门冰箱（白色）
         * market_price : 2799.00
         * goods_price : 0.01
         * member_goods_price : 0.01
         * goods_num : 1
         * spec_key :
         * spec_key_name :
         * bar_code :
         * selected : 0
         * add_time : 1485153666
         * prom_type : 0
         * prom_id : 0
         * sku :
         * farmer_goods : 0
         * rebate_amount : 0
         * activity_red : 0
         * limited : null
         * cost_price : 1000.00
         * acc_points : 0
         * give_integral : 15
         * original_img : /Public/upload/goods/2016/04-22/57199141d9c05.jpg
         * goods_freight : 0.00
         * goods_fee : 0.01
         * store_count : 951
         */

        @SerializedName("id")
        public String id;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("session_id")
        public String sessionId;
        @SerializedName("goods_id")
        public String goodsId;
        @SerializedName("goods_sn")
        public String goodsSn;
        @SerializedName("goods_name")
        public String goodsName;
        @SerializedName("market_price")
        public String marketPrice;
        @SerializedName("goods_price")
        public double goodsPrice;
        @SerializedName("member_goods_price")
        public String memberGoodsPrice;
        @SerializedName("goods_num")
        public int goodsNum;
        @SerializedName("spec_key")
        public String specKey;
        @SerializedName("spec_key_name")
        public String specKeyName;
        @SerializedName("bar_code")
        public String barCode;
        @SerializedName("selected")
        public String selected;
        @SerializedName("add_time")
        public String addTime;
        @SerializedName("prom_type")
        public String promType;
        @SerializedName("prom_id")
        public String promId;
        @SerializedName("sku")
        public String sku;
        @SerializedName("farmer_goods")
        public String farmerGoods;
        @SerializedName("rebate_amount")
        public String rebateAmount;
        @SerializedName("activity_red")
        public String activityRed;
        @SerializedName("limited")
        public String limited;
        @SerializedName("cost_price")
        public String costPrice;
        @SerializedName("acc_points")
        public String accPoints;
        @SerializedName("give_integral")
        public String giveIntegral;
        @SerializedName("original_img")
        public String originalImg;
        @SerializedName("goods_freight")
        public String goodsFreight;
        @SerializedName("goods_fee")
        public double goodsFee;
        @SerializedName("is_fail")
        public String is_fail;
        @SerializedName("freight_type")
        public int freightType;
        @SerializedName("store_count")
        public String storeCount;
        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }


        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean isChoosed;
        public boolean isCheck = false;
    }


}
