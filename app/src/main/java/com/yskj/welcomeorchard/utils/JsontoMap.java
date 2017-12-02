package com.yskj.welcomeorchard.utils;

import java.util.Map;

/**
 * Created with Android Studio.
 * 作者: 陈宙洋
 * 日期: 2016/8/18 14:12
 */
public class JsontoMap {
    /**
     * 获取code
     * @param bytes
     * @return
     */
    public static int getint(byte[] bytes){
        String s = new String(bytes);
        Map<String, Object> map = JSONFormat.jsonToMap(s);
        int c = (int) map.get("error_code");
        return c;
    }

    /**
     * 获取json并转换成map类型
     * @param bytes
     * @return
     */
    public static Map<String,Object> getmap(byte[] bytes){
        String s = new String(bytes);
        Map<String, Object> map = JSONFormat.jsonToMap(s);
        return map;
    }
}
