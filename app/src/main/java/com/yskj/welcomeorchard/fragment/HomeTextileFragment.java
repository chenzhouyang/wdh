package com.yskj.welcomeorchard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AllFragmentGoodsListAdapter;
import com.yskj.welcomeorchard.adapter.AllFragmentListRecommendAdapter;
import com.yskj.welcomeorchard.adapter.HomeTextileGvBottomAdapter;
import com.yskj.welcomeorchard.adapter.HomeTextileTopRecycleAdapter;
import com.yskj.welcomeorchard.base.BaseFragment;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AllFragmentListRecommendEntity;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.entity.WDHFragmentNewPeopleEntity;
import com.yskj.welcomeorchard.entity.WDHFragmentTopRecycleEntity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.ui.web.MainImageActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/4/17on 17:51.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class HomeTextileFragment extends BaseFragment implements View.OnClickListener{
    private FragmentInterface.OnGetUrlListener onGetUrlListener;
    XRecyclerView lvMallGoods;
    ImageView ivReturn;
    private String id;
    private int page = 1;
    private AllFragmentGoodsListAdapter adapter;
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListALL = new ArrayList<>();//全部数据
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListPage = new ArrayList<>();//请求的每一页的数据
    private boolean isRequest = true; //是否需要请求
    private boolean isRequesting = false; //是否在请求中

    private View view;//底部根视图
    private View headView;
    private boolean isaddheade = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETHEAD:
                    if (!isaddheade) {
                        lvMallGoods.addHeaderView(headView);
                        adapter.notifyDataSetChanged();
                        isaddheade = true;
                    }
                    break;
            }
        }
    };
    private final int SETHEAD = 100;
    private NoScrollListView listRecommend;
    private RecyclerView rvTop;
    private NoScrollGridView gvBottom;
    private ImageView imgTop,imgBottom;
    private TextView tvWdh;
    private GridLayoutManager layoutManager;
    private GridLayoutManager horilayoutManager;
    private String firstPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beauty_makeup, container, false);
        initView(view);
        initListen();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(SETHEAD, 300);
        getGoodsList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    private void initListen() {
        lvMallGoods.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lvMallGoods.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getGoodsList();
                lvMallGoods.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });

        lvMallGoods.setLoadingMoreEnabled(false);

        lvMallGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                // dy>0 表示向下滑动
                if (lastVisibleItem >= 8) {
                    ivReturn.setVisibility(View.VISIBLE);
                } else {
                    ivReturn.setVisibility(View.GONE);
                }
                if (goodsListPage == null || goodsListPage.size() == 0) {
                    return;
                }
                if (lastVisibleItem >= totalItemCount - 4 && !isRequesting) {
                    isRequesting = true;
                    if (isRequest) {
                        page++;
                        getGoodsList();
                    }
                }
            }
        });
        ivReturn.setOnClickListener(this);
    }
    private void initView(View view) {
        id = getArguments().getString("id");

        firstPos =getArguments().getString("position");
        lvMallGoods = (XRecyclerView) view.findViewById(R.id.lv_mall_goods);

        //初始化头部
        initHeadView();

        adapter = new AllFragmentGoodsListAdapter(getActivity(), goodsListALL, onGetUrlListener);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvMallGoods.setLayoutManager(layoutManager);
        lvMallGoods.setHasFixedSize(true);
        lvMallGoods.addItemDecoration(new DividerGridItemDecoration(getActivity(), 15,R.drawable.divider));
        adapter.setOnItemClickListener(new AllFragmentGoodsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GoodsListEntity.GoodsListBean goodsListBean = goodsListALL.get(position);
                onGetUrlListener.getUrl(goodsListBean.goodsId);
            }
        });
        lvMallGoods.setAdapter(adapter);
        ivReturn = (ImageView) view.findViewById(R.id.iv_return);
    }
    private void initHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_fragment_home_textile, null);
        rvTop = (RecyclerView) headView.findViewById(R.id.rv_top);
        gvBottom = (NoScrollGridView) headView.findViewById(R.id.gv_bottom);
        imgTop = (ImageView) headView.findViewById(R.id.img_top);
        imgBottom = (ImageView) headView.findViewById(R.id.img_bottom);
        tvWdh = (TextView) headView.findViewById(R.id.tv_wdh);
        listRecommend = (NoScrollListView) headView.findViewById(R.id.list_recommend);
        initHeadTopRecycleView();
        initBottomGv();
        initListRecommend();
    }
    //家纺fragment top 接口
    private void initHeadTopRecycleView() {
        //测试id先用1
        OkHttpUtils.get().url(Urls.WDGFRAGMAENTGOODCATEGORY+id).build().execute(new GoodsCategoryCallBack());
    }
    private class GoodsCategoryCallBack extends Callback<WDHFragmentTopRecycleEntity> {
        @Override
        public WDHFragmentTopRecycleEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            Logger.json(string);
            WDHFragmentTopRecycleEntity registerEntity = new Gson().fromJson(string, WDHFragmentTopRecycleEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(WDHFragmentTopRecycleEntity response, int id) {
            if (response.errorCode.equals("000")){
                WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean goodsCategoryTreeBean = response.goodsCategoryTree.get(0);
                GlideImage.loadImage(getActivity(),imgTop,goodsCategoryTreeBean.image,R.mipmap.img_error);
                if (goodsCategoryTreeBean.tmenu.size()<3){
                    return;
                }else {
                    HomeTextileTopRecycleAdapter adapter = new HomeTextileTopRecycleAdapter(getActivity(),goodsCategoryTreeBean.tmenu.subList(0,3));
                    layoutManager = new GridLayoutManager(getActivity(), 3);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    rvTop.setLayoutManager(layoutManager);
                    rvTop.setHasFixedSize(true);
                    rvTop.setAdapter(adapter);
                    adapter.setOnItemClickListener(new HomeTextileTopRecycleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            onGetUrlListener.getTwoPosition(Integer.parseInt(firstPos),position);
                        }
                    });

                }
            }
        }
    }
    //家纺底部列表
    private void initBottomGv() {
        OkHttpUtils.get().url(Urls.HOMETEXTILEGRIDBOTTOM).build().execute(new bottomGvCallBack());
    }
    //家纺底部列表
    private class bottomGvCallBack extends Callback<WDHFragmentNewPeopleEntity>{

        @Override
        public WDHFragmentNewPeopleEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            WDHFragmentNewPeopleEntity registerEntity = new Gson().fromJson(string, WDHFragmentNewPeopleEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(final WDHFragmentNewPeopleEntity response, int id) {
            if (response.errorCode.equals("000")){
                GlideImage.loadImage(getActivity(),imgBottom,response.positionInfo.thumb,R.mipmap.img_error);
                HomeTextileGvBottomAdapter adapter = new HomeTextileGvBottomAdapter(getActivity(),response.adList);
                gvBottom.setAdapter(adapter);
                gvBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(response.adList.get(position).adType == 0){
                            showToast("敬请期待...");
                        }else if(response.adList.get(position).adType == 1){
                            startActivity(new Intent(getActivity(), CommodityDetailsActiviy.class).putExtra("goodid",response.adList.get(position).adLink));
                        }else if(response.adList.get(position).adType == 2){
                            startActivity(new Intent(getActivity(), MainImageActivity.class).putExtra("url",response.adList.get(position).adLink));
                        }
                    }
                });
            }
        }
    }


    //唯多惠商品网络请求
    private void getGoodsList() {
        OkHttpUtils.get().url(Urls.GOODSLIST + "/p/" + page+"/id/"+id).build().execute(new GoodsListCallBack());
    }
    //唯多惠商品网络请求
    private class GoodsListCallBack extends Callback<GoodsListEntity> {

        @Override
        public GoodsListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            GoodsListEntity registerEntity = new Gson().fromJson(string, GoodsListEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(GoodsListEntity response, int id) {
            if (page == 1) {
                goodsListALL.clear();
            }
            //当数据加载完后将isRequesting 是否在加载中 设置为false
            isRequesting = false;//加载完成后初始化
            //每次清空上页数据
            goodsListPage.clear();
            if (response.error_code == 000) {
                goodsListPage = response.goodsList;
                goodsListALL.addAll(goodsListPage);
                adapter.notifyDataSetChanged();
            }
        }
    }

    //唯多惠推荐产品
    private void initListRecommend() {
        OkHttpUtils.get().url(Urls.ALLFRAGMENTRECOMMEND+"/id/"+id).build().execute(new ListRecommendCallBack());
    }

    private class ListRecommendCallBack extends Callback<AllFragmentListRecommendEntity> {

        @Override
        public AllFragmentListRecommendEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            AllFragmentListRecommendEntity registerEntity = new Gson().fromJson(string, AllFragmentListRecommendEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(final AllFragmentListRecommendEntity response, int id) {
            if (response.errorCode.equals("000")) {
                AllFragmentListRecommendAdapter allFragmentListRecommendAdapter = new AllFragmentListRecommendAdapter(getActivity(), response.goodsList);
                listRecommend.setAdapter(allFragmentListRecommendAdapter);
                listRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(getActivity(), CommodityDetailsActiviy.class).putExtra("goodid",response.goodsList.get(position).goodsId));
                    }
                });
            }
        }
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
                lvMallGoods.scrollToPosition(0);
                break;

        }
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
}
