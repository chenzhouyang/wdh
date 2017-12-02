package com.yskj.welcomeorchard.ui.advertising;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.ItemRedRecordAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.AdverMainDialog;
import com.yskj.welcomeorchard.dialog.TitlePopup;
import com.yskj.welcomeorchard.entity.CarcuseEntity;
import com.yskj.welcomeorchard.entity.MyItem;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.approve.ApproveActivity;
import com.yskj.welcomeorchard.ui.mapmanager.Cluster;
import com.yskj.welcomeorchard.ui.mapmanager.ClusterManager;
import com.yskj.welcomeorchard.utils.BaiDuUtil;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.widget.BetterRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/3/13.
 * 广告首页 主要有轮播图，拆红包地图及menu功能
 */

public class AdvertisingMainActivity extends BaseActivity implements View.OnClickListener, CloudListener, BaiduMap.OnMapLoadedCallback{
    @Bind(R.id.image_right)
    Button imageRight;
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.mapView)
    TextureMapView mMapView;
    @Bind(R.id.btn_release)
    ImageView btnRelease;
    @Bind(R.id.btn_to_my_location)
    Button btnToMyLocation;
    @Bind(R.id.close_advertising)
    ImageView closeAdvertising;
    @Bind(R.id.main_framelayout)
    FrameLayout mainFramelayout;
    @Bind(R.id.btn_map_big)
    Button btnMapBig;
    @Bind(R.id.btn_map_small)
    Button btnMapSmall;
    @Bind(R.id.fl_title)
    FrameLayout flTitle;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    private PopupWindow mPopupWindow;
    //定义标题栏弹窗按钮
    private TitlePopup titlePopup;
    private LinearLayout popupLayout;
    // 定位相关
    private BaiduMap mBaiduMap;
    public MyLocationListenner myListener = new MyLocationListenner();
    LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker = null;

    boolean isFirstLoc = true; // 是否首次定位
    private ArrayList<MyItem> clusterList;
    private double latitude, longitude;
    MapStatusUpdate zoomIn = MapStatusUpdateFactory.zoomIn();
    MapStatusUpdate zoomOut = MapStatusUpdateFactory.zoomOut();
    private int pageIndex = 0;
    //检索当前页的红包
    private ArrayList<MyItem> items;
    private MapStatus ms;
    private ClusterManager<MyItem> mClusterManager = null;
    private StaggeredGridLayoutManager layoutManager;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private ArrayList image;
    private int a = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            a++;
            if(null!=mBaiduMap){
                if (a % 2 == 0) {
                    mBaiduMap.setMapStatus(zoomIn);
                } else {
                    mBaiduMap.setMapStatus(zoomOut);
                }
            }

        }
    };
    private RedMarkDialog redMarkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        setContentView(R.layout.activity_adversing_main);
        ButterKnife.bind(this);
        initView();
        setDate();
    }

    protected void onResume() {
        //开始自动翻页
        convenientBanner.startTurning(3000);
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
        if (mBaiduMap != null) {
            mBaiduMap.clear();
        }
        if (mClusterManager != null) {
            mClusterManager = null;
        }
        pageIndex = 0;
        searchPoi();
    }

    private void setDate() {
        OkHttpUtils.get().url(Urls.CAROUSE + "1").build().execute(new CarouleCallBack());
    }



    public class CarouleCallBack extends Callback<CarcuseEntity> {
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
        public CarcuseEntity parseNetworkResponse(Response response, int id) throws Exception {
            String string = response.body().string();
            CarcuseEntity carcuseEntity = new Gson().fromJson(string, new TypeToken<CarcuseEntity>() {
            }.getType());
            return carcuseEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(CarcuseEntity response, int id) {
            //添加轮播图
            image = new ArrayList();
            ArrayList<CarcuseEntity.AdListBean> adList = response.adList;
            if (adList != null && adList.size() != 0) {
                for (int i = 0; i < adList.size(); i++) {
                    image.add(adList.get(i).adCode);
                }
            } else {
                return;
            }
            //初始化首页轮播图
            initCB();
        }
    }

    //加载轮播
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
            imageView.setImageResource(R.mipmap.img_error);
            GlideImage.loadImage(context, imageView, data, R.mipmap.img_error);
        }
    }

    //LBS云检索
    private void searchPoi() {
        CloudManager.getInstance().init(AdvertisingMainActivity.this);
        NearbySearchInfo info = new NearbySearchInfo();
        info.ak = Config.SERVERBAIDUAK;
        info.geoTableId = Config.GEOTABLEID;
        info.radius = 50000000;
        info.pageIndex = pageIndex;
        info.filter = "ad_red_status:[0,1]";
        info.location = longitude + "," + latitude;
        CloudManager.getInstance().nearbySearch(info);
    }

    private void localtion() {
        // 地图初始化
        mBaiduMap = mMapView.getMap();

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        mBaiduMap.setMyLocationEnabled(true);
        // 开启室内图
        mBaiduMap.setIndoorEnable(true);
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        //设置是否允许旋转手势
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        //设置是否允许俯视手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        //设置是否允许缩放手势
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocClient.setLocOption(option);
        mLocClient.start();
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(false);
        //设置地图指南针不可见
        mBaiduMap.getUiSettings().setCompassEnabled(false);
        //设置是否允许旋转手势
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        //设置是否允许俯视手势
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        //设置是否允许缩放手势
        mBaiduMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
            if (pageIndex == 0) {
                mBaiduMap.clear();
            }
            //根据检索状态显示检索内容
            allSearchPoi(result, i);
        }
    }

    //扫描全部数据，没有任何处理
    private void allSearchPoi(CloudSearchResult result, int i) {
        items = new ArrayList<>();
        ms = new MapStatus.Builder().target(new LatLng(latitude, longitude)).zoom(12).build();
        mBaiduMap.setOnMapLoadedCallback(this);
        if(ms!=null){
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        }
        // 定义点聚合管理类ClusterManager
        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<>(this, mBaiduMap);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng ll = null;
        for (int n = 0; n < result.poiList.size(); n++) {
            CloudPoiInfo cloudPoiInfo = result.poiList.get(n);
            ll = new LatLng(cloudPoiInfo.latitude, cloudPoiInfo.longitude);
            Map<String, Object> map = result.poiList.get(n).extras;
            MyItem item = new MyItem(ll,map);
            item.setRedMaxAge(map.get("ad_red_limit_max_age") == null ? "" : map.get("ad_red_limit_max_age").toString());
            item.setRedTemplate(map.get("ad_red_template") == null ? "" : map.get("ad_red_template").toString());
            item.setRedMinAge(map.get("ad_red_limit_min_age") == null ? "" : map.get("ad_red_limit_min_age").toString());
            item.setRedSex(map.get("ad_red_limit_sex") == null ? "" : map.get("ad_red_limit_sex").toString());
            item.setRedCover(map.get("ad_red_cover") == null ? "" : map.get("ad_red_cover").toString());
            item.setRedTitle(map.get("ad_red_title") == null ? "" : map.get("ad_red_title").toString());
            item.setRedImage(map.get("ad_red_avatar") == null ? "" : map.get("ad_red_avatar").toString());
            item.setRedName(map.get("ad_red_name") == null ? "" : map.get("ad_red_name").toString());
            item.setRedUserIds(map.get("ad_red_user_ids") == null ? "-1,-2" : map.get("ad_red_user_ids").toString());
            item.setRedRegion(map.get("ad_red_region").toString() == null ? "" : map.get("ad_red_region").toString());
            item.setRedStatus(map.get("ad_red_status").toString() == null ? "" : map.get("ad_red_status").toString());
            item.setRedId(map.get("ad_red_id").toString() == null ? "" : map.get("ad_red_id").toString());
            item.setRedAddress(cloudPoiInfo.address);
            items.add(item);
            if (items.size() == result.poiList.size()) {
                mClusterManager.addItems(items);
            }
            builder.include(ll);
        }

        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                goToJudge(item);
                return false;
            }
        });
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                 clusterList = (ArrayList<MyItem>) cluster.getItems();
                 redMarkDialog = new RedMarkDialog(context);
                redMarkDialog.show();
                return false;
            }
        });
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(latitude)
                .longitude(longitude).build();
        mBaiduMap.setMyLocationData(locData);
        if (result.total > 10 * pageIndex + result.size) {
            pageIndex++;
            searchPoi();
        } else {
            handler.sendEmptyMessageDelayed(2, 300);
        }
    }

    //判断红包是否可领取
    private void goToJudge(MyItem item) {
        //1.先判断用户是否登陆
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(AdvertisingMainActivity.this, LoginActivity.class));
            return;
        } else {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
        }
        //2.判断是否是精准红包
        if (!item.getRedSex().equals("") || !item.getRedMaxAge().equals("0") || !item.getRedMinAge().equals("0")) {
            //是精准红包   3判断是否实名认证
            if (userInfoEntity.data.realnameVo == null || userInfoEntity.data.realnameVo.toString().equals("")) {
                //没有实名认证去实名认证
                Intent intent = new Intent(context, ApproveActivity.class);
                intent.putExtra("type", "3");
                startActivity(intent);
                return;
            } else {
                //限制性别
                if (!item.getRedSex().equals("")) {
                    if (userInfoEntity.data.userVo.sex != null && !userInfoEntity.data.userVo.sex.equals(item.getRedSex())) {
                        //性别不符
                        showToast("该红包限制性别领取哦，试试其他的吧");
                        return;
                    }
                }
                //限制年龄
                if (!item.getRedMaxAge().equals("0") || !item.getRedMinAge().equals("0")) {
                    if (Integer.parseInt(item.getRedMaxAge()) < Integer.parseInt(userInfoEntity.data.userVo.age) || Integer.parseInt(item.getRedMinAge()) > Integer.parseInt(userInfoEntity.data.userVo.age)) {
                        //年龄不符
                        showToast("该红包限制年龄段领取哦，试试其他的吧");
                        return;
                    }
                }
            }
        }
        if (item.getRedStatus().equals("2")) {
            showToast("广告红包已拆完");
            return;
        }
        if (Double.parseDouble(item.getRedRegion()) < BaiDuUtil.getDistanceSimplify(longitude, latitude, item.getPosition().longitude, item.getPosition().latitude)) {
            showToast("广告红包超出可领范围");
            return;
        }
        Intent intent = new Intent(context, AdvertisingOpenActivity.class);

        intent.putExtra("adRedId", item.getRedId());
        intent.putExtra("adRedTemplate", item.getRedTemplate());
        intent.putExtra("latitude", item.getPosition().latitude + "");
        intent.putExtra("longitude", item.getPosition().longitude + "");
        startActivity(intent);
    }

    //待使用，用于解决地图级别最大红包点击不了问题
    //地图make点点击显示对话框
    public class RedMarkDialog extends Dialog {
        private BetterRecyclerView recycler_view;

        public RedMarkDialog(Context context) {
            super(context, R.style.GiftDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_red_mark);
            initRedDialogView();
            initRedDialogListener();
        }

        private void initRedDialogView() {
            recycler_view = (BetterRecyclerView) findViewById(R.id.recycler_view);
            //全屏，底部弹出
            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }

        private void initRedDialogListener() {
            recycler_view.setHasFixedSize(true);
            int spanCount = 1; // 只显示一行
            layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL);
            recycler_view.setLayoutManager(layoutManager);
            ItemRedRecordAdapter adapter = new ItemRedRecordAdapter(context, clusterList);
            recycler_view.setAdapter(adapter);
            adapter.setInterfaceListener(new ItemRedRecordAdapter.OnInterfaceListener() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void getRed(int postion) {
                    MyItem myItem = clusterList.get(postion);
                    goToJudge(myItem);
                }

                @Override
                public void showdialog() {
                    redMarkDialog.dismiss();
                }
            });
        }
    }
    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {

    }

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(12).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                sp.edit().putString(Config.SPKEY_LATITUDE, latitude+"").commit();
                sp.edit().putString(Config.SPKEY_LONGITUDE, longitude+"").commit();
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void initView() {
        try {
            latitude = Double.parseDouble(sp.getString(Config.SPKEY_LATITUDE,"0"));
            longitude = Double.parseDouble(sp.getString(Config.SPKEY_LONGITUDE,"0"));
        }catch (Exception e){
            e.getStackTrace();
        }
        localtion();
        imageRight.setOnClickListener(this);
        btnRelease.setOnClickListener(this);
        btnToMyLocation.setOnClickListener(this);
        closeAdvertising.setOnClickListener(this);
        btnMapBig.setOnClickListener(this);
        btnMapSmall.setOnClickListener(this);
        isFirstLoc = true; // 每次进来定位一次
        //实例化标题栏弹窗
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_right:
                AdverMainDialog dialog = new AdverMainDialog(context);
                dialog.show();
                break;
            case R.id.btn_release:
                startActivity(new Intent(context, ChooseVersionActivity.class));
                break;
            case R.id.btn_to_my_location:
                backToMyLocation();
                break;
            case R.id.close_advertising:
                mainFramelayout.setVisibility(View.GONE);
                break;
            case R.id.btn_map_big:
                mBaiduMap.setMapStatus(zoomIn);
                break;
            case R.id.btn_map_small:
                mBaiduMap.setMapStatus(zoomOut);
                break;
        }
    }

    //回到我的位置
    private void backToMyLocation() {
        LatLng cenpt = new LatLng(latitude, longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(12)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected void onPause() {
        convenientBanner.stopTurning();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
