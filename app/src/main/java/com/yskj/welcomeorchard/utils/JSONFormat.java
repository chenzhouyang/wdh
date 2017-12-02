package com.yskj.welcomeorchard.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 对于传值和接收的值进行格式化
 *
 * @author 陈宙洋
 *         2015-7-28 11:36:39
 */
@SuppressWarnings("unchecked")
public class JSONFormat {
    /**
     * 根据返回值转换成map集合
     *
     * @param json json字符串,格式如 {"id":1001, "name":"张三"}
     * @return map集合
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keyIterator = jsonObject.keys();
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                Object value = jsonObject.get(key);
                map.put(key, value == null || value == JSONObject.NULL ? null : value);
            }
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }
        return map;
    }


    /**
     * list 转json字符串
     *
     * @param list
     * @return
     */
    public static String listToJson(List<Map<String, Object>> list) {
        Gson gson = new Gson();
        return gson.toJson(list);


    }

    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            Object value = jsonObject.get(key);
            map.put(key, value == null || value == JSONObject.NULL ? null : value);
        }

        return map;
    }

    /**
     * 根据返回值转换成list集合
     *
     * @param json json字符串,格式如[{}, {}]
     * @return list集合
     */
    public static List<Map<String, Object>> jsonToListMap(String json) {
        List<Map<String, Object>> list = null;
        try {
            list = new ArrayList<Map<String, Object>>();
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                list.add(jsonToMap(jsonObject.toString()));
            }
        } catch (JSONException e) {
            return null;
        }
        return list;
    }

    /**
     * 解析比较复杂的map-json字符串 包含各种数据类型 字符串 布尔型 对象
     *
     * @param json json字符串
     * @return map集合
     */
    public static <T> Map<String, Object> jsonToComplexMap(String json, Class<T> cls) {
        Map<String, Object> map = null;
        if (json.startsWith("{") && json.endsWith("}")) {//判断是否都是对象格式
            map = jsonMapToMap(json, cls);
        }
        return map;
    }

    /**
     * 解析比较复杂的list-json字符串 包含多个同类型的数组
     *
     * @param json json字符串
     * @return list集合
     */
    public static <T> List<T> jsonToComplexList(String json, Class<T> cls) {
        List<T> list = null;
        if (json.startsWith("[") && json.endsWith("]")) {//判断是否都是数组格式
            list = jsonListToList(json, cls);
        }
        return list;
    }

    public static List<Map<String, Object>> jsonToComplexListMap(String json) {
        List list = null;
        if (json.startsWith("[") && json.endsWith("]")) {//判断是否都是数组格式
            list = jsonListToList(json, Map.class);
        }
        return list;
    }


    /**
     * 包含多层map的字符串解析
     *
     * @param json json字符串
     * @return map集合
     */
    public static <T> Map<String, Object> jsonMapToMap(String json, Class<T> cls) {
        Map<String, Object> map = null;
        try {
            map = new HashMap<String, Object>();
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> keyIterator = jsonObject.keys();
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                Object value = jsonObject.get(key);
                //System.out.println(key + "=" + value);
                if (value != null && !"".equals(value)) {
                    String val = value.toString();
                    if (val.startsWith("{") && val.toString().endsWith("}")) {//object
                        //System.out.println(1111);
                        value = jsonMapToMap(val, cls);//迭代
                    } else if (val.startsWith("[") && val.endsWith("]")) {//list
                        //System.out.println(1222);
                        value = jsonListToList(val, cls);
                    }
                }
                map.put(key, value == null || value == JSONObject.NULL ? null : value);
            }
        } catch (JSONException e) {
            return null;
        }

        return map;
    }

    /**
     * 包含多层list的字符串解析
     *
     * @param json json字符串
     * @return list集合
     */
    public static <T> List<T> jsonListToList(String json, Class<T> cls) {
        List<T> list = null;
        try {
            list = new ArrayList<T>();
            JSONArray array = new JSONArray(json);
            //System.out.println("数组长度：" + array.length());
            //System.out.println(cls.getName());
            //System.out.println(cls.getSimpleName());
            String simpleName = cls.getSimpleName();
            for (int i = 0; i < array.length(); i++) {
                Object value = null;
                //System.out.println("999999");
                if ("Boolean".equals(simpleName) || "Byte".equals(simpleName) || "Character".equals(simpleName) || "Short".equals(simpleName)
                        || "Integer".equals(simpleName) || "Long".equals(simpleName) || "Float".equals(simpleName) || "Double".equals(simpleName)) {
                    //System.out.println(999);
                    value = array.get(i);
                } else {
                    value = array.getJSONObject(i);
                }

                String val = value == null ? null : value.toString();
                //System.out.println("array[" + i + "]=" + val);
                if (val.startsWith("{") && val.toString().endsWith("}")) {//object
                    //System.out.println(2222);
                    value = jsonMapToMap(val, cls);
                } else if (val.startsWith("[") && val.toString().endsWith("]")) {
                    //System.out.println(2333);
                    value = jsonListToList(val, cls); //迭代
                }

                list.add(value == null || value == JSONObject.NULL ? null : (T) value);
            }
        } catch (JSONException e) {
            return null;
        }
        return list;
    }

    /**
     * 通过json字符串转换成对应的bean
     *
     * @param json
     * @param cls
     * @return
     */
    public <T> T jsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = gson.fromJson(json, cls);
        return t;
    }

    /**
     * 通过json字符串装换成list包含bean的集合
     *
     * @param json
     * @return
     */
    public static <T> List<T> jsonToListBean(String json,Class<T> cls) {

        List<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * bean转json字符串
     *
     * @param t
     * @return
     */
    public <T> String beanToJson(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    /**
     * list包含bean转换成json字符串
     *
     * @param list
     * @return
     */
    public <T> String listBeanToJson(List<T> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public static StringBuffer setPostPassParamsint(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer;
    }

    public static String lpad(String s, int n, String replace) {
        while (s.length() < n) {
            s = replace + s;
        }
        return s;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    //解析成实体类
    public static <T> T parseT(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }

}
