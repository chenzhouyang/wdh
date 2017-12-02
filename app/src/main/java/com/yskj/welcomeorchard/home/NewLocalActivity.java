package com.yskj.welcomeorchard.home;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.networkbench.agent.impl.NBSAppAgent;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.NewLocalGoodsListAdapter;
import com.yskj.welcomeorchard.adapter.NewLocalHeadGridViewOneAdapter;
import com.yskj.welcomeorchard.adapter.NewLocalHeadGridViewTwoAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.VersionDialog;
import com.yskj.welcomeorchard.entity.AllFragmentHeadViewEntity;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.entity.NewLocalGoodsListEntity;
import com.yskj.welcomeorchard.entity.UpdateApp;
import com.yskj.welcomeorchard.entity.UpdateEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.receiver.UpdateInfoService;
import com.yskj.welcomeorchard.ui.advertising.ChooseDishActivity;
import com.yskj.welcomeorchard.ui.approve.ApproveActivity;
import com.yskj.welcomeorchard.ui.localserver.CityBusinessActivity;
import com.yskj.welcomeorchard.ui.localserver.LocalServerOrderActivity;
import com.yskj.welcomeorchard.ui.localserver.LookUpActivity;
import com.yskj.welcomeorchard.ui.merchant.MerChantActivity;
import com.yskj.welcomeorchard.ui.qrcode.QrCodeActivity;
import com.yskj.welcomeorchard.ui.redboxx.RedListActivity;
import com.yskj.welcomeorchard.ui.supply.SourcelistActivity;
import com.yskj.welcomeorchard.utils.ExampleUtil;
import com.yskj.welcomeorchard.utils.GlideCircleTransform;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Network;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/8/5on 15:08.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class NewLocalActivity extends BaseActivity implements View.OnClickListener {
    private ConvenientBanner convenientBanner;
    private ArrayList image = new ArrayList();
    private final int SETHEAD = 100;
    private XRecyclerView lvMallGoods;
    private ImageView ivShoppingCar, imgOrder, mainExessive;
    private TextView tvCity;
    private GridLayoutManager layoutManager;
    private boolean isaddheade = false;
    private UpdateApp info;
    //自定义推送消息展示
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final int UPDATE = 400;
    UpdateInfoService updateInfoService;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SETHEAD:
                    if (!isaddheade && !isDestroy) {
                        lvMallGoods.addHeaderView(headView);
                        adapter.notifyDataSetChanged();
                        isaddheade = true;
                        Glide.with(context).resumeRequests();
                        initData();
                    }
                    //开始自动翻页
                    if (convenientBanner != null) {
                        convenientBanner.startTurning(3000);
                    }
                    break;
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs = "null";
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
            }
        }
    };
    private ImageView ivReturn;
    private View headView;
    private NoScrollGridView gvOne, gvTwo;
    private ImageView imgDesOne;
    private TextView imgDesTwo;
    private ArrayList<NewLocalGoodsListEntity.DataBean.LocalShopsBean> goodsListALL = new ArrayList<>();
    private ArrayList<NewLocalGoodsListEntity.DataBean.LocalShopsBean> goodsListPage = new ArrayList<>();
    private NewLocalGoodsListAdapter adapter;
    private Intent intent;
    private static final int REQUESRCODE = 1001;
    private String cityId;
    private String tvAddress = "";
    private int offset = 0;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private boolean isRequesting = false; //是否在请求中
    private boolean isRequest = true; //是否需要请求
    private TextView etSearch;
    private LinearLayout llTitle;
    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationClient mLocationClient;
    int mDistance = 0;
    int maxDistance;//当距离在[0,最大值]变化时，透明度在[0,255之间变化]
    private int id;
    private boolean isFirstLoc;
    private String shopStatus = "-2";//商家入驻状态码
    private boolean isRealName = false;//判断是否实名认证

    static {
        System.loadLibrary("MyGame");
    }

    private boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NBSAppAgent.setLicenseKey("d3f6abd752274318b70e7bb07666fa0d").withLocationServiceEnabled(true).start(this.getApplicationContext());
        setContentView(R.layout.activity_new_local);
        initView();
        registerMessageReceiver();
        initLocation();
        initListen();
        updateInfoService = new UpdateInfoService(this);
        getUpDateInfo();
    }


    /**
     * 定位
     */
    private void initLocation() {
        isFirstLoc = true; // 每次进来定位一次
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (isFirstLoc) {
                isFirstLoc = false;
                //经纬度
                String lati = location.getLatitude() + "";
                String longa = location.getLongitude() + "";
                if (lati.equals("4.9E-324")) {
                    sp.edit().putString(Config.SPKEY_LATITUDE, "0").commit();
                    sp.edit().putString(Config.SPKEY_LONGITUDE, "0").commit();
                } else {
                    sp.edit().putString(Config.SPKEY_LATITUDE, lati).commit();
                    sp.edit().putString(Config.SPKEY_LONGITUDE, longa).commit();
                }

                sp.edit().putString(Config.SPKEY_CITYNAME, location.getCity()).commit();
            }
        }
    }

    private void initData() {
        if (sp.getString(Config.SPKEY_CITYID, "0").equals("0") || sp.getString(Config.SPKEY_CITY, "0").equals("0")) {
            showToast("请选择城市");
            intent = new Intent(NewLocalActivity.this, CityBusinessActivity.class);
            startActivityForResult(intent, REQUESRCODE);
            return;
        } else {
            tvCity.setText(sp.getString(Config.SPKEY_CITY, "许昌市"));
            cityId = sp.getString(Config.SPKEY_CITYID, "22655");
        }
        if (cityId == null || cityId.equals("")) {
            OkHttpUtils.get().url(Urls.NEWLOCALGOODSLIST).addParams("latitude", sp.getString(Config.SPKEY_LATITUDE, "0"))
                    .addParams("longitude", sp.getString(Config.SPKEY_LONGITUDE, "0")).addParams("offset", offset * 10 + "").build().execute(new NewLocalGoodsListCallBack());
        } else {
            getGoodsList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!caches.get("access_token").equals("null")) {
            getuserinfo();
        }
        handler.sendEmptyMessageDelayed(SETHEAD, 300);
    }

    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int ids) {
                caches.put("userinfo", response);
                sp.edit().putString("userinfo", response).commit();
                userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                }.getType());
                id = userInfoEntity.data.userVo.id;
                shopStatus = userInfoEntity.data.shopStatus + "";
                if (userInfoEntity.data.realnameVo != null) {
                    isRealName = true;
                }
                caches.put("spreadCode", userInfoEntity.data.userVo.spreadCode + "");
                //设置别名

                String alias = id + "";
                if (TextUtils.isEmpty(alias)) {
                    return;
                }
                if (!ExampleUtil.isValidTagAndAlias(alias)) {
                    return;
                }
                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
            }
        });
    }


    private void getGoodsList() {
        lvMallGoods.refreshComplete();
        OkHttpUtils.get().url(Urls.NEWLOCALGOODSLIST).addParams("areaId", cityId).addParams("offset", offset * 10 + "")
                .build().execute(new NewLocalGoodsListCallBack());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (convenientBanner != null) {
            convenientBanner.stopTurning();
        }
        handler.removeCallbacksAndMessages(SETHEAD);
        myListener = null;
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        mLocationClient.stop();
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    /**
     * 设置标题栏背景透明度
     *
     * @param alpha 透明度
     */
    private void setSystemBarAlpha(int alpha) {
        if (alpha >= 255) {
            llTitle.setBackgroundColor(Color.WHITE);
        } else {
            //标题栏渐变。a:alpha透明度 r:红 g：绿 b蓝
            llTitle.setBackgroundColor(Color.argb(alpha, 255, 255, 255));//透明效果是由参数1决定的，透明范围[0,255]
        }

    }

    private void initListen() {
        lvMallGoods.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lvMallGoods.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                getGoodsList();
            }

            @Override
            public void onLoadMore() {
                offset++;
                getGoodsList();
            }
        });

        lvMallGoods.setLoadingMoreEnabled(false);
        lvMallGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                maxDistance = convenientBanner.getBottom() - llTitle.getHeight();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                mDistance += dy;
                float percent = mDistance * 1f / maxDistance;//百分比
                int alpha = (int) (percent * 255);
                Drawable dwDrawable = null;
                Drawable searchDrawable = null;
                if (alpha >= 125) {
                    dwDrawable = getResources().getDrawable(R.mipmap.img_new_local_dw_gray);
                    searchDrawable = getResources().getDrawable(R.mipmap.img_new_local_search_gray);
                    dwDrawable.setBounds(0, 0, dwDrawable.getMinimumWidth(), dwDrawable.getMinimumHeight());
                    searchDrawable.setBounds(0, 0, 39, 39);
                    tvCity.setCompoundDrawables(dwDrawable, null, null, null);
                    tvCity.setTextColor(getResources().getColor(R.color.gray));
                    etSearch.setCompoundDrawables(searchDrawable, null, null, null);
                    etSearch.setBackgroundResource(R.drawable.round_corner_999999);
                    imgOrder.setImageResource(R.mipmap.img_new_local_order_gray);
                } else {
                    dwDrawable = getResources().getDrawable(R.mipmap.img_new_local_dw);
                    searchDrawable = getResources().getDrawable(R.mipmap.img_new_local_search);
                    dwDrawable.setBounds(0, 0, dwDrawable.getMinimumWidth(), dwDrawable.getMinimumHeight());
                    searchDrawable.setBounds(0, 0, 39, 39);
                    tvCity.setCompoundDrawables(dwDrawable, null, null, null);
                    tvCity.setTextColor(getResources().getColor(R.color.white));
                    etSearch.setCompoundDrawables(searchDrawable, null, null, null);
                    etSearch.setBackgroundResource(R.drawable.round_corner_f27a6b);
                    imgOrder.setImageResource(R.mipmap.img_new_local_order);
                }
                setSystemBarAlpha(alpha);
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
                        offset++;
                        getGoodsList();
                    }
                }
            }
        });
        ivReturn.setOnClickListener(this);

        try {
            adapter = new NewLocalGoodsListAdapter(context, goodsListALL);
            layoutManager = new GridLayoutManager(context, 1);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            lvMallGoods.setLayoutManager(layoutManager);
            lvMallGoods.setHasFixedSize(true);
            adapter.setOnItemClickListener(new NewLocalGoodsListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    startActivity(new Intent(context, ChooseDishActivity.class)
                            .putExtra("shopid", goodsListALL.get(position).shopId + "")
                            .putExtra("address", goodsListALL.get(position).address)
                            .putExtra("distance", goodsListALL.get(position).distanceString));
                }
            });
            lvMallGoods.setAdapter(adapter);
        } catch (IllegalArgumentException e) {
            e.getStackTrace();
        }

    }

    private class NewLocalGoodsListCallBack extends Callback<NewLocalGoodsListEntity> {

        @Override
        public NewLocalGoodsListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            NewLocalGoodsListEntity newLocalGoodsListEntity = new Gson().fromJson(string, NewLocalGoodsListEntity.class);
            return newLocalGoodsListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("服务器忙。。。");
        }

        @Override
        public void onResponse(NewLocalGoodsListEntity response, int id) {
            if (offset == 0) {
                goodsListALL.clear();
            }
            //当数据加载完后将isRequesting 是否在加载中 设置为false
            isRequesting = false;//加载完成后初始化
            //每次清空上页数据
            goodsListPage.clear();
            if (response.code == 0) {
                goodsListPage = response.data.localShops;
                goodsListALL.addAll(goodsListPage);
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void initView() {
        lvMallGoods = (XRecyclerView) findViewById(R.id.lv_mall_goods);
        ivReturn = (ImageView) findViewById(R.id.iv_return);
        ivShoppingCar = (ImageView) findViewById(R.id.iv_shopping_car);
        etSearch = (TextView) findViewById(R.id.et_search);
        ivShoppingCar.setVisibility(View.GONE);
        tvCity = (TextView) findViewById(R.id.tv_city);
        imgOrder = (ImageView) findViewById(R.id.img_order);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        setSystemBarAlpha(0);
        ivReturn.setOnClickListener(this);
        ivShoppingCar.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        imgOrder.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        //初始化头部
        initHeadView();
    }

    private void initHeadView() {
        headView = LayoutInflater.from(context).inflate(R.layout.head_new_local, null);
        convenientBanner = (ConvenientBanner) headView.findViewById(R.id.convenientBanner);
        mainExessive = (ImageView) headView.findViewById(R.id.main_exessive);
        gvOne = (NoScrollGridView) headView.findViewById(R.id.gv_one);
        gvTwo = (NoScrollGridView) headView.findViewById(R.id.gv_two);
        imgDesOne = (ImageView) headView.findViewById(R.id.img_des_one);
        imgDesTwo = (TextView) headView.findViewById(R.id.img_des_two);
        mainExessive.setOnClickListener(this);
        Glide.with(context).load(R.mipmap.main_excessiver_image).transform(new CenterCrop(context), new GlideCircleTransform(context, 6)).into(mainExessive);
        //初始化首页轮播图
        initCB();
        initHeadGridViewOne();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stopMyDialog();
        if (requestCode == REQUESRCODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    tvAddress = bundle.getString("name");
                    cityId = bundle.getString("cityId");
                    sp.edit().putString(Config.SPKEY_CITY, tvAddress).commit();
                    sp.edit().putString(Config.SPKEY_CITYID, cityId).commit();
                    tvCity.setText(tvAddress);
                    goodsListALL.clear();
                    offset = 0;
                    getGoodsList();
                }
            }
        }
    }

    private void initHeadGridViewOne() {
        final ArrayList<AllFragmentHeadViewEntity> allFragmentHeadViewEntityArrayList = new ArrayList<>();
        AllFragmentHeadViewEntity allFragmentHeadViewEntity2 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity2.setName("商家入驻");
        allFragmentHeadViewEntity2.setIconId(R.mipmap.img_new_local_gv_one4);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity2);

        AllFragmentHeadViewEntity allFragmentHeadViewEntity1 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity1.setName("领取红包");
        allFragmentHeadViewEntity1.setIconId(R.mipmap.img_new_local_gv_one2);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity1);


        AllFragmentHeadViewEntity allFragmentHeadViewEntity = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity.setName("厂家直供");
        allFragmentHeadViewEntity.setIconId(R.mipmap.img_new_local_gv_one1);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity);


        AllFragmentHeadViewEntity allFragmentHeadViewEntity4 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity4.setName("分享有礼");
        allFragmentHeadViewEntity4.setIconId(R.mipmap.img_new_local_gv_one3);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity4);


        AllFragmentHeadViewEntity allFragmentHeadViewEntity3 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity3.setName("同城酒水");
        allFragmentHeadViewEntity3.setIconId(R.mipmap.img_new_local_gv_one5);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity3);


        NewLocalHeadGridViewOneAdapter newLocalHeadGridViewOneAdapter = new NewLocalHeadGridViewOneAdapter(context, allFragmentHeadViewEntityArrayList);
        gvOne.setAdapter(newLocalHeadGridViewOneAdapter);
        final ArrayList arrayList = new ArrayList();
        arrayList.add(R.mipmap.img_new_local_gv_two1);
        arrayList.add(R.mipmap.img_new_local_gv_two2);
        arrayList.add(R.mipmap.img_new_local_gv_two3);
        arrayList.add(R.mipmap.img_new_local_gv_two4);
        NewLocalHeadGridViewTwoAdapter newLocalHeadGridViewTwoAdapter = new NewLocalHeadGridViewTwoAdapter(context, arrayList);
        newLocalHeadGridViewOneAdapter.setOnItemClickListener(new NewLocalHeadGridViewOneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        judge(shopStatus);//商家入驻
                        break;
                    case 1:
                        startActivity(new Intent(context, RedListActivity.class));//领取红包
                        break;
                    case 2:
                        startActivity(new Intent(context, SourcelistActivity.class));//厂家直供
                        break;
                    case 3:
                        //分享有礼
                        //我的
                        if (caches.get("access_token").equals("null")) {
                            goHome();
                        } else {
                            startActivity(new Intent(context, QrCodeActivity.class));
                        }
                        break;
                    case 4:
                        //同城酒水
                        showToast("敬请期待...");
                        break;
                }
            }
        });
        gvTwo.setAdapter(newLocalHeadGridViewTwoAdapter);

    }

    private void initCB() {
        image.add(R.mipmap.banner_one);
        image.add(R.mipmap.banner_two);
        image.add(R.mipmap.banner_three);
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

    /**
     * 自动登录
     */
    public void goHome() {
        if (Network.isNetworkAvailable(this)) {
            SharedPreferences share2 = getSharedPreferences("mobile", 0);
            String mobile = share2.getString("mobile", "null");
            SharedPreferences share = getSharedPreferences("password", 0);
            String password = share.getString("password", "null");
            if (mobile.equals("null") || password.equals("null")) {
                startActivity(new Intent(NewLocalActivity.this, LoginActivity.class));
            } else {
                OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                        .addHeader("Authorization", Ips.AUTHORIZATION)
                        .addParams("username", mobile)
                        .addParams("password", password)
                        .addParams("grant_type", "password").build()
                        .execute(new LoginCallBack());
            }
        }
    }

    private class LoginCallBack extends Callback<LoginEntity> {
        @Override
        public LoginEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginEntity loginEntity = new Gson().fromJson(s, new TypeToken<LoginEntity>() {
            }.getType());
            return loginEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (e == null || e.getMessage() == null) {
                return;
            }
            if (e.getMessage().contains("403") || e.getMessage().contains("400")) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            caches.put("access_token", Config.TOKENHEADER + response.accessToken);
            caches.put("php_token", response.accessToken);
            caches.put("refresh_token", response.refreshToken);
            caches.put("token_type", response.tokenType);
            caches.put("expires_in", response.expiresIn + "");
            caches.put("scope", response.scope);
            caches.put("token", response.accessToken);
            caches.put("page", "0");
            sp.edit().putString("access_token", Config.TOKENHEADER + response.accessToken).commit();
            sp.edit().putString("refresh_token", response.refreshToken).commit();
            sp.edit().putString("page", "0").commit();
            getUserInfo();
        }
    }

    private void getUserInfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int ids) {
                caches.put("userinfo", response);
                sp.edit().putString("userinfo", response).commit();
                startActivity(new Intent(context, QrCodeActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 返回顶部
             */
            case R.id.iv_return:
                lvMallGoods.scrollToPosition(0);
                setSystemBarAlpha(0);
                mDistance = 0;
                break;
            case R.id.tv_city:
                intent = new Intent(NewLocalActivity.this, CityBusinessActivity.class);
                startActivityForResult(intent, REQUESRCODE);
                break;
            case R.id.img_order:
                if (caches.get("access_token").equals("null")) {
                    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                            .setMessage("您还没有登录，是否登录？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    startActivity(new Intent(context, LoginActivity.class));
                                }
                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框

                } else {
                    startActivity(new Intent(context, LocalServerOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                break;
            case R.id.iv_shopping_car:
                //startActivity(new Intent(context, ShoppingCartActivity.class));
                break;
            case R.id.et_search:
                intent = new Intent(context, LookUpActivity.class);
                intent.putExtra("cityid", cityId + "");
                startActivity(intent);
                break;
            case R.id.main_exessive:
                startActivity(new Intent(context, ExessiveActivity.class));
                break;
        }
    }


    //轮播图适配图片
    public class NetworkImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
//            GlideImage.loadImage(context,imageView,data,R.mipmap.img_error);
        }
    }


    //显示是否要更新的对话框
    private void showUpdateDialog( UpdateEntity updateEntity) {
        final VersionDialog.Builder builder = new VersionDialog.Builder(NewLocalActivity.this);
        builder.setTitle("版本更新");
        builder.setDetailMessage(updateEntity.desc);
        final String url = updateEntity.url;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateInfoService.downLoadFile(url);
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 更新
     */
    public void getUpDateInfo() {
        OkHttpUtils.get().url(Urls.UPDATEAPP).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                UpdateEntity updateEntity = new Gson().fromJson(response, UpdateEntity.class);
                if (updateEntity.isUpdate == 0) {
                    Logger.d(updateEntity.url);
                    PackageManager packageManager = context.getPackageManager();
                    PackageInfo packageInfo = null;
                    try {
                        packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                        int oldVersion = packageInfo.versionCode;
                        if (updateEntity.vcode > oldVersion) {
                            showUpdateDialog(updateEntity);
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    /**
     * 判断商家入驻
     *
     * @param stutas 入驻状态码0、待审批  1、审批通过  2、审批失败  3、冻结  -1未提交信息
     */

    public void judge(String stutas) {
        if (!isRealName) {
            new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("商家入驻需先进行实名认证，您还未进行实名认证是否前往实名认证？")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            startActivity(new Intent(context, ApproveActivity.class).putExtra("type", "5"));
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        }
        if (stutas.equals("0")) {
            new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("待审批")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("1")) {
            try {
                SharedPreferences share2 = getSharedPreferences("mobile", 0);
                String mobile = share2.getString("mobile", "null");
                SharedPreferences share = getSharedPreferences("password", 0);
                String password = share.getString("password", "null");
                ComponentName componentName = new ComponentName("com.yskj.wdh", "com.yskj.wdh.start.SplashActivity");
                Intent intent = new Intent();
                //  Intent intent = new Intent("chroya.foo");
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("password", password);
                intent.putExtras(bundle);
                intent.setComponent(componentName);
                startActivity(intent);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("请下载商家版app")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                Intent viewIntent = new
                                        Intent("android.intent.action.VIEW", Uri.parse(Ips.MALL));
                                startActivity(viewIntent);
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        } else if (stutas.equals("2")) {
            new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("审批失败，请修改信息")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            Intent intent = new Intent(NewLocalActivity.this, MerChantActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("where", 2);
                            startActivityForResult(intent, 102);
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("3")) {
            new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("店家已冻结，请联系管理员")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("-1")) {
            new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("您还未提交申请信息，是否提交信息")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            Intent intent = new Intent(NewLocalActivity.this, MerChantActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("where", 3);
                            startActivityForResult(intent, 101);
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("-2")) {
            new AlertDialog.Builder(NewLocalActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("是否刷新界面？")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            if (!caches.get("access_token").equals("null")) {
                                getuserinfo();
                            }
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        }
    }
}
