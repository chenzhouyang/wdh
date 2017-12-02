package com.yskj.welcomeorchard.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.networkbench.agent.impl.NBSAppAgent;
import com.umeng.socialize.UMShareAPI;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.PageFragmentAdapter;
import com.yskj.welcomeorchard.base.BaseFragmentActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.HintLoginDialog;
import com.yskj.welcomeorchard.dialog.VersionDialog;
import com.yskj.welcomeorchard.entity.GoodsCategoryListEntity;
import com.yskj.welcomeorchard.entity.MainDialogEntiity;
import com.yskj.welcomeorchard.entity.PrizeListEntity;
import com.yskj.welcomeorchard.entity.UpdateApp;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.fragment.AllFragment;
import com.yskj.welcomeorchard.fragment.BeautyMakeupFragment;
import com.yskj.welcomeorchard.fragment.FragmentInterface;
import com.yskj.welcomeorchard.fragment.HomeTextileFragment;
import com.yskj.welcomeorchard.fragment.WDHFragment;
import com.yskj.welcomeorchard.receiver.UpdateInfoService;
import com.yskj.welcomeorchard.ui.buyersShow.BuyersShowListActivity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.ui.luckpan.LuckPanActivity;
import com.yskj.welcomeorchard.ui.merchant.MerChantActivity;
import com.yskj.welcomeorchard.ui.payment.PayMentActivity;
import com.yskj.welcomeorchard.ui.performance.PerformanceActivity;
import com.yskj.welcomeorchard.ui.push.PushMessageActivity;
import com.yskj.welcomeorchard.ui.qrcode.QrCodeActivity;
import com.yskj.welcomeorchard.ui.redboxx.RedListActivity;
import com.yskj.welcomeorchard.ui.store.GoodsCategoryActivity;
import com.yskj.welcomeorchard.ui.store.GoodsCategoryDetailActivity;
import com.yskj.welcomeorchard.ui.web.MainImageActivity;
import com.yskj.welcomeorchard.utils.ExampleUtil;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, FragmentInterface.OnGetUrlListener {

    @Bind(R.id.rgTab)
    RadioGroup rgTab;
    @Bind(R.id.hvTab)
    HorizontalScrollView hvTab;
    @Bind(R.id.vpList)
    ViewPager viewPager;
    @Bind(R.id.img_kinds)
    ImageView imgKinds;
    @Bind(R.id.message_image)
    ImageView messageImage;
    @Bind(R.id.img_scan)
    ImageView imgScan;
    private PageFragmentAdapter adapter = null;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private GoodsCategoryListEntity registerEntity;
    public SharedPreferences sp;
    private int tabPage = 0;
    private static final int POPWIN = 300;
    private String urlimage, intentimage;
    private MainDialogEntiity mainDialogEntiity;
    private ProgressDialog progressDialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case POPWIN:
                    showPopMenu();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private UserInfoEntity userInfoEntity;



    public static boolean isForeground = false;
    private RadioButton rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }





    private void initView() {
        sp = getSharedPreferences("UserInfor", 0);
        sp.edit().putInt("tabPage", 0).commit();
        imgKinds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GoodsCategoryActivity.class));
            }
        });
        messageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caches.get("access_token").equals("null")) {
                    HintLoginDialog dialog = new HintLoginDialog(MainActivity.this);
                    dialog.show();
                } else {
                    startActivity(new Intent(getApplicationContext(), PushMessageActivity.class));
                }

            }
        });
        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caches.get("access_token").equals("null")) {
                    HintLoginDialog dialog = new HintLoginDialog(MainActivity.this);
                    dialog.show();
                } else {
                    startActivity(new Intent(getApplicationContext(), PayMentActivity.class));
                }

            }
        });
        initData();
        rgTab.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        viewPager.setCurrentItem(checkedId);
                    }
                });

        viewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void initData() {
        OkHttpUtils.get().url(Urls.GOODSCATEGORYLIST).build().execute(new GoodsCategotyListCallBack());
    }

    private void initTab(GoodsCategoryListEntity response) {
        if (response.errorCode.equals("000")) {
            List<String> channelList = new ArrayList<>();
            channelList.add("全部");
            for (int i = 0; i < response.goodsCategoryTree.size(); i++) {
                if (null != response.goodsCategoryTree.get(i).mobileName) {
                    channelList.add(response.goodsCategoryTree.get(i).mobileName);
                }
            }
            for (int i = 0; i < channelList.size(); i++) {
                RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.tab_rb, null);
                if (i == 0) {
                    rb.setTextSize(15);
                } else {
                    rb.setTextSize(13);
                }
                rb.setId(i);
                rb.setText(channelList.get(i));
                if (null != rb){
                    rgTab.addView(rb);
                }
            }
            rgTab.check(0);
            initViewPager(response);
        }

    }

    @Override
    public void getUrl(String id) {
        if (id != null) {
            Intent intent = new Intent(MainActivity.this, CommodityDetailsActiviy.class);
            intent.putExtra("goodid", id);
            startActivity(intent);
        }
    }

    @Override
    public void getData(String goodsId) {
        if (goodsId != null) {
            Intent intent = new Intent(MainActivity.this, BuyersShowListActivity.class);
            intent.putExtra("goodsId", goodsId);
            startActivity(intent);
        }
    }

    //用于tab选择位置时回掉
    @Override
    public void getfirstPosition(int firstPosition) {
    }

    @Override
    public void getTwoPosition(int firstPosition, int twoPosition) {
        if (twoPosition == 100) {
            startActivity(new Intent(MainActivity.this, GoodsCategoryActivity.class));
        } else {
            Intent intent = new Intent(MainActivity.this, GoodsCategoryDetailActivity.class);
            intent.putExtra("firstPosition", firstPosition);
            intent.putExtra("twoPosition", twoPosition);
            startActivity(intent);
        }
    }

    @Override
    public void getBandle(Bundle bundle) {
        switch (bundle.getString("from")) {
            case "allFragment":
                if (caches.get("access_token").equals("null")) {
                    HintLoginDialog dialog = new HintLoginDialog(MainActivity.this);
                    dialog.show();
                } else {
                    switch (bundle.getString("position")) {
                        case "0":
                            startActivity(new Intent(MainActivity.this, QrCodeActivity.class));
                            break;
                        case "1":
                            startActivity(new Intent(MainActivity.this, RedListActivity.class));
                            break;
                        case "2":
                            OkHttpUtils.get().url(Urls.PRIZELIST).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    PrizeListEntity prizeListEntity = new Gson().fromJson(response, new TypeToken<PrizeListEntity>() {
                                    }.getType());
                                    if (prizeListEntity.errorCode.equals("000")) {
                                        if (prizeListEntity.list.size() == 6) {
                                            startActivity(new Intent(MainActivity.this, LuckPanActivity.class));
                                        } else {
                                            showToast("敬请期待...");
                                        }
                                    } else {
                                        showToast("敬请期待...");
                                    }

                                }
                            });

                            break;
                        case "3":
                            startActivity(new Intent(MainActivity.this, PerformanceActivity.class));
                            break;
                        case "4":
//                            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
//                            }.getType());
//                            judge(userInfoEntity.data.shopStatus + "");
                            break;
                    }
                }
                break;
            case "WDHFragment":
                //type 0:不跳  1：商品 2：文章  3：其他
                switch (bundle.getString("adType")) {
                    case "0":
                        showToast("敬请期待...");
                        break;
                    case "1":
                        startActivity(new Intent(MainActivity.this, CommodityDetailsActiviy.class).putExtra("goodid", bundle.getString("adLink")));
                        break;
                    case "2":
                        startActivity(new Intent(MainActivity.this, MainImageActivity.class).putExtra("url", bundle.getString("adLink")));
                        break;
                }
                break;
        }
    }

    //首页Tab列表分类接口
    public class GoodsCategotyListCallBack extends Callback<GoodsCategoryListEntity> {
        @Override
        public GoodsCategoryListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            registerEntity = new Gson().fromJson(string, GoodsCategoryListEntity.class);
            return registerEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(GoodsCategoryListEntity response, int id) {
            if (response.errorCode.equals("000")) {
                initTab(response);
            }
        }
    }

    private void initViewPager(GoodsCategoryListEntity response) {
        AllFragment allmainFragment = new AllFragment();
        Bundle allbundle = new Bundle();
        allbundle.putString("id", "-1");
        allmainFragment.setArguments(allbundle);
        fragmentList.add(allmainFragment);
        for (int i = 0; i < response.goodsCategoryTree.size(); i++) {
            switch (response.goodsCategoryTree.get(i).catGroup) {
                //0默认 1唯多惠出品 2家纺 3个护美妆
                case "0":
                    BeautyMakeupFragment beautyMakeupFragment0 = new BeautyMakeupFragment();
                    Bundle bundle0 = new Bundle();
                    bundle0.putString("id", response.goodsCategoryTree.get(i).id);
                    bundle0.putString("position", i + "");
                    beautyMakeupFragment0.setArguments(bundle0);
                    fragmentList.add(beautyMakeupFragment0);
                    break;
                case "1":
                    WDHFragment wdhFragment = new WDHFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", response.goodsCategoryTree.get(i).id);
                    bundle1.putString("position", i + "");
                    wdhFragment.setArguments(bundle1);
                    fragmentList.add(wdhFragment);
                    break;
                case "2":
                    HomeTextileFragment homeTextileFragment = new HomeTextileFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("id", response.goodsCategoryTree.get(i).id);
                    bundle2.putString("position", i + "");
                    homeTextileFragment.setArguments(bundle2);
                    fragmentList.add(homeTextileFragment);
                    break;
                case "3":
                    BeautyMakeupFragment beautyMakeupFragment = new BeautyMakeupFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("id", response.goodsCategoryTree.get(i).id);
                    bundle3.putString("position", i + "");
                    beautyMakeupFragment.setArguments(bundle3);
                    fragmentList.add(beautyMakeupFragment);
                    break;
            }

        }
        adapter = new PageFragmentAdapter(super.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
    }

    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     *
     * @param idx
     */
    private void setTab(int idx) {
        int size = rgTab.getChildCount();
        for (int i = 0; i < size; i++) {
            if (i == idx) {
                RadioButton rb = (RadioButton) rgTab.getChildAt(idx);
                rb.setChecked(true);
                rb.setTextSize(15);
                int left = rb.getLeft();
                int width = rb.getMeasuredWidth();
                DisplayMetrics metrics = new DisplayMetrics();
                super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int screenWidth = metrics.widthPixels;
                int len = left + width / 2 - screenWidth / 2;
                hvTab.smoothScrollTo(len, 0);//滑动ScroollView
            } else {
                RadioButton rb = (RadioButton) rgTab.getChildAt(i);
                rb.setTextSize(13);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            //正在滑动   pager处于正在拖拽中
            Glide.with(getApplicationContext()).pauseRequests();
        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
            Glide.with(getApplicationContext()).pauseRequests();
            //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            Glide.with(getApplicationContext()).resumeRequests();
            //空闲状态  pager处于空闲状态
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        setTab(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }

    @Override
    protected void onResume() {
        isForeground = true;
        if (sp.getInt("tabPage", 0) != 0) {
            rgTab.check(sp.getInt("tabPage", 0));
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        handler.removeCallbacksAndMessages(POPWIN);
        super.onPause();
    }



    //获取弹出框
    public void getwindow(String id) {
        OkHttpUtils.get().url(Urls.FIRSTWINDOWS + "/uid/" + id).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = (String) map.get("error_code");
                if (code.equals("000")) {
                    mainDialogEntiity = new Gson().fromJson(response, new TypeToken<MainDialogEntiity>() {
                    }.getType());
                    if (mainDialogEntiity.error_code == 000) {
                        urlimage = Ips.PHPURL + mainDialogEntiity.newsInfo.thumb;
                        intentimage = Ips.PHPURL + "/" + mainDialogEntiity.newsUrl;
                        handler.sendEmptyMessage(POPWIN);
                    }
                }


            }
        });
    }

    private void showPopMenu() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.add_dialogs);
        View view = View.inflate(getParent(), R.layout.dialog_message, null);
        ImageView dialog_quxiao = (ImageView) view.findViewById(R.id.dialog_quxiao);
        ImageView dialog_image = (ImageView) view.findViewById(R.id.dialog_image);
        GlideImage.loadImage(MainActivity.this, dialog_image, urlimage, R.mipmap.img_error);
        dialog_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainImageActivity.class).putExtra("url", intentimage));
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


}