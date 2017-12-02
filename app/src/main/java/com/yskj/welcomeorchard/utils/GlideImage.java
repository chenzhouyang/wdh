package com.yskj.welcomeorchard.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.widget.CircleImageView;

/**
 * Glide 图片加载辅助类
 * 适配圆形图片加载情况
 */

public class GlideImage {
    public static void loadImage(RequestManager loader, ImageView view, String url) {
        loadImage(loader, view, url, 0);
    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder) {
        loadImage(loader, view, url, placeholder, placeholder);
    }
    public static void loadImage(Context context, ImageView view, String url) {
        loadImage(context, view, url, 0);
    }

    public static void loadImage(Context context, ImageView view, String url, int placeholder) {
        loadImage(context, view, url, placeholder, placeholder);
    }

    public static void loadImage(Context context, ImageView view, String url, int placeholder, int error) {
        if(context!=null&&context.getApplicationContext()!=null){
            loadImage(Glide.with(context.getApplicationContext()), view, url, placeholder, error);
        }

    }

    public static void loadImage(RequestManager loader, ImageView view, String url, int placeholder, int error) {
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(placeholder);
            return;
        }
        if (!url.startsWith("http")) {
            if(!url.contains(".")){
                url = Ips.API_URL_PHOTO+url;
            }else {
                url = Ips.PHPURL + url;
            }
        }
        if (view instanceof CircleImageView) {
            BitmapRequestBuilder builder = loader.load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(placeholder).dontAnimate().error(error);

            builder.into(new BitmapImageViewTarget(view) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    view.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            DrawableRequestBuilder builder = loader.load(url).diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(placeholder).dontAnimate().error(error);
            if(view!=null){
                builder.into(view);
            }

        }
    }
}
