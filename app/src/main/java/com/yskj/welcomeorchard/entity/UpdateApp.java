package com.yskj.welcomeorchard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/3/29.
 */

public class UpdateApp {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * version_info : {"version_code":"1","version_name":"1.1","version_description":"1.1版本","thumb":"/Public/upload/File/app_apk/58d8c3943d0f4.apk"}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("version_info")
    public VersionInfoBean versionInfo;

    public static class VersionInfoBean {
        /**
         * version_code : 1
         * version_name : 1.1
         * version_description : 1.1版本
         * thumb : /Public/upload/File/app_apk/58d8c3943d0f4.apk
         */

        @SerializedName("version_code")
        public String versionCode;
        @SerializedName("version_name")
        public String versionName;
        @SerializedName("version_description")
        public String versionDescription;
        @SerializedName("thumb")
        public String thumb;
    }
}
