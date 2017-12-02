package com.yskj.welcomeorchard.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by YSKJ-02 on 2017/1/3.
 */

public class Animation {
    private static ScaleAnimation sa;
    //设置背景透明度
    public static void setbackGround(Activity activity, float bgAlpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha != 1f) {
            //此行代码主要是解决在华为手机上半透明效果无效的bug
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        window.setAttributes(lp);
    }
    //缩放
    public static void startScale(ImageView imageView) {
            /*
         * 第一个参数 原始大小, 第二参数 toX 轴截止大小(若起始大小=截止大小就是指x轴不伸缩).第三个参
         * 数fromY Y轴的起始大小  第四个参数toY 轴的截止大小第五个参数pivotXType X轴的原点的类型（相
         * 对于自己而言还是相对于父容器而言） 第六个参数pivotXValue 开始伸缩时的X轴的原点(例:0.5就是
         * 指以图片宽度的二分之一的位置作为X轴的原点)第七个参数pivotYType Y轴的原点的类型第八个参数
         * pivotYValue 开始伸缩时的Y轴的原点
        */

        sa = new ScaleAnimation(1, 1.1f, 1, 1.1f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, android.view.animation.Animation.
                RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
        sa.setRepeatCount(Integer.MAX_VALUE);
        sa.setRepeatMode(android.view.animation.Animation.REVERSE);
        //sa.setFillBefore(true);
        imageView.startAnimation(sa);
    }
}
