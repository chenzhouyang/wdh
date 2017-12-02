package com.yskj.welcomeorchard.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.GoodCateAdapter;
import com.yskj.welcomeorchard.base.BaseFragment;
import com.yskj.welcomeorchard.entity.GoodsCategoryListEntity;

import java.util.ArrayList;

import static com.yskj.welcomeorchard.R.id.listView;


/**
 * Created by YSKJ-JH on 2017/1/12.
 */

public class GoodCateFragment extends BaseFragment implements View.OnClickListener{
    private ArrayList<GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean> list;
    private ImageView hint_img;
    private GridView gridView;
    private GoodCateAdapter adapter;
    private String typename;
    private TextView tv_toptype;
    private LinearLayout llTop;

    private int firstPosition;


    private FragmentInterface.OnGetUrlListener onGetDataListener;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_top:
                onGetDataListener.getfirstPosition(firstPosition+1);
                break;
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_good_cate,null);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        typename=getArguments().getString("typename");
        list = (ArrayList<GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean>) getArguments().getSerializable("list");
        firstPosition = getArguments().getInt("firstPosition",0);
        tv_toptype.setText(typename);
        //如果没数据就显示图片，返回  如果有数据就显示数据
        if (list.size()==0){
            hint_img.setImageResource(R.mipmap.img_error);
            hint_img.setVisibility(View.VISIBLE);
            return;
        }
        adapter=new GoodCateAdapter(getActivity(), list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                onGetDataListener.getTwoPosition(firstPosition,arg2);
            }
        });
    }
    // 当Fragment被加载到activity的时候会被回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof FragmentInterface.OnGetUrlListener)
            onGetDataListener = (FragmentInterface.OnGetUrlListener) activity; // 2.2 获取到宿主activity并赋值
        else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }
    //把传递进来的activity对象释放掉
    @Override
    public void onDetach() {
        super.onDetach();
        onGetDataListener = null;
    }


    private void initView(View view) {
        hint_img=(ImageView) view.findViewById(R.id.hint_img);
        gridView = (GridView) view.findViewById(listView);
        tv_toptype = (TextView)view.findViewById(R.id.tv_toptype);
        llTop = (LinearLayout) view.findViewById(R.id.ll_top);
        llTop.setOnClickListener(this);
    }
}
