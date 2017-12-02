package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/15on 11:12.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ChooseVersionItemEntity  {

    /**
     * error_code : 000
     * error_msg : SUCCESS
     * adr_tlist : [{"template_id":"4","template_name":"招聘封面","template_description":"招聘模版1","thumb":"/Public/upload/app_adredtemplate/2017/05-08/5910357fa0f6e.png"},{"template_id":"5","template_name":"企业介绍","template_description":"企业介绍","thumb":"/Public/upload/app_adredtemplate/2017/05-08/591035acea736.png"},{"template_id":"6","template_name":"招聘职位","template_description":"招聘职位","thumb":"/Public/upload/app_adredtemplate/2017/05-08/591035c741fe6.png"},{"template_id":"7","template_name":"联系我们","template_description":"联系我们","thumb":"/Public/upload/app_adredtemplate/2017/05-08/591035f02f31e.png"}]
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("adr_tlist")
    public ArrayList<AdrTlistBean> adrTlist;

    public static class AdrTlistBean {
        /**
         * template_id : 4
         * template_name : 招聘封面
         * template_description : 招聘模版1
         * thumb : /Public/upload/app_adredtemplate/2017/05-08/5910357fa0f6e.png
         */

        @SerializedName("template_id")
        public String templateId;
        @SerializedName("template_name")
        public String templateName;
        @SerializedName("template_description")
        public String templateDescription;
        @SerializedName("thumb")
        public String thumb;
    }
}
