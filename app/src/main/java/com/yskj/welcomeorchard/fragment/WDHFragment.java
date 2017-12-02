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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AllFragmentGoodsListAdapter;
import com.yskj.welcomeorchard.adapter.AllFragmentListRecommendAdapter;
import com.yskj.welcomeorchard.adapter.WDHFragmentHoriListAdapter;
import com.yskj.welcomeorchard.adapter.WDHFragmentNewPeopleAdapter;
import com.yskj.welcomeorchard.base.BaseFragment;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AllFragmentListRecommendEntity;
import com.yskj.welcomeorchard.entity.GoodsListEntity;
import com.yskj.welcomeorchard.entity.SupentEntity;
import com.yskj.welcomeorchard.entity.WDHFragmentNewPeopleEntity;
import com.yskj.welcomeorchard.entity.WDHFragmentTopRecycleEntity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.utils.GlideCatchUtil;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/4/14on 11:36.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class WDHFragment extends BaseFragment implements View.OnClickListener{
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
    private RelativeLayout root;
    private ImageView btnRecommend;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private float startX = 0;
    private float startY = 0;
    private boolean isaddheade = false;
    private SupentEntity supentEntity;
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
    private NoScrollGridView gvBottom;
    private RecyclerView gvTop;
    private TextView tvNewPeople,tvWdh;
    private GridLayoutManager layoutManager;
    private GridLayoutManager horilayoutManager;
    private String firstPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wdh, container, false);
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
        GlideCatchUtil.getInstance().clearCacheMemory();
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

        root = (RelativeLayout) view.findViewById(R.id.root);
        btnRecommend = (ImageView) view.findViewById(R.id.btn_recommend);
        // 获取屏幕大小
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels-450;

        btnRecommend.setOnTouchListener(onDragTouchListener);
        btnRecommend.setOnClickListener(this);

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
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.head_fragment_wdh, null);
        imgTop = (ImageView) headView.findViewById(R.id.img_top);
        gvTop = (RecyclerView) headView.findViewById(R.id.recycler_view);
        gvBottom = (NoScrollGridView) headView.findViewById(R.id.gvBottom);
        tvNewPeople = (TextView) headView.findViewById(R.id.tv_new_people);
        tvWdh = (TextView) headView.findViewById(R.id.tv_wdh);
        listRecommend = (NoScrollListView) headView.findViewById(R.id.list_recommend);
        initHeadTopRecycleView();
        intitsuspend();
        initNewPeople();
        initListRecommend();
    }
    ////唯多惠出品fragment 分类接口
    private void initHeadTopRecycleView() {
        //测试id先用1
        OkHttpUtils.get().url(Urls.WDGFRAGMAENTGOODCATEGORY+id).build().execute(new GoodsCategoryCallBack());
    }
    private class GoodsCategoryCallBack extends Callback<WDHFragmentTopRecycleEntity>{

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
        public void onResponse(WDHFragmentTopRecycleEntity response, int id) {
            if (response.errorCode.equals("000")){
                final WDHFragmentTopRecycleEntity.GoodsCategoryTreeBean goodsCategoryTreeBean = response.goodsCategoryTree.get(0);
                GlideImage.loadImage(getActivity(),imgTop,goodsCategoryTreeBean.image,R.mipmap.img_error);
                if (goodsCategoryTreeBean.tmenu.size()<3){
                    gvTop.setVisibility(View.GONE);
                }else {
                    gvTop.setVisibility(View.VISIBLE);
                    WDHFragmentHoriListAdapter adapter = new WDHFragmentHoriListAdapter(getActivity(),goodsCategoryTreeBean.tmenu);
                    gvTop.setHasFixedSize(true);
                    horilayoutManager =  new GridLayoutManager(getActivity(), 3);
                    horilayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    gvTop.setLayoutManager(horilayoutManager);
                    gvTop.setAdapter(adapter);
                    adapter.setOnItemClickListener(new WDHFragmentHoriListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                                onGetUrlListener.getTwoPosition(Integer.parseInt(firstPos),position);
                        }
                    });
                }
            }
        }
    }
    //设置悬浮按钮
    private void intitsuspend(){
        OkHttpUtils.get().url(Urls.SUSPENT).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("网络请求错误");
            }

            @Override
            public void onResponse(String response, int id) {
                supentEntity = new Gson().fromJson(response,new TypeToken<SupentEntity>(){}.getType());
                if(supentEntity.errorCode.equals("000")){
                    GlideImage.loadImage(getActivity(),btnRecommend,supentEntity.adList.get(0).adCode,R.mipmap.img_error);
                }else {
                    btnRecommend.setVisibility(View.GONE);
                }
            }
        });
    }
    //新人专属网络请求
    private void initNewPeople() {
        OkHttpUtils.get().url(Urls.WDGFRAGMAENTNEWPEOPLE).build().execute(new NewPeopleCallBack());
    }
    //新人专属网络请求
    private class NewPeopleCallBack extends Callback<WDHFragmentNewPeopleEntity>{

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
                tvNewPeople.setVisibility(View.VISIBLE);
                if(response.positionInfo!=null){
                    tvNewPeople.setText(response.positionInfo.positionName);
                }else {
                    tvNewPeople.setText("专属推荐");
                }

                WDHFragmentNewPeopleAdapter adapter = new WDHFragmentNewPeopleAdapter(getActivity(),response.adList);
                gvBottom.setAdapter(adapter);
                gvBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("from","WDHFragment");
                        bundle.putString("adType",response.adList.get(position).adType+"");
                        bundle.putString("adLink",response.adList.get(position).adLink);
                        onGetUrlListener.getBandle(bundle);
                    }
                });
            }else {
                tvNewPeople.setVisibility(View.GONE);
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

            case R.id.btn_recommend:
                if(supentEntity!=null){
                    startActivity(new Intent(getActivity(),CommodityDetailsActiviy.class)
                            .putExtra("goodid",supentEntity.adList.get(0).adLink));
                }

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
    float startdx = 0,startdy = 0;
    View.OnTouchListener onDragTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            boolean isMove = false;
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    startX = event.getRawX();
                    startY = event.getRawY();
                    startdx = event.getRawX();
                    startdy = event.getRawY();
                    break;
                }
                case MotionEvent.ACTION_MOVE:
                {
                    RelativeLayout.LayoutParams layoutParams  = (RelativeLayout.LayoutParams) v.getLayoutParams();
                    // 计算偏移量
                    int dx = (int) (event.getRawX() - startX);
                    int dy = (int) (event.getRawY() - startY);
                    // 计算控件的区域
                    int left = v.getLeft() + dx;
                    int right = v.getRight() + dx;
                    int top = v.getTop() + dy;
                    int bottom = v.getBottom() + dy;
                    // 超出屏幕检测
                    if (left < 0)
                    {
                        left = 0;
                        right = v.getWidth();
                    }
                    if (right > screenWidth)
                    {
                        right = screenWidth;
                        left = screenWidth - v.getWidth();
                    }
                    if (top < 0)
                    {
                        top = 0;
                        bottom = v.getHeight();
                    }
                    if (bottom > screenHeight)
                    {
                        bottom = screenHeight;
                        top = screenHeight - v.getHeight();
                    }
                    layoutParams.leftMargin = left;
                    layoutParams.topMargin = top;
                    v.setLayoutParams(layoutParams);
                    startX = event.getRawX();
                    startY = event.getRawY();
                    break;
                }
                case MotionEvent.ACTION_UP:
                    if (Math.abs(event.getRawX() - startdx)<=5&&Math.abs(event.getRawY() - startdy)<=5){
                        isMove = false;
                    }else {
                        isMove = true;
                    }
                    startdx = startX;
                    startdy = startY;
                    break;
            }
            root.invalidate();
            return isMove;
        }
    };
}
