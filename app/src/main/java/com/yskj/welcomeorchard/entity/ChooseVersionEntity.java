package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/3/16on 10:34.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ChooseVersionEntity {
    /**
     * code : 0
     * message : 成功
     * data : [{"name":"交友广告","id":2},{"name":"招聘广告","id":1}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * name : 交友广告
         * id : 2
         */

        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
    }

//    /**
//     * error_code : 000
//     * error_msg : SUCCESS
//     * adr_tlist : [{"template_id":"1","template_name":"招聘模版","template_description":"招聘模版","thumb":"/Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png"},{"template_id":"2","template_name":"企业模版","template_description":"企业模版企业模版","thumb":"/Public/upload/app_adredtemplate/2017/03-13/58c655b65d5de.jpg"}]
//     */
//
//    @SerializedName("error_code")
//    public String errorCode;
//    @SerializedName("error_msg")
//    public String errorMsg;
//    @SerializedName("adr_tlist")
//    public ArrayList<AdrTlistBean> adrTlist;
//
//    public static class AdrTlistBean {
//        /**
//         * template_id : 1
//         * template_name : 招聘模版
//         * template_description : 招聘模版
//         * thumb : /Public/upload/app_adredtemplate/2017/03-13/58c64fcde327e.png
//         */
//
//        @SerializedName("template_id")
//        public String templateId;
//        @SerializedName("template_name")
//        public String templateName;
//        @SerializedName("template_description")
//        public String templateDescription;
//        @SerializedName("thumb")
//        public String thumb;
//    }
}
