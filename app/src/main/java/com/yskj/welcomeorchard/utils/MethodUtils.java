package com.yskj.welcomeorchard.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.pulltorefresh.library.PullToRefreshListView;

import java.io.UnsupportedEncodingException;

/**
 * 缓存
 */
public class MethodUtils {
    static Context utilsContext;
    static String showText;
    private static String string;
    private static String json;

    public static void stopRefresh(final PullToRefreshListView ptr) {
        ptr.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptr.onRefreshComplete();
            }
        }, 300);
    }


    /**
     * MethodUtils.showToash();调用
     * <p>
     * 弹吐司
     * 在activity中用**Activity.this
     * 在Fragment中用getActivity
     * 在page中用getContext()
     *
     * @param context 上下文
     * @param text    吐司内容
     */
    public static void showToast(Context context, String text) {
        utilsContext = context;
        showText = text;
        boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
        if (isMainThread) {
            Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
        } else {
            Activity activity = (Activity) utilsContext;
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(utilsContext, showText, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    /**
     * 转换字符串格式
     *
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String codeConvert(String str) {
        try {
            string = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }


    /**
     * 将px转换为dp
     *
     * @param context
     * @param
     * @return
     */
    public int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * dp转换为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
