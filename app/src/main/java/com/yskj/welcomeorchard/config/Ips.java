package com.yskj.welcomeorchard.config;

/**
 * Created by Administrator on 2016/10/13 0013.
 */
public class Ips {
    //测试环境
//  ###########################################################################################
//
//    public final static String HOST = "192.168.0.196:8080";
//    public final static String PHPURL = "http://192.168.0.181";
//    public static final String MALL = "https://www.pgyer.com/ma3p";
//    public static final int BAIDU = 175910;//测试红包表聚合ID
//    public static final String BAIDUAK = "LEm0zzyntxeKIm29ZxwGElYy1zDy83Ct";
//    public static final String HTTP = "http://";
//    public static final boolean ONLINE = false;

//  ###########################################################################################


    //生产环境
//  ###########################################################################################
//
    public final static String PHPURL = "https://www.wdh158.com";    //正式
    public final static String HOST = "api.wdh158.com";
    public static final String MALL = "https://www.pgyer.com/9pCw";
    public static final int BAIDU = 175906;//正式红包表聚合ID
    public static final String BAIDUAK = "LEm0zzyntxeKIm29ZxwGElYy1zDy83Ct";
    public static final String HTTP = "https://";
    public static final boolean ONLINE = true;


//  ###########################################################################################

    public final static String API_URL = HTTP + HOST + "/";
    public final static String SK = "2ab99a6af22365686e97992df974d5c2";
    public final static String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public final static String AUTHORIZATION = "Basic bW9iaWxlOjEyMzQ1Ng==";

    //用户头像
    public final static String API_URL_PHOTO = HTTP + HOST + "/api/v1/mgs/file/download?fid=";

}
