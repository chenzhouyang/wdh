package com.yskj.welcomeorchard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by YSKJ-JH on 2017/1/11.
 * 首页viewPager适配器
 */

public class PageFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public PageFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.fragmentList=fragmentList;
        this.fm=fm;
    }

    @Override
    public Fragment getItem(int idx) {
        // TODO Auto-generated method stub
        return fragmentList.get(idx);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragmentList.size();
    }
    //    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;  //没有找到child要求重新加载
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);//重写该方法，保证item不被销毁，从而返回每个已加载的Fragment时，不再重新加载,增加Viewpager滑动时的流畅性
    }
}
