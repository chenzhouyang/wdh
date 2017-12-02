package com.yskj.welcomeorchard.ui.income;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.IncomeAdapter;
import com.yskj.welcomeorchard.adapter.IncomeFansAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.IncomeEntitiy;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/13.
 */

public class IncomeActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.income_name)
    TextView incomeName;
    @Bind(R.id.income_user)
    TextView incomeUser;
    @Bind(R.id.income_level)
    TextView incomeLevel;
    @Bind(R.id.income_date)
    TextView incomeDate;
    @Bind(R.id.income_pull_lv)
    PullToRefreshListView incomePullLv;
    private int index = 0,max = 10;//每页的开始标识,每页的最大记录数
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ArrayList<IncomeEntitiy.DataBean> listAll = new ArrayList<>();
    private ArrayList<IncomeEntitiy.DataBean> listpage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        if(getIntent().getStringExtra("income").equals("0")){
            txtTitle.setText("邀请明细");
            incomeName.setText("用户手机");
            incomeUser.setText("用户姓名");
            incomeLevel.setText("等级称谓");
            incomeDate.setText("注册日期");
            getfans();
        }else{
            txtTitle.setText("客户收入");
            getfansincome();
        }
        initscrollview();

    }
//得到客户分享记录
    private void getfans() {
        OkHttpUtils.get().url(Urls.FINDPAGINGLIST+"/"+index*10+"/"+max).addHeader("Authorization",caches.get("access_token"))
                .build().execute(new IncomeCallBalck(context,index,getIntent().getStringExtra("income")));
    }
    //得到客户收入记录
    private void getfansincome() {
        OkHttpUtils.get().url(Urls.FINDPAGINGLIST+"/"+index*10+"/"+max).addHeader("Authorization",caches.get("access_token"))
                .build().execute(new IncomeCallBalck(context,index,getIntent().getStringExtra("income")));
    }

    //刷新
    private void initscrollview(){
        incomePullLv.setMode(PullToRefreshBase.Mode.BOTH);
        incomePullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                if(getIntent().getStringExtra("income").equals("0")){
                    getfans();
                }else {
                    getfansincome();
                }
                MethodUtils.stopRefresh(incomePullLv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                if(getIntent().getStringExtra("income").equals("0")){
                    getfans();
                }else {
                    getfansincome();
                }
                MethodUtils.stopRefresh(incomePullLv);
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }

    public class IncomeCallBalck extends Callback<IncomeEntitiy> {
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
        private IncomeFansAdapter adapter;
        private IncomeAdapter incomeAdapter;
        private int index;
        private String Type;
        public IncomeCallBalck(Context context,int index,String Type){
            super();
            this.context = context;
            this.index = index;
            this.Type = Type;
            if(Type.equals("0")){
                adapter = new IncomeFansAdapter(context,listAll);
                incomePullLv.setAdapter(adapter);
            }else {
                incomeAdapter = new IncomeAdapter(context,listAll);
                incomePullLv.setAdapter(incomeAdapter);
            }

        }
        @Override
        public IncomeEntitiy parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            IncomeEntitiy incomeEntitiy = new Gson().fromJson(s,new TypeToken<IncomeEntitiy>(){}.getType());
            return incomeEntitiy;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
                isLogin(e);
        }

        @Override
        public void onResponse(IncomeEntitiy response, int id) {
            if(response.code == 0){
                listpage.clear();
                listpage.addAll(response.data);
                if(listpage.size()!=0){
                    if(index == 0){
                        listAll.clear();
                    }

                    listAll.addAll(listpage);
                    if (Type.equals("0")){
                        adapter.notifyDataSetChanged();
                    }else {
                        incomeAdapter.notifyDataSetChanged();
                    }

                }else {
                    showToast("没有数据了");
                }
            }else {
                 showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }

}
