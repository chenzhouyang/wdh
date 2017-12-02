package com.yskj.welcomeorchard.ui.revenue;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.RevenueAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ChangeOtherDialog;
import com.yskj.welcomeorchard.dialog.DateChooserDialog;
import com.yskj.welcomeorchard.http.RevenueEntiity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/19.
 */
//账单

public class RevenueActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.revenue_pull_lv)
    PullToRefreshListView revenuePullLv;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.shouyi_view)
    LinearLayout shouyiView;
    private int years, months,count = 10,cursor = 0;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ArrayList<RevenueEntiity.DataBean.ListBean> listBeenAll = new ArrayList<>();
    private ArrayList<RevenueEntiity.DataBean.ListBean> listBeenpage = new ArrayList<>();
    private RevenueAdapter adapter;
    private List<String> mYearList = new ArrayList<>();
    private List<String> mMountList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        ButterKnife.bind(this);
        txtTitle.setText("账单");
        //获取账单列表
        getreveulist();
        intiview();
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        years = t.year;
        months = t.month + 1;
        adapter = new RevenueAdapter(context,listBeenAll);
        revenuePullLv.setAdapter(adapter);
    }

    //上拉下拉刷新
    private void intiview(){
        revenuePullLv.setMode(PullToRefreshBase.Mode.BOTH);
        revenuePullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor = 0;
                if(time.getText().toString().equals("本月")){
                    getreveulist();
                }else {
                    getyearmonthlist();
                }
                MethodUtils.stopRefresh(revenuePullLv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor++;
                if(time.getText().toString().equals("本月")){
                    getreveulist();
                }else {
                    getyearmonthlist();
                }
                MethodUtils.stopRefresh(revenuePullLv);
            }
        });
    }
    //默认账单
    private void getreveulist() {
        OkHttpUtils.post().url(Urls.REVEBU).addHeader("Authorization",caches.get("access_token"))
                .addParams("accTypes","10,11,12,13")
                .addParams("count",count+"").addParams("cursor",cursor*10+"")
                .build().execute(new RevenuCallBack(context));
    }
    //根据年月查询账单
    private void getyearmonthlist(){
        OkHttpUtils.post().url(Urls.REVEBU).addHeader("Authorization",caches.get("access_token"))
                .addParams("accTypes","10,11,12,13")
                .addParams("year",years+"").addParams("month",months+"")
                .addParams("count",count+"").addParams("cursor",cursor*10+"")
                .build().execute(new RevenuCallBack(context));
    }
    @OnClick({R.id.img_back, R.id.shouyi_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.shouyi_view:
                showExitGameAlert();
                break;
        }
    }
    private void showExitGameAlert() {

        Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        int year=calendar.get(Calendar.YEAR);
        for (int i = 2010; i <= year; i++) {
            mYearList.add(i+"");
        }
        for (int i = 1; i <=12 ; i++) {
            mMountList.add(i+"");
        }
        DateChooserDialog dialog = new DateChooserDialog(context);
        dialog.show();
        dialog.setAddressData(mYearList,mMountList);
        dialog.setAddresskListener(new DateChooserDialog.OnAddressCListener() {
            @Override
            public void onClick(String year, String mount) {
                years = Integer.parseInt(year);
                months = Integer.parseInt(mount) ;
                time.setText(+ years +"年" + months +"月");
                cursor = 0;
                getyearmonthlist();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    public class RevenuCallBack extends Callback<RevenueEntiity> {
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

        private Context context;

        public RevenuCallBack(Context context) {
            super();
            this.context = context;

        }

        @Override
        public RevenueEntiity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            RevenueEntiity revenueEntiity = new Gson().fromJson(s, new TypeToken<RevenueEntiity>() {
            }.getType());
            return revenueEntiity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(RevenueEntiity response, int id) {
            if (response.code == 0) {
                listBeenpage.clear();
                listBeenpage=response.data.list;
                if(listBeenpage.size()==0){
                    Toast.makeText(context,"没有数据了哦",Toast.LENGTH_SHORT).show();
                }else {
                    if(cursor == 0){
                        listBeenAll.clear();
                    }
                    listBeenAll.addAll(listBeenpage);
                    adapter.notifyDataSetChanged();
                }


            }
        }
    }

}
