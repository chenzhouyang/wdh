package com.yskj.welcomeorchard.base;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * 创建日期 2017/4/24on 11:21.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class SimpleGlideModule implements GlideModule {

    // 图片缓存最大容量，150M，根据自己的需求进行修改
    public static final int GLIDE_CATCH_SIZE = 100 * 1024 * 1024;

    // 图片缓存子目录
    public static final String GLIDE_CARCH_DIR = "image_catch";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GLIDE_CARCH_DIR,
                GLIDE_CATCH_SIZE));
        //设置内存缓存大小，一般默认使用glide内部的默认值
        builder.setMemoryCache(new LruResourceCache(GLIDE_CATCH_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
