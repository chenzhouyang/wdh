package com.yskj.welcomeorchard.fragment;

import android.app.Activity;
import android.content.Context;
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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AllFragmentGoodsListAdapter;
import com.yskj.welcomeorchard.adapter.AllFragmentHeadGridViewAdapter;
import com.yskj.welcomeorchard.adapter.AllFragmentHeadListTimeAdapter;
import com.yskj.welcomeorchard.adapter.AllFragmentListRecommendAdapter;
import com.yskj.welcomeorchard.base.BaseFragment;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AllFragmentHeadViewEntity;
import com.yskj.welcomeorchard.entity.AllFragmentListRecommendEntity;
import com.yskj.welcomeorchard.entity.AllFragmentListTimeEntity;
import com.yskj.welcomeorchard.entity.AllFragmentMyselfEntity;
import com.yskj.welcomeorchard.entity.CarcuseEntity;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.home.CellsActivitty;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.ui.web.MainImageActivity;
import com.yskj.welcomeorchard.utils.GlideCatchUtil;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/11.
 */

public class AllFragment extends BaseFragment implements View.OnClickListener {
    private FragmentInterface.OnGetUrlListener onGetUrlListener;
    XRecyclerView lvMallGoods;
    ImageView ivReturn;
    private String id;
    private int page = 1;
    private AllFragmentGoodsListAdapter adapter;
    private CarcuseEntity convenientBannerEntities;
    private ArrayList image = new ArrayList();
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListALL = new ArrayList<>();//全部数据
    private ArrayList<GoodsListEntity.GoodsListBean> goodsListPage = new ArrayList<>();//请求的每一页的数据
    private boolean isRequest = true; //是否需要请求
    private boolean isRequesting = false; //是否在请求中

