package com.yskj.welcomeorchard.fragment;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AllFragmentGoodsListAdapter;
import com.yskj.welcomeorchard.adapter.AllFragmentListRecommendAdapter;
import com.yskj.welcomeorchard.adapter.BeautyMakeupHeadGridAdapter;
import com.yskj.welcomeorchard.base.BaseFragment;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AllFragmentListRecommendEntity;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.entity.WDHFragmentTopRecycleEntity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/4/17on 9:31.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class BeautyMakeupFragment extends BaseFragment implements View.OnClickListener{
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
    private BeautyMakeupHeadGridAdapter Beautyadapter;
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
    private ImageView imgTop;
    private RecyclerView gvTop;
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
        firstPos = getArguments().getString("position");
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
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_fragment_beauty_makeup, null);
        imgTop = (ImageView) headView.findViewById(R.id.img_top);
        gvTop = (RecyclerView) headView.findViewById(R.id.recycler_view);
        tvWdh = (TextView) headView.findViewById(R.id.tv_wdh);
        listRecommend = (NoScrollListView) headView.findViewById(R.id.list_recommend);
        initHeadTopRecycleView();
        initListRecommend();
    }
    ////唯多惠出品fragment 分类接口
    private void initHeadTopRecycleView() {
        //测试id先用1
        OkHttpUtils.get().url(Urls.WDGFRAGMAENTGOODCATEGORY+id).build().execute(new GoodsCategoryCallBack());
    }
    private class GoodsCategoryCallBack extends Callback<WDHFragmentTopRecycleEntity> {

        @Override
        public WDHFragmentTopRecycleEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            WDHFragmentTopRecycleEntity registerEntity = new Gson().fromJson(string, WDHFragmentTopRecycleEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(final WDHFragmentTopRecycleEntity response, int id) {
            if (response.errorCode.equals("000")){
                final WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean goodsCategoryTreeBean = response.goodsCategoryTree.get(0);
                GlideImage.loadImage(getActivity(),imgTop,goodsCategoryTreeBean.image,R.mipmap.img_error);
                int  type = 0;//用于判断adapter类型  如果数量<5 类型为 0 适配器隐藏没用不考虑  如果5<=数量<10  类型为1 列表取前4个数据 后边的为更多 适配器在pos为5时图片换位更多
                //如果10<=数量（即else） 类型为2 列表取前9个数据 后边的为更多 适配器在pos为10时图片换位更多

                if (goodsCategoryTreeBean.tmenu.size()<5){
                    gvTop.setVisibility(View.GONE);
                }else if (goodsCategoryTreeBean.tmenu.size()>=5&&goodsCategoryTreeBean.tmenu.size()<10){
                    gvTop.setVisibility(View.VISIBLE);
                    type = 1;
                    Beautyadapter = new BeautyMakeupHeadGridAdapter(getActivity(),goodsCategoryTreeBean.tmenu.subList(0,5),type,goodsCategoryTreeBean.catGroup);
                    gvTop.setHasFixedSize(true);
                    horilayoutManager =  new GridLayoutManager(getActivity(), 5);
                    horilayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    gvTop.setLayoutManager(horilayoutManager);
                    gvTop.setAdapter(Beautyadapter);
                    Beautyadapter.setOnItemClickListener(new BeautyMakeupHeadGridAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            //100相当于更多标识
                            if (position==4){
                                onGetUrlListener.getTwoPosition(Integer.parseInt(firstPos),100);
                            }else {
                                onGetUrlListener.getTwoPosition(Integer.parseInt(firstPos),position);
                            }

                        }
                    });
                } else {
                    gvTop.setVisibility(View.VISIBLE);
                    type = 2;
                    Beautyadapter = new BeautyMakeupHeadGridAdapter(getActivity(),goodsCategoryTreeBean.tmenu.subList(0,10),type,goodsCategoryTreeBean.catGroup);
                    gvTop.setHasFixedSize(true);
                    horilayoutManager =  new GridLayoutManager(getActivity(), 5);
                    horilayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    gvTop.setLayoutManager(horilayoutManager);
                    gvTop.setAdapter(Beautyadapter);
                    Beautyadapter.setOnItemClickListener(new BeautyMakeupHeadGridAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            //100相当于更多标识
                            if (position==9){
                                onGetUrlListener.getTwoPosition(Integer.parseInt(firstPos),100);
                            }else {
                                onGetUrlListener.getTwoPosition(Integer.parseInt(firstPos),position);
                            }
                        }
                    });
                }

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
            Logger.d("AllFragmentListRecommendEntity===" + string);
            AllFragmentListRecommendEntity registerEntity = new Gson().fromJson(string, AllFragmentListRecommendEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(AllFragmentListRecommendEntity response, int id) {
            if (response.errorCode.equals("000")) {
                AllFragmentListRecommendAdapter allFragmentListRecommendAdapter = new AllFragmentListRecommendAdapter(getActivity(), response.goodsList);
                listRecommend.setAdapter(allFragmentListRecommendAdapter);
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
