package com.yskj.welcomeorchard.utils;

import android.content.Context;

import com.yskj.welcomeorchard.dialog.MyLoading;


/**
 * 创建日期 2017/6/17on 10:24.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class Loading {
    private Context context;
    private MyLoading myloading;

    public Loading(Context context) {
        this.context = context;
    }

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (context==null){
            return;
        }
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(context);
        }
        myloading.show();
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            myloading.dismiss();
            myloading = null;
        }
    }
}
