package com.yskj.welcomeorchard.fragment;

import android.os.Bundle;

/**
 * Created by YSKJ-02 on 2017/3/15.
 */

public class FragmentInterface {
    //定义了activity必须实现的接口方法
    public interface OnGetUrlListener {
        //跳转到商品详情
        void getUrl(String id);
        //跳转到买家秀
        void getData(String goodsId);
        //跳转到一级分类
        void getfirstPosition(int firstPosition);
        //跳转到二级分类
        void getTwoPosition(int firstPosition,int twoPosition);
        //列表跳转指定地方   bundle 第一个传值为from 即从哪个界面来
        void  getBandle(Bundle bundle);
    }
}
