package com.yskj.welcomeorchard.ui.advertising;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.alipay.sdk.app.PayTask;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ChangeAddressDialog;
import com.yskj.welcomeorchard.dialog.ChangeOtherDialog;
import com.yskj.welcomeorchard.dialog.PassWordDialog;
import com.yskj.welcomeorchard.entity.CityEntity;
import com.yskj.welcomeorchard.entity.DistrictEntity;
import com.yskj.welcomeorchard.entity.MoneyInfoEntity;
import com.yskj.welcomeorchard.entity.OrderPayEntity;
import com.yskj.welcomeorchard.entity.ProvenceEntity;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.entity.SendAdEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.payment.zfb.PayResult;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.Extension;
import com.yskj.welcomeorchard.utils.GridPasswordView;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.NumberFormat;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用户投放广告页面
 */
public class SendAdvertisingActivity extends BaseActivity implements View.OnClickListener, BaiduMap.OnMapClickListener, OnGetGeoCoderResultListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.tv_sure_send)
    TextView tvSureSend;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.img_notice_delete)
    ImageView imgNoticeDelete;
    @Bind(R.id.ll_notice)
    LinearLayout llNotice;
    @Bind(R.id.tv_province)
    TextView tvProvince;
    @Bind(R.id.tv_range)
    TextView tvRange;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.et_people)
    EditText etPeople;
    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.tv_des_del)
    TextView tvDesDel;

    //地图
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private boolean isFirstMap = true;//判断是不是第一次定位，如果是，记录我的位置；
    public MyLocationListener listener = new MyLocationListener();
    private double myLatitude, myLongitude, latitude, longitude;
    private boolean mapClick = false;
    private GeoCoder search = null;
    private float radius = 500; //单位为米
    private String people = ""; //投放人数
    private String money = ""; //投放金额
    private String sex = ""; //投放性别
    private String age = ""; //投放年龄
    private double amount;
    private double needPay = 0;//当余额不够时候需要充值钱数
    //其他sp
    private ArrayList<String> rangeList = new ArrayList<>();
    private ArrayList<String> peopleList = new ArrayList<>();
    private ArrayList<String> sexList = new ArrayList<>();
    private ArrayList<String> ageList = new ArrayList<>();

    private List<ProvenceEntity> mArrProvinces;
    private Map<String, List<CityEntity>> mCitisDatasMap = new HashMap<>();

    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private MoneyInfoEntity moneyInfoEntity;
    private String adRedId;//php数据ID
    private int pay_type;//支付类型  1.支付宝  2.微信
    private String payRradeNo; //订单号
    private List<DistrictEntity> districtEntityList;
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        isChargePaySendOk();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast("支付结果确认中");
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showToast("您还未安装支付宝客户端");
                        } else {
                            showToast("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private String orderid;
    private ChangeOtherDialog changeOtherDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_advertising);
        ButterKnife.bind(this);
        initView();
        initCityData();
        initOtherSp();
        initListerer();
    }

    private void initCityData() {
        try {
            mArrProvinces = getProvinces();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < mArrProvinces.size(); i++) {
            mCitisDatasMap.put(mArrProvinces.get(i).getId(), mArrProvinces.get(i).getCitys());
        }
    }

    private void initListerer() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //保留小数点后两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etMoney.setText(s);
                        etMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etMoney.setText(s);
                    etMoney.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etMoney.setText(s.subSequence(0, 1));
                        etMoney.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                money = etMoney.getText().toString();
            }
        });
        etPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.toString().length();
                if (len == 1 && text.equals("0")) {
                    s.clear();
                    return;
                }
                people = etPeople.getText().toString();
            }
        });
    }

    private void initOtherSp() {
        rangeList.add("0.5公里");
        rangeList.add("1公里");
        rangeList.add("2公里");
        rangeList.add("3公里");
        rangeList.add("4公里");
        rangeList.add("5公里");
        rangeList.add("10公里");
        rangeList.add("50公里");

        peopleList.add("快捷选择");
        peopleList.add("100");
        peopleList.add("200");
        peopleList.add("500");
        peopleList.add("1000");
        peopleList.add("10000");

        sexList.add("不限");
        sexList.add("男");
        sexList.add("女");

        ageList.add("不限");
        ageList.add("16——25");
        ageList.add("25——30");
        ageList.add("30——40");
        ageList.add("40——50");
        ageList.add("50——75");
        ageList.add("75——90");
    }


    /**
     * 解决百度地图和ScrollView的滑动冲突
     */
    private void solveClash() {
        View v = mapView.getChildAt(0);
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    private void initView() {
        txtTitle.setText("投放广告");
        imgBack.setOnClickListener(this);
        tvProvince.setOnClickListener(this);
        tvRange.setOnClickListener(this);
        tvAge.setOnClickListener(this);
        tvSex.setOnClickListener(this);
        tvSureSend.setOnClickListener(this);
        tvDesDel.setOnClickListener(this);
        imgNoticeDelete.setOnClickListener(this);
        adRedId = getIntent().getStringExtra("adRedId");
        changeOtherDialog = new ChangeOtherDialog(context);
        /**地图*/
        baiduMap = mapView.getMap();
        baiduMap.setOnMapClickListener(this);        //地图点击监听
        /**解决滑动冲突*/
        solveClash();
        /**初始化搜索模块*/
        search = GeoCoder.newInstance();
        search.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        // 开启室内图
        baiduMap.setIndoorEnable(true);
        // 定位初始化
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        locationClient.setLocOption(option);
        locationClient.start();
        // 隐藏logo
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mapView.showScaleControl(false);
        // 隐藏缩放控件
        mapView.showZoomControls(false);
        //设置地图指南针不可见
        baiduMap.getUiSettings().setCompassEnabled(false);
        //设置是否允许旋转手势
        baiduMap.getUiSettings().setRotateGesturesEnabled(false);
        //设置是否允许俯视手势
        baiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        initUserInfo();
        super.onResume();
    }

    private void initUserInfo() {
        OkHttpUtils.get().url(Urls.MONEYINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int ids) {
                moneyInfoEntity = new Gson().fromJson(response,new TypeToken<MoneyInfoEntity>(){}.getType());
            }
        });
    }

    @Override
    protected void onDestroy() {
        /**退出时销毁定位*/
        locationClient.stop();
        /**关闭定位图层*/
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    /**
     * 百度地图点击监听
     */
    @Override
    public void onMapClick(LatLng latLng) {
        geoSearch(latLng);
    }

    /**
     * 百度地图兴趣点点击监听
     */
    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        geoSearch(mapPoi.getPosition());
        return false;
    }

    /**
     * 反地理编码，根据点击点获取坐标，并移动地图中心至点击点，并设置Marker
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.getLocation() == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(SendAdvertisingActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        baiduMap.clear();
        baiduMap.addOverlay(new MarkerOptions().position(geoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_openmap_focuse_mark)));
        if (geoCodeResult.getLocation() != null || !geoCodeResult.getLocation().equals("")) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult.getLocation()));
        }

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.getLocation() == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        baiduMap.clear();
        baiduMap.addOverlay(new MarkerOptions().position(reverseGeoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_openmap_focuse_mark)));
        if (reverseGeoCodeResult.getLocation() != null || !reverseGeoCodeResult.getLocation().equals("")) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult.getLocation()));
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            if (isFirstMap) {
                isFirstMap = false;
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
            } else {
                return;
            }
            if (!mapClick) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            geoSearch(new LatLng(myLatitude, myLongitude));

            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(new LatLng(myLatitude, myLongitude)).zoom(15);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    /**
     * 反Geo搜索
     */
    private void geoSearch(LatLng latLng) {
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        LatLng ptCenter = new LatLng(latitude, longitude);
        search.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
        mapClick = true;
        setRadius();
    }

    /**
     * 此处设置开发者获取到的方向信息，顺时针0-360
     */
    public void setRadius() {
        MyLocationData locData = new MyLocationData.Builder().accuracy(radius)
                .direction(100).latitude(latitude).longitude(longitude).build();
        baiduMap.setMyLocationData(locData);
    }

    //用pull解析获取本地城市列表
    public List<ProvenceEntity> getProvinces() throws XmlPullParserException, IOException {
        List<ProvenceEntity> provinces = null;
        ProvenceEntity province = null;
        List<CityEntity> citys = null;
        CityEntity city = null;
        DistrictEntity district = null;
        Resources resources = getResources();

        InputStream in = resources.openRawResource(R.raw.citys_weather);

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();

        parser.setInput(in, "utf-8");
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:
                    provinces = new ArrayList<ProvenceEntity>();
                    break;
                case XmlPullParser.START_TAG:
                    String tagName = parser.getName();
                    if ("p".equals(tagName)) {
                        province = new ProvenceEntity();
                        citys = new ArrayList<CityEntity>();
                        int count = parser.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            String attrName = parser.getAttributeName(i);
                            String attrValue = parser.getAttributeValue(i);
                            if ("p_id".equals(attrName))
                                province.setId(attrValue);
                        }
                    }
                    if ("pn".equals(tagName)) {
                        province.setName(parser.nextText());
                    }
                    if ("c".equals(tagName)) {
                        city = new CityEntity();
                        districtEntityList = new ArrayList<DistrictEntity>();
                        int count = parser.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            String attrName = parser.getAttributeName(i);
                            String attrValue = parser.getAttributeValue(i);
                            if ("c_id".equals(attrName))
                                city.setId(attrValue);
                        }
                    }
                    if ("cn".equals(tagName)) {
                        city.setName(parser.nextText());
                    }
                    if ("d".equals(tagName)) {
                        district = new DistrictEntity();
                        int count = parser.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            String attrName = parser.getAttributeName(i);
                            String attrValue = parser.getAttributeValue(i);
                            if ("d_id".equals(attrName))
                                district.setId(attrValue);
                        }
                        district.setName(parser.nextText());
                        districtEntityList.add(district);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("c".equals(parser.getName())) {
                        city.setDistricts(districtEntityList);
                        citys.add(city);
                    }
                    if ("p".equals(parser.getName())) {
                        province.setCitys(citys);
                        provinces.add(province);
                    }
                    break;
            }
            event = parser.next();
        }
        return provinces;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_sure_send:
                //确认发布
                sureSend();
                break;
            case R.id.tv_province:
                ChangeAddressDialog dialog = new ChangeAddressDialog(SendAdvertisingActivity.this);
                dialog.show();
                dialog.setAddressData(mArrProvinces, mCitisDatasMap);
                dialog.setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {
                    @Override
                    public void onClick(ProvenceEntity province, CityEntity city) {
                        tvProvince.setText(province.getName() + city.getName());
                        tvProvince.setTextColor(Color.parseColor("#333333"));
                        search.geocode(new GeoCodeOption().city(city.getName()).address(city.getName() + "市政府"));
                    }
                });
                break;
            case R.id.tv_range:
                changeOtherDialog.show();
                changeOtherDialog.setAddressData(rangeList);
                changeOtherDialog.setAddresskListener(new ChangeOtherDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String data) {
                        tvRange.setText(data);
                        tvRange.setTextColor(Color.parseColor("#333333"));
                        radius = Float.parseFloat(data.replace("公里", "")) * 1000;
                        setRadius();
                    }
                });
                break;
            case R.id.tv_sex:
                changeOtherDialog.show();
                changeOtherDialog.setAddressData(sexList);
                changeOtherDialog.setAddresskListener(new ChangeOtherDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String data) {
                        tvSex.setText(data);
                        sex = data;
                        if (sex.equals("男")) {
                            sex = "1";
                        }
                        if (sex.equals("女")) {
                            sex = "0";
                        }
                        tvSex.setTextColor(Color.parseColor("#333333"));
                    }
                });
                break;

            case R.id.tv_age:
                changeOtherDialog.show();
                changeOtherDialog.setAddressData(ageList);
                changeOtherDialog.setAddresskListener(new ChangeOtherDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String data) {
                        tvAge.setText(data);
                        tvAge.setTextColor(Color.parseColor("#333333"));
                    }
                });
                break;

            case R.id.tv_des_del:
                tvDesDel.setVisibility(View.GONE);
                break;
            case R.id.img_notice_delete:
                llNotice.setVisibility(View.GONE);
                break;
        }
    }

    private void sureSend() {
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(SendAdvertisingActivity.this, LoginActivity.class));
            return;
        } else {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
        }
        if (!mapClick) {
            showToast("请点击地图位置，选择投放区");
            return;
        }
        if (people.equals("") || people.equals("快捷选择")) {
            showToast("请选择投放人数");
            return;
        }
        if (money.equals("")) {
            showToast("请输入投放金额");
            return;
        }
        //普通红包每个最低0.3   精选红包每个最低0.5
        if (tvAge.getText().toString().equals("不限") && tvSex.getText().toString().equals("不限")) {
            if (NumberFormat.convertToDouble(money, 0.0) < NumberFormat.convertToDouble(people, 0.0) * 0.3) {
                showToast("最低金额为" + String.format("%.2f", NumberFormat.convertToDouble(people, 0.0) * 0.3));
                return;
            }
        } else {
            if (NumberFormat.convertToDouble(money, 0.0) < NumberFormat.convertToDouble(people, 0.0) * 0.5) {
                showToast("最低金额为" + String.format("%.2f", NumberFormat.convertToDouble(people, 0.0) * 0.5));
                return;
            }
        }
        SetUp();
    }

    //选择支付类型弹窗
    private class PayDialog extends Dialog implements View.OnClickListener {
        private Context context;
        private NoScrollListView listView;

        private ImageView btn_cancle;
        private TextView btn_sure;
        private int[] imgList;
        private String[] payType;
        private int selectedPosition = 0;
        private OrderPayAdapter adapter;

        public PayDialog(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_send_red_pay);
            initDialogView();
        }

        private void initDialogView() {
            listView = (NoScrollListView) findViewById(R.id.listView);
            imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
            btn_cancle = (ImageView) findViewById(R.id.btn_cancle);
            btn_sure = (TextView) findViewById(R.id.btn_sure);
            TextView detail_meny = (TextView) findViewById(R.id.detail_meny);//投放金额
            TextView tv_balance_meny = (TextView) findViewById(R.id.tv_balance_meny);//余额抵扣
            TextView excrete_true_meny = (TextView) findViewById(R.id.excrete_true_meny);//应付
            detail_meny.setText(StringUtils.getStringtodouble(Double.parseDouble(money)));
            amount = moneyInfoEntity.data.fundAccount;
            double deduction = DoubleUtils.sub(amount, Double.parseDouble(money));
            if (deduction >= 0) {
                tv_balance_meny.setText(StringUtils.getStringtodouble(Double.parseDouble(money)));
            } else {
                tv_balance_meny.setText(StringUtils.getStringtodouble(amount));
            }
            excrete_true_meny.setText(StringUtils.getStringtodouble(needPay));
            btn_cancle.setOnClickListener(this);
            btn_sure.setOnClickListener(this);

            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);

            payType = new String[]{"支付宝支付", "微信支付"};
            final ArrayList<OrderPayEntity> arrayList = new ArrayList<>();
            for (int i = 0; i < imgList.length; i++) {
                OrderPayEntity orderPayEntity = new OrderPayEntity();
                orderPayEntity.setChacked(false);
                orderPayEntity.setImgId(imgList[i]);
                orderPayEntity.setPayType(payType[i]);
                arrayList.add(orderPayEntity);
            }
            adapter = new OrderPayAdapter(context, arrayList);
            listView.setAdapter(adapter);
            listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedPosition = position;
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    dismiss();
                    switch (selectedPosition) {
                        case 0:
                            pay_type = 1;
                            break;
                        case 1:
                            pay_type = 2;
                            break;
                    }
                    chargePay();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }

        public class OrderPayAdapter extends BaseAdapter {
            private Context context;
            private ArrayList<OrderPayEntity> arrayList;
            private Map<Integer, Boolean> selectedMap;//保存checkbox是否被选中的状态

            public OrderPayAdapter(Context context, ArrayList<OrderPayEntity> arrayList) {
                this.context = context;
                this.arrayList = arrayList;
                selectedMap = new HashMap<Integer, Boolean>();
                initData();
            }

            public void initData() {

                for (int i = 0; i < arrayList.size(); i++) {
                    selectedMap.put(i, false);
                }
            }

            @Override
            public int getCount() {
                if (arrayList == null || arrayList.size() == 0)
                    return 0;
                return arrayList.size();
            }

            @Override
            public Object getItem(int position) {
                return arrayList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_order_pay, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                OrderPayEntity orderPayEntity = arrayList.get(position);
                holder.imageView.setImageResource(orderPayEntity.imgId);
                holder.textView.setText(orderPayEntity.payType);
                if (selectedPosition == 0) {
                    if (position == 0) {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzg);
                    } else {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzf);
                    }
                } else {
                    if (selectedPosition == position) {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzg);
                    } else {
                        holder.img_choose_icon.setImageResource(R.mipmap.fzf);
                    }
                }
                return convertView;
            }

            private class ViewHolder {
                ImageView imageView, img_choose_icon;
                TextView textView;

                public ViewHolder(View itemView) {
                    imageView = (ImageView) itemView.findViewById(R.id.img_type);
                    textView = (TextView) itemView.findViewById(R.id.tv_type);
                    img_choose_icon = (ImageView) itemView.findViewById(R.id.img_choose_icon);
                }
            }
        }
    }

    //检查是否发送成功
    private class sendADCallBack extends Callback<SendAdEntity> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            stopMyDialog();
            super.onAfter(id);
        }

        @Override
        public SendAdEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            SendAdEntity sendAdEntity = new Gson().fromJson(s, new TypeToken<SendAdEntity>() {
            }.getType());
            return sendAdEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("服务器正忙，稍等。。。");
        }

        @Override
        public void onResponse(SendAdEntity response, int id) {
            if (response.code == 0) {
                startActivity(new Intent(context,AdvertiseaCcomplishActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }

    /**
     * 生成临时订单
     */
    private void SetUp() {
        if(orderid==null){
            if (sex.equals("") || sex.equals("不限")) {
                if (age.equals("") || age.equals("不限")) {
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("accountType", "10")
                            .addParams("region", radius + "")
                            .build().execute(new CreatCallBack());
                } else {
                    String agelimit[] = age.toString().split("——");
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("region", radius + "")
                            .addParams("accountType", "10")
                            .addParams("limitMinAge", agelimit[0]).addParams("limitMaxAge", agelimit[1])
                            .build().execute(new CreatCallBack());
                }
            } else {
                if (age.equals("") || age.equals("不限")) {
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("region", radius + "")
                            .addParams("limitSex", sex)
                            .addParams("accountType", "10")
                            .build().execute(new CreatCallBack());
                } else {
                    String agelimit[] = age.toString().split("——");
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("region", radius + "")
                            .addParams("limitSex", sex)
                            .addParams("accountType", "10")
                            .addParams("limitMinAge", agelimit[0]).addParams("limitMaxAge", agelimit[1])
                            .build().execute(new CreatCallBack());
                }
            }
        }else {
            if (sex.equals("") || sex.equals("不限")) {
                if (age.equals("") || age.equals("不限")) {
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("accountType", "10")
                            .addParams("orderId",orderid)
                            .addParams("region", radius + "")
                            .build().execute(new CreatCallBack());
                } else {
                    String agelimit[] = age.toString().split("——");
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("region", radius + "")
                            .addParams("orderId",orderid)
                            .addParams("accountType", "10")
                            .addParams("limitMinAge", agelimit[0]).addParams("limitMaxAge", agelimit[1])
                            .build().execute(new CreatCallBack());
                }
            } else {
                if (age.equals("") || age.equals("不限")) {
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("region", radius + "")
                            .addParams("limitSex", sex)
                            .addParams("orderId",orderid)
                            .addParams("accountType", "10")
                            .build().execute(new CreatCallBack());
                } else {
                    String agelimit[] = age.toString().split("——");
                    OkHttpUtils.post().url(Urls.CREATEADRED)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("adPageId", adRedId)
                            .addParams("sendCount", people)
                            .addParams("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)))
                            .addParams("latitude", latitude + "")
                            .addParams("longitude", longitude + "")
                            .addParams("region", radius + "")
                            .addParams("limitSex", sex)
                            .addParams("orderId",orderid)
                            .addParams("accountType", "10")
                            .addParams("limitMinAge", agelimit[0]).addParams("limitMaxAge", agelimit[1])
                            .build().execute(new CreatCallBack());
                }
            }
        }

    }

    private class CreatCallBack extends Callback<ScanCodeTmpEntity> {
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
        public ScanCodeTmpEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ScanCodeTmpEntity setUpEntity = new Gson().fromJson(s, new TypeToken<ScanCodeTmpEntity>() {
            }.getType());
            return setUpEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(ScanCodeTmpEntity response, int id) {
            if (response.code == 0) {
                orderid = response.data.orderId + "";
                caches.put("tid", orderid);
                //如果用户余额大于等于投放金额，判断用户是否设置支付密码，弹出支付密码框
                //如果
                if (moneyInfoEntity.data.fundAccount >= Double.parseDouble(money)) {
                    if (userInfoEntity.data.accountPasswordExist) {
                        SurePwdDialog dialog = new SurePwdDialog(context);
                        dialog.show();
                    } else {
                        caches.put("type", "SendAdvertisingActivity");
                        //设置支付密码
                        PassWordDialog dialog = new PassWordDialog(context, "您还没有设置支付密码,是否前往设置");
                        dialog.show();
                    }
                } else {
                    needPay = Double.parseDouble(money) - moneyInfoEntity.data.fundAccount;
                    PayDialog dialog = new PayDialog(context);
                    dialog.show();

                }
            }else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }

    //调取支付
    private void chargePay() {
        OkHttpUtils.post().url(Urls.PAY).addHeader("Authorization", caches.get("access_token"))
                .addParams("order_type", "4")
                .addParams("order_id", orderid)
                .addParams("price", String.format("%.2f", NumberFormat.convertToDouble(needPay, 0.00)))
                .addParams("pay_type", pay_type + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    JSONObject payinfor = (JSONObject) map.get("data");
                    final String PY_info;
                    try {
                        payRradeNo = payinfor.getString("pay_trade_no");
                        if (pay_type == 1) {
                            PY_info = (String) payinfor.get("pay_info");
                            Runnable payRunnable = new Runnable() {
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask aliPay = new PayTask((Activity) context);
                                    // 调用支付接口，获取支付结果
                                    String result = aliPay.pay(
                                            PY_info, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else if (pay_type == 2) {
                            JSONObject params = payinfor.getJSONObject("pay_info");
                            if (Extension.isWeixinAvilible(context)) {
                                caches.put("ordertype", "4");
                                caches.put("pay_trade_no", payRradeNo + "");
                                caches.put("sex", sex);
                                caches.put("age", age);
                                caches.put("adRedId", adRedId);
                                caches.put("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)));
                                caches.put("sendCount", people);
                                caches.put("sendAmount", String.format("%.2f", NumberFormat.convertToDouble(money, 0.00)));
                                caches.put("latitude", latitude + "");
                                caches.put("longitude", longitude + "");
                                caches.put("tradeNo", payRradeNo);
                                caches.put("region", radius + "");
                                String appid = params.getString("appid");
                                IWXAPI api = WXAPIFactory.createWXAPI(context, appid, false);
                                api.registerApp(appid);
                                PayReq req = new PayReq();
                                req.appId = params.getString("appid");
                                req.partnerId = params.getString("partnerid");
                                req.prepayId = params.getString("prepayid");
                                req.packageValue = "Sign=WXPay";
                                req.nonceStr = params.getString("noncestr");
                                req.timeStamp = params.getString("timestamp");
                                req.sign = params.getString("sign");
                                api.sendReq(req);
                            } else {
                                Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    //用通贝支付
    public class SurePwdDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        public SurePwdDialog(Context context) {
            super(context, R.style.GiftDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);
            initView();
        }

        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            tv_forget_pwd.setVisibility(View.GONE);//不显示密码框下边的钱
            pwdView = (GridPasswordView) findViewById(R.id.pwd);
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {
                }

                @Override
                public void onMaxLength(String psw) {
                    dismissJP();
                    dismiss();
                    //检查是否发送成功
                    OkHttpUtils.post().url(Urls.ADSENDRED).addHeader("Authorization", caches.get("access_token"))
                            .addParams("adRedId", orderid)
                            .addParams("payPassword", psw)
                            .build().execute(new sendADCallBack());
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) SendAdvertisingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void isChargePaySendOk() {
        //检查是否发送成功
        OkHttpUtils.post().url(Urls.ADSENDRED)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("adRedId", orderid)
                .addParams("tradeNo", payRradeNo)
                .build().execute(new sendADCallBack());

    }
}
