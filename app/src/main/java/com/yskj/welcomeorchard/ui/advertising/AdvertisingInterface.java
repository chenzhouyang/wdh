package com.yskj.welcomeorchard.ui.advertising;

/**
 * Created by YSKJ-02 on 2017/5/4.
 */

public class AdvertisingInterface {
    //定义了activity必须实现的接口方法
    public interface OnGetadpterListener {
        //领取红包
        void getRed(int postion);
        //关闭弹出框
        void showdialog();
    }
}
