package com.yskj.welcomeorchard.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Created by YSKJ-02 on 2016/10/26.
 */

public class LoadingCaches {
    private static LoadingCaches instance = null;
    private static LoadingCache<String, String> cache = null;
    public static LoadingCaches getInstance() {
        if (instance == null) {
            init();
            instance = new LoadingCaches();          //line 13
        }
        return instance;
    }
    private static void init() {
        cache  = CacheBuilder.newBuilder()
                //设置缓存最大容量为1000，超过100之后就会按照LRU最近虽少使用算法来移除缓存项,根据缓存需求设置
                .maximumSize(1000)
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                    }
                })
                .build(
                        new CacheLoader<String, String>() {
                            public String load(String String)  {
                                return null;
                            }
                        });

    }
    public static void put(String key,String value){
        if(StringUtils.isEmpty(value)){
            return;
        }
        cache.put(key,value);
    }

    public static String get(String key){
        String keys = null;
        try {
             keys =cache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            keys = "null";
        }

        return keys;
    }
    public static void remover(){
        cache.invalidateAll();
    }
}