    private View view;//底部根视图
    private View headView;
    private ConvenientBanner convenientBanner;
    private NoScrollGridView noScrollGridView;
    private TextView tvMyself;
    private ImageView imgLsk, imgGanxibao, imgJijin;
    private NoScrollListView listTime;
    private GridLayoutManager layoutManager;
    private LoadingCaches aCache = LoadingCaches.getInstance();
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
                        Glide.with(getActivity()).resumeRequests();
                    }
                    //开始自动翻页
                    convenientBanner.startTurning(3000);
                    break;
            }
        }
    };
    private final int SETHEAD = 100;
    private NoScrollListView listRecommend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_all, container, false);
        initView(view);
        initListen();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initListTime();
        getGoodsList();
        handler.sendEmptyMessageDelayed(SETHEAD, 300);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
        handler.removeCallbacksAndMessages(SETHEAD);
        GlideCatchUtil.getInstance().clearCacheMemory();
        GlideCatchUtil.getInstance().clearCacheDiskSelf();
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

    //网络请求
    private void getGoodsList() {
        if (id.equals("-1")) {
            OkHttpUtils.get().url(Urls.GOODSLIST + "/p/" + page).build().execute(new GoodsListCallBack());
        }
    }

    public class ConvenientBannerCallBack extends Callback<CarcuseEntity> {
        @Override
        public CarcuseEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            convenientBannerEntities = new Gson().fromJson(string, new TypeToken<CarcuseEntity>() {
            }.getType());
            return convenientBannerEntities;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(CarcuseEntity response, int id) {
            //添加轮播图

            image.clear();
            ArrayList<CarcuseEntity.AdListBean> adList = response.adList;
            if (adList != null && adList.size() != 0) {
                int size = adList.size();
                for (int i = 0; i < size; i++) {
                    image.add(adList.get(i).adCode);
                }
            } else {
                return;
            }

            //初始化首页轮播图
            initCB();
        }
    }

    private void initCB() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, image)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_red})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    //轮播图适配图片
    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            GlideImage.loadImage(context,imageView,data,R.mipmap.img_error);
        }
    }

    //网络请求
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

    private void initView(View view) {
        id = getArguments().getString("id");
        if (id.equals("-1")) {
            OkHttpUtils.get().url(Urls.CAROUSE + "2").build().execute(new ConvenientBannerCallBack());
        }
        lvMallGoods = (XRecyclerView) view.findViewById(R.id.lv_mall_goods);


        //初始化头部
        initHeadView();

        adapter = new AllFragmentGoodsListAdapter(getActivity(), goodsListALL, onGetUrlListener);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvMallGoods.setLayoutManager(layoutManager);
        lvMallGoods.setHasFixedSize(true);
        lvMallGoods.addItemDecoration(new DividerGridItemDecoration(getActivity(), 15, R.drawable.divider));
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
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_fragement_all, null);
        convenientBanner = (ConvenientBanner) headView.findViewById(R.id.convenientBanner);
        noScrollGridView = (NoScrollGridView) headView.findViewById(R.id.gridView);
        tvMyself = (TextView) headView.findViewById(R.id.tv_myself);
        imgJijin = (ImageView) headView.findViewById(R.id.img_jijin);
        imgLsk = (ImageView) headView.findViewById(R.id.img_lsk);
        imgGanxibao = (ImageView) headView.findViewById(R.id.img_ganxibao);
        listTime = (NoScrollListView) headView.findViewById(R.id.list_time);
        listRecommend = (NoScrollListView) headView.findViewById(R.id.list_recommend);
        initHeadGridView();
        initMySelf();
        initListTime();
        initListRecommend();
    }

    //推荐产品
    private void initListRecommend() {
        OkHttpUtils.get().url(Urls.ALLFRAGMENTRECOMMEND).build().execute(new ListRecommendCallBack());
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
                        startActivity(new Intent(getActivity(), CommodityDetailsActiviy.class)
                                .putExtra("goodid", response.goodsList.get(position).goodsId));
                    }
                });
            }
        }
    }


    //限时抢购数据
    private void initListTime() {
        OkHttpUtils.get().url(Urls.ALLFRAGMENTHELISTTIME).build().execute(new ListTimeCallBack());
    }

    private class ListTimeCallBack extends Callback<AllFragmentListTimeEntity> {

        @Override
        public AllFragmentListTimeEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            AllFragmentListTimeEntity registerEntity = new Gson().fromJson(string, AllFragmentListTimeEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(final AllFragmentListTimeEntity response, int id) {
            if (response.errorCode.equals("000")) {
                AllFragmentHeadListTimeAdapter allFragmentHeadListTimeAdapter = new AllFragmentHeadListTimeAdapter(getActivity(), response.promotionList);
                listTime.setAdapter(allFragmentHeadListTimeAdapter);
            }
        }
    }

    //当代的我们接口
    private void initMySelf() {
        OkHttpUtils.get().url(Urls.ALLFRAGMENTHEMYSELF).build().execute(new MySelfCallBack());
    }

    //当代的我们接口
    private class MySelfCallBack extends Callback<AllFragmentMyselfEntity> {

        @Override
        public AllFragmentMyselfEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            AllFragmentMyselfEntity registerEntity = new Gson().fromJson(string, AllFragmentMyselfEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("网络请求错误");
        }

        @Override
        public void onResponse(final AllFragmentMyselfEntity response, int id) {
            if (response.errorCode.equals("000")) {
                tvMyself.setText(response.catInfo.catName);
                GlideImage.loadImage(getActivity(),imgJijin,response.adList.get(0).thumb,R.mipmap.img_error);
                imgJijin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (response.adList.get(0).articleType == 0) {
                            showToast("敬请期待...");
                        } else if (response.adList.get(0).articleType == 1) {
                            startActivity(new Intent(getActivity(), CellsActivitty.class).putExtra("id", response.adList.get(0).url));
                        } else if (response.adList.get(0).articleType == 2) {
                            startActivity(new Intent(getActivity(), MainImageActivity.class).putExtra("url", response.adList.get(0).url));
                        }
                    }
                });
                GlideImage.loadImage(getActivity(),imgLsk,response.adList.get(1).thumb,R.mipmap.img_error);
                imgLsk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (response.adList.get(1).articleType == 0) {
                            showToast("敬请期待...");
                        } else if (response.adList.get(1).articleType == 1) {
                            startActivity(new Intent(getActivity(), CellsActivitty.class).putExtra("id", response.adList.get(1).url));
                        } else if (response.adList.get(1).articleType == 2) {
                            startActivity(new Intent(getActivity(), MainImageActivity.class).putExtra("url", response.adList.get(1).url));
                        }
                    }
                });
                GlideImage.loadImage(getActivity(),imgGanxibao,response.adList.get(2).thumb,R.mipmap.img_error);
                imgGanxibao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (response.adList.get(2).articleType == 0) {
                            showToast("敬请期待...");
                        } else if (response.adList.get(2).articleType == 1) {
                            startActivity(new Intent(getActivity(), CellsActivitty.class).putExtra("id", response.adList.get(2).url));
                        } else if (response.adList.get(2).articleType == 2) {
                            startActivity(new Intent(getActivity(), MainImageActivity.class).putExtra("url", response.adList.get(2).url));
                        }
                    }
                });
            }
        }
    }

    //轮播下gridview
    private void initHeadGridView() {
        ArrayList<AllFragmentHeadViewEntity> allFragmentHeadViewEntityArrayList = new ArrayList<>();

        AllFragmentHeadViewEntity allFragmentHeadViewEntity = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity.setName("推荐有礼");
        allFragmentHeadViewEntity.setIconId(R.mipmap.all_fragment_head_tuijian);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity);

        AllFragmentHeadViewEntity allFragmentHeadViewEntity1 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity1.setName("领红包");
        allFragmentHeadViewEntity1.setIconId(R.mipmap.all_fragment_head_hongbao);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity1);
        AllFragmentHeadViewEntity allFragmentHeadViewEntity4 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity4.setName("积分大转盘");
        allFragmentHeadViewEntity4.setIconId(R.mipmap.all_fragment_head_duobao);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity4);
        AllFragmentHeadViewEntity allFragmentHeadViewEntity2 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity2.setName("业绩管理");
        allFragmentHeadViewEntity2.setIconId(R.mipmap.all_fragment_head_bendi);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity2);

        AllFragmentHeadViewEntity allFragmentHeadViewEntity3 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity3.setName("商家入驻");
        allFragmentHeadViewEntity3.setIconId(R.mipmap.all_fragment_head_shangjia);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity3);

        AllFragmentHeadGridViewAdapter allFragmentHeadGridViewAdapter = new AllFragmentHeadGridViewAdapter(getActivity(), allFragmentHeadViewEntityArrayList,onGetUrlListener);
        noScrollGridView.setAdapter(allFragmentHeadGridViewAdapter);
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
}
