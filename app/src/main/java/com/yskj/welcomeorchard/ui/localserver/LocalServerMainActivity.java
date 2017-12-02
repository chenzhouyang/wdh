package com.yskj.welcomeorchard.ui.localserver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.unionpay.tsmservice.request.RequestParams;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.LocalMainAdapter;
import com.yskj.welcomeorchard.adapter.LocalServerMainListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerMainListBean;
import com.yskj.welcomeorchard.entity.LocalServerNearPopBean;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Administrator on 2016/11/7 0007.
 */
public class LocalServerMainActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_address;
    private EditText et_search;
    private ImageButton image_search;
    private NoScrollListView listView;
    private ImageView imgOrder,imgPosition;


    private ArrayList<LocalServerMainListBean.LocalLifesBean> locallifelist = new ArrayList<>();
    private ArrayList<LocalServerMainListBean.LocalLifesBean> list = new ArrayList<>();
    private PullToRefreshScrollView scrollView;
    private Intent intent;

    private LocalServerMainListAdapter adapter;
    private int offset = 0;

    private static final int REQUESRCODE = 1001;
    private String tvAddress = "";
    private String cityId = "";
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private LocalServerNearPopBean localServerNearPopBean;
    private ViewPager viewpager;
    private ArrayList<View> mViews = new ArrayList<>();

    //回调接口 用于以后跳转选择tab
    public interface ChangePageListener {
        void changeListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_server_main);
        initView();
        initScrollView();
        //initGridView();
        getfication();
    }

    @Override
    protected void onResume() {
        super.onResume();
        offset = 0;
        locallifelist.clear();
        initData();
    }

    private void initData() {
        adapter = new LocalServerMainListAdapter(context, locallifelist);
        listView.setAdapter(adapter);
        RequestParams params = new RequestParams();
        if (sp.getString(Config.SPKEY_CITYID, "0").equals("0") || sp.getString(Config.SPKEY_CITY, "0").equals("0")) {
            showToast("请选择城市");
            intent = new Intent(LocalServerMainActivity.this, CityBusinessActivity.class);
            startActivityForResult(intent, REQUESRCODE);
            return;
        } else {
            tv_address.setText(sp.getString(Config.SPKEY_CITY, "许昌市"));
            cityId = sp.getString(Config.SPKEY_CITYID, "22655");
        }
        if (cityId == null || cityId.equals("")) {
            OkHttpUtils.get().url(Urls.LOCALSERVERFIND).addParams("latitude", sp.getString(Config.SPKEY_LATITUDE, "0"))
                    .addParams("longitude", sp.getString(Config.SPKEY_LONGITUDE, "0")).addParams("offset", offset * 10 + "").build().execute(new LocalServerMainListCallBack());
        } else {
            OkHttpUtils.get().url(Urls.LOCALSERVERFIND).addParams("areaId", cityId).addParams("offset", offset * 10 + "").build().execute(new LocalServerMainListCallBack());
        }
    }

    private class LocalServerMainListCallBack extends Callback<LocalServerMainListBean> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }
        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
            scrollView.onRefreshComplete();
        }
        @Override
        public LocalServerMainListBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LocalServerMainListBean localServerMainListBean = new Gson().fromJson(s, new TypeToken<LocalServerMainListBean>() {
            }.getType());
            return localServerMainListBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            stopMyDialog();
            scrollView.onRefreshComplete();
        }

        @Override
        public void onResponse(LocalServerMainListBean response, int id) {
            if (offset==0){
                locallifelist.clear();
            }
            if (response.code == 0) {
                list.clear();
                list.addAll(response.data.localLifes);
                if (list == null || list.size() == 0) {
                    showToast("没用更多数据了");
                }
                locallifelist.addAll(list);
                adapter.notifyDataSetChanged();
                stopMyDialog();
                scrollView.onRefreshComplete();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(LocalServerMainActivity.this, LocalServerDetailActivity.class);
                        intent.putExtra("life_id", locallifelist.get(position).lifeId + "");
                        intent.putExtra("sell", locallifelist.get(position).saleCount + "");
                        intent.putExtra("distance", locallifelist.get(position).distanceString + "");
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void initScrollView() {
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset = 0;
                locallifelist.clear();
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                offset++;
                initData();
            }
        });
    }

    private void initView() {
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address.setText(sp.getString(Config.SPKEY_CITYNAME, "许昌"));
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setFocusable(false);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        image_search = (ImageButton) findViewById(R.id.image_search);
        imgOrder = (ImageView) findViewById(R.id.img_order);
        imgPosition = (ImageView) findViewById(R.id.img_position);
        //gridView = (GridView) findViewById(R.id.my_gridView);
        listView = (NoScrollListView) findViewById(R.id.listView);
        listView.setFocusable(false);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
        tv_address.setOnClickListener(this);
        et_search.setOnClickListener(this);
        imgPosition.setOnClickListener(this);
        imgOrder.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_address:
                intent = new Intent(LocalServerMainActivity.this, CityBusinessActivity.class);
                startActivityForResult(intent, REQUESRCODE);
                break;
            case R.id.et_search:
                intent = new Intent(LocalServerMainActivity.this, LookUpActivity.class);
                startActivity(intent);
                break;
            case R.id.img_order:
                if(aCache.get("access_token").equals("null")){
                    startActivity(new Intent(context, LoginActivity.class));
                }else {
                    intent = new Intent(LocalServerMainActivity.this,LocalServerOrderActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.img_position:
                intent = new Intent(LocalServerMainActivity.this,LocalServerNearActivity.class);
                startActivity(intent);
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stopMyDialog();
        scrollView.onRefreshComplete();
        if (requestCode == REQUESRCODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    tvAddress = bundle.getString("name");
                    cityId = bundle.getString("cityId");
                    Logger.d(cityId+"");
                    sp.edit().putString(Config.SPKEY_CITY, tvAddress).commit();
                    sp.edit().putString(Config.SPKEY_CITYID, cityId).commit();
                    tv_address.setText(tvAddress);
                    locallifelist.clear();
                    offset = 0;
                    initData();
                }

            }
        }

    }
    private void getfication(){
        OkHttpUtils.get().url(Urls.LOCALSERVERPOPWIN).build().execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    localServerNearPopBean = new Gson().fromJson(response, new TypeToken<LocalServerNearPopBean>() {}.getType());
                    LocalMainAdapter adapter1 = new LocalMainAdapter(context,localServerNearPopBean.ret_data.subList(0,10));
                    //把有GridView布局资源转换为View对象
                    View PagerOne = getLayoutInflater().inflate(R.layout.viewpager_gridview, null);
                    //从装有GridView的View对象里查找GridView对象
                    GridView gridView = (GridView) PagerOne.findViewById(R.id.gridView);
                    gridView.setFocusable(true);
                    //为GridView设置适配器
                    gridView.setAdapter(adapter1);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Intent intent = new Intent(context, LocalServerKindActivity.class);
                            LocalServerNearPopBean.RetData retData = localServerNearPopBean.ret_data.get(position);
                            intent.putExtra("title", retData.name);
                            intent.putExtra("categoryId", retData.cateId);
                            if (cityId==null||cityId.equals("0")){
                                intent = new Intent(LocalServerMainActivity.this, CityBusinessActivity.class);
                                startActivityForResult(intent, REQUESRCODE);
                            }else {
                                startActivity(intent);
                            }

                        }
                    });
                    mViews.add(PagerOne);
                    if(localServerNearPopBean.ret_data.size()>10){
                        /**
                         * 第二个GridView的页面
                         */
                        LocalMainAdapter adapter2 = new LocalMainAdapter(context,localServerNearPopBean.ret_data.subList(10,localServerNearPopBean.ret_data.size()));
                        View pagerTwo = getLayoutInflater().inflate(R.layout.viewpager_gridview, null);
                        GridView gridView02 = (GridView) pagerTwo.findViewById(R.id.gridView);
                        gridView02.setAdapter(adapter2);
                        gridView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                Intent intent = new Intent(context, LocalServerKindActivity.class);
                                LocalServerNearPopBean.RetData retData = localServerNearPopBean.ret_data.subList(10,localServerNearPopBean.ret_data.size()).get(position);
                                intent.putExtra("title", retData.name);
                                intent.putExtra("categoryId", retData.cateId);
                                if (cityId==null||cityId.equals("0")){
                                    intent = new Intent(LocalServerMainActivity.this, CityBusinessActivity.class);
                                    startActivityForResult(intent, REQUESRCODE);
                                }else {
                                    startActivity(intent);
                                }

                            }
                        });
                        mViews.add(pagerTwo);
                    }
                    //对viewPager初始化
                    viewpager.setAdapter(new MyPagerAdapter(mViews));

                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }



    public class MyPagerAdapter extends PagerAdapter {

        //存放外界传来的集合数据
        private ArrayList<View> mViews = new ArrayList<>();

        //构造方法,进行容器数据的初始化,必须把外界的数据传进来,让ViewPager进行加载显示
        //提示:有些参数没有数据,但是代码中用到了,第一个想到的构造方法,传数据
        public MyPagerAdapter(ArrayList<View> views) {
            mViews = views;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //从容器里拿数据
            View view = mViews.get(position);
            //把控件对象放入ViewPager的容器里,进行显示
            container.addView(view);
            //把控件显示出来,方便销毁
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //把不用的View对象销毁,防止内存泄漏
            container.removeView(mViews.get(position));
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }



}
