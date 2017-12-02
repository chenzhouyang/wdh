package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-JH on 2017/2/11.
 */

public class CityTestEntity {
    /**
     * id : 1
     * name : 北京市
     * city : [{"id":"2","name":"市辖区","city":[{"id":"3","name":"东城区"},{"id":"14","name":"西城区"},{"id":"22","name":"崇文区"},{"id":"30","name":"宣武区"},{"id":"39","name":"朝阳区"},{"id":"83","name":"丰台区"},{"id":"105","name":"石景山区"},{"id":"115","name":"海淀区"},{"id":"145","name":"门头沟区"},{"id":"159","name":"房山区"},{"id":"188","name":"通州区"},{"id":"204","name":"顺义区"},{"id":"227","name":"昌平区"},{"id":"245","name":"大兴区"},{"id":"264","name":"怀柔区"},{"id":"281","name":"平谷区"}]},{"id":"300","name":"县","city":[{"id":"301","name":"密云县"},{"id":"322","name":"延庆县"}]}]
     */

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("city")
    public ArrayList<CityBeanX> city;

    public static class CityBeanX {
        /**
         * id : 2
         * name : 市辖区
         * city : [{"id":"3","name":"东城区"},{"id":"14","name":"西城区"},{"id":"22","name":"崇文区"},{"id":"30","name":"宣武区"},{"id":"39","name":"朝阳区"},{"id":"83","name":"丰台区"},{"id":"105","name":"石景山区"},{"id":"115","name":"海淀区"},{"id":"145","name":"门头沟区"},{"id":"159","name":"房山区"},{"id":"188","name":"通州区"},{"id":"204","name":"顺义区"},{"id":"227","name":"昌平区"},{"id":"245","name":"大兴区"},{"id":"264","name":"怀柔区"},{"id":"281","name":"平谷区"}]
         */

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("city")
        public ArrayList<CityBean> city;

        public static class CityBean {
            /**
             * id : 3
             * name : 东城区
             */

            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
        }
    }
}
