package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;
import com.yskj.welcomeorchard.base.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/14.
 */

public class AddressEntity  implements Serializable {


    /**
     * address_list : [{"address_id":"1","user_id":"3","consignee":"姜贺","email":"","country":"0","province":"21387","city":"22655","district":"22671","twon":"0","address":"恐龙卖掉了吗？","zipcode":"","mobile":"13526787582","is_default":"0","full_address":"河南省许昌市许昌县"},{"address_id":"12","user_id":"3","consignee":"电风扇","email":"","country":"0","province":"8558","city":"9553","district":"9603","twon":"0","address":"飞洒","zipcode":"","mobile":"15936365921","is_default":"1","full_address":"黑龙江省伊春市翠峦区"}]
     */
    @SerializedName("error_code")
    public String error_code;
    @SerializedName("address_list")
    public ArrayList<AddressListBean> addressList;

    public static class AddressListBean implements Serializable{
        /**
         * address_id : 1
         * user_id : 3
         * consignee : 姜贺
         * email :
         * country : 0
         * province : 21387
         * city : 22655
         * district : 22671
         * twon : 0
         * address : 恐龙卖掉了吗？
         * zipcode :
         * mobile : 13526787582
         * is_default : 0
         * full_address : 河南省许昌市许昌县
         */

        @SerializedName("address_id")
        public String addressId;
        @SerializedName("user_id")
        public String userId;
        @SerializedName("consignee")
        public String consignee;
        @SerializedName("email")
        public String email;
        @SerializedName("country")
        public String country;
        @SerializedName("province")
        public String province;
        @SerializedName("city")
        public String city;
        @SerializedName("district")
        public String district;
        @SerializedName("twon")
        public String twon;
        @SerializedName("address")
        public String address;
        @SerializedName("zipcode")
        public String zipcode;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("is_default")
        public String isDefault;
        @SerializedName("full_address")
        public String fullAddress;
    }
}
