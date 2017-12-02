package com.yskj.welcomeorchard.ui.localserver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SortAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.SortModel;
import com.yskj.welcomeorchard.utils.CharacterParser;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.PinyinComparator;
import com.yskj.welcomeorchard.utils.PinyinUtils;
import com.yskj.welcomeorchard.widget.SideBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


public class CityBusinessActivity extends BaseActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
//    private App app;
    private CharacterParser characterParser;
    private TextView txt_current_city;
    private String nameCity;
    private int cityId;
    private ImageView arrow_left;
    public MyLocationListenner myListener = new MyLocationListenner();
    public LocationClient mLocationClient = null;
    private List<SortModel> mSortList = new ArrayList<>();
    private boolean isFirstLoc =true;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_business_layout);
        initViews();
        initLocation();
        filledData();
    }

    private void initViews() {
        isFirstLoc = true; // 每次进来定位一次
        txt_current_city = (TextView) findViewById(R.id.txt_current_city);
        arrow_left = (ImageView) findViewById(R.id.arrow_left);
        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        characterParser = CharacterParser.getInstance();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if(adapter!=null){
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position);
                    }
                }

            }
        });
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                nameCity = ((SortModel) adapter.getItem(position)).getName();
                cityId = ((SortModel) adapter.getItem(position)).getCity_Id();
                Toast.makeText(getApplication(), nameCity, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("name", nameCity);
                intent.putExtra("cityId", cityId+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void filledData() {
        OkHttpUtils.get().url(Urls.BUSINESSCITY).build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                Logger.d(map.toString());
                int code = (int) map.get("code");
                if (code == 0) {
                    try {
                        JSONArray object = (JSONArray) map.get("data");
                        for (int a = 0; a < object.length(); a++) {
                            JSONObject ob = (JSONObject) object.get(a);
                            int cityId = ob.getInt("cityId");
                            String codes = ob.getString("code");
                            String fullName = ob.getString("fullName");
                            Boolean isOpen = ob.getBoolean("isOpen");
                            String name = ob.getString("name");
                            SortModel mBean = new SortModel();
                            mBean.setName(name);
                            mBean.setCity_Id(cityId);
                            names = new ArrayList<String>();
                            for (int i = 0; i < object.length(); i++) {
                                names.add(mBean.getName());
                                String pinyin = PinyinUtils.getPingYin(names.get(i));
                                String sortString = pinyin.substring(0, 1).toUpperCase();
                                if (sortString.matches("[A-Z]")) {
                                    mBean.setSortLetters(sortString.toUpperCase());
                                    if (!names.contains(sortString)) {
                                        names.add(sortString);
                                    }
                                }
                            }
                            mSortList.add(mBean);
                        }
                        Collections.sort(mSortList, new PinyinComparator());
                        adapter = new SortAdapter(context, mSortList);
                        sortListView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (isFirstLoc) {
                isFirstLoc = false;
                //经纬度
                String lati = location.getLatitude()+"";
                String longa = location.getLongitude()+"";
                sp.edit().putString(Config.SPKEY_LATITUDE, lati).commit();
                sp.edit().putString(Config.SPKEY_LONGITUDE, longa).commit();
                sp.edit().putString(Config.SPKEY_CITYNAME, location.getCity()).commit();

                String str = (location.getCity());
                txt_current_city.setText(str);
            }
        }
    }
    private void initLocation() {
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
        //int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }
}
