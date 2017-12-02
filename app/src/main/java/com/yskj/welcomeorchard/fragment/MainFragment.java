package com.yskj.welcomeorchard.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.GoodsListAdapter;
import com.yskj.welcomeorchard.base.BaseFragment;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/11.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {
//    //定义了activity必须实现的接口方法
//    public interface OnGetUrlListener {
//        void getUrl(String url, String goodsName, String imgUrl);
//
//        void getData(String goodsId);
//    }

    // 这里是fragment内部的成员变量,用来接受activity的赋值
    private FragmentInterface.OnGetUrlListener onGetUrlListener;
    PullToRefreshListView lvMallGoods;
    ImageView ivReturn;
    private String id;
    private int page = 1; //请求页
    private int current = 1; //记录请求页
    private GoodsListAdapter adapter;
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListALL = new ArrayList<>();//全部数据
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListPage = new ArrayList<>();//请求的每一页的数据
    private boolean isRequest = true; //是否需要请求
    private boolean isRequesting = false; //是否在请求中

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_other, container, false);
        initView(view);
        initListen();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getGoodsList();
    }

    // 当Fragment被加载到activity的时候会被回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentInterface.OnGetUrlListener) {
            onGetUrlListener = (FragmentInterface.OnGetUrlListener) activity; // 2.2 获取到宿主activity并赋值
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    //把传递进来的activity对象释放掉
    @Override
    public void onDetach() {
        super.onDetach();
        onGetUrlListener = null;
    }

    private void initListen() {
//        lvMallGoods.setMode(PullToRefreshBase.Mode.BOTH);

        lvMallGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isRequest = true;
                page = 1;
                current = 1;
                getGoodsList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                page++;
//                getGoodsList();
            }
        });

        lvMallGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsListEntity.GoodsListBean goodsListBean = goodsListALL.get(position - 1);
                onGetUrlListener.getUrl(goodsListBean.goodsId);
            }
        });

        lvMallGoods.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 4) {
                    ivReturn.setVisibility(View.VISIBLE);
                } else {
                    ivReturn.setVisibility(View.GONE);
                }
                if ((totalItemCount - 2) - firstVisibleItem == 8&&!isRequesting) {
                    isRequesting = true;
                    if (isRequest) {
                        page++;
                        getGoodsList();
                    }
                }
                //当数据加载完后2条将isRequesting 是否在加载中 设置为false
                if ((totalItemCount - 2) - firstVisibleItem == 10){
                    isRequesting = false;
                }
            }
        });
        ivReturn.setOnClickListener(this);
    }

    //网络请求
    private void getGoodsList() {
        OkHttpUtils.get().url(Urls.GOODSLIST + "/id/" + id + "/p/" + page).build().execute(new GoodsListCallBack());
    }

    //网络请求
    private class GoodsListCallBack extends Callback<GoodsListEntity> {
        @Override
        public void onBefore(Request request, int id) {
//            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public GoodsListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            GoodsListEntity registerEntity = new Gson().fromJson(string, GoodsListEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
//            stopMyDialog();
            lvMallGoods.onRefreshComplete();
        }

        @Override
        public void onResponse(GoodsListEntity response, int id) {
//            stopMyDialog();
            lvMallGoods.onRefreshComplete();
            if (page == 1) {
                goodsListALL.clear();
            }
            //每次清空上页数据
            goodsListPage.clear();
            if (response.error_code == 000) {
                goodsListPage = response.goodsList;
                if (goodsListPage.size() == 0) {
                    isRequest = false;
                }
                goodsListALL.addAll(goodsListPage);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView(View view) {
        id = getArguments().getString("id");
        lvMallGoods = (PullToRefreshListView) view.findViewById(R.id.lv_mall_goods);
        adapter = new GoodsListAdapter(getActivity(), goodsListALL, onGetUrlListener);
        lvMallGoods.setAdapter(adapter);
        ivReturn = (ImageView) view.findViewById(R.id.iv_return);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 返回顶部
             */
            case R.id.iv_return:
                lvMallGoods.getRefreshableView().setSelection(0);
                break;

        }
    }
}
