package com.yskj.welcomeorchard.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.PageFragmentAdapter;
import com.yskj.welcomeorchard.base.BaseFragmentActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.GoodsCategoryListEntity;
import com.yskj.welcomeorchard.fragment.FragmentInterface;
import com.yskj.welcomeorchard.fragment.MainFragment;
import com.yskj.welcomeorchard.ui.buyersShow.BuyersShowListActivity;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/18.
 */

public class GoodsCategoryDetailActivity extends BaseFragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener,
        FragmentInterface.OnGetUrlListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.rgTab)
    RadioGroup rgTab;
    @Bind(R.id.hvTab)
    HorizontalScrollView hvTab;
    @Bind(R.id.vpList)
    ViewPager viewPager;

    private int firstPosition;
    private int twoPosition;
    private GoodsCategoryListEntity registerEntity;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private PageFragmentAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_goods_category_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        firstPosition = getIntent().getIntExtra("firstPosition",0);
        twoPosition = getIntent().getIntExtra("twoPosition",0);
        imgBack.setOnClickListener(this);

        viewPager.setOnPageChangeListener(this);
        initData();
    }

    public void initData() {
        OkHttpUtils.get().url(Urls.GOODSCATEGORYLIST).build().execute(new GoodsCategotyListCallBack());
    }

    @Override
    public void getUrl(String url) {
        if (url!=null){
            Intent intent = new Intent(GoodsCategoryDetailActivity.this, CommodityDetailsActiviy.class);
            intent.putExtra("goodid",url);
            startActivity(intent);
        }
    }

    @Override
    public void getData(String goodsId) {
        if (goodsId != null) {
            Intent intent = new Intent(GoodsCategoryDetailActivity.this, BuyersShowListActivity.class);
            intent.putExtra("goodsId", goodsId);
            startActivity(intent);
        }
    }

    @Override
    public void getfirstPosition(int firstPosition) {

    }

    @Override
    public void getTwoPosition(int firstPosition, int twoPosition) {

    }

    @Override
    public void getBandle(Bundle bundle) {

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
            if (response.errorCode.equals("000")){
                initTab(response);
            }
        }
    }

    private void initTab(GoodsCategoryListEntity response) {
        final List<String> channelList = new ArrayList<>();
        ArrayList<GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean> tmenuList = response.goodsCategoryTree.get(firstPosition).tmenu;
        for (int i = 0; i < tmenuList.size(); i++) {
            channelList.add(tmenuList.get(i).mobileName);
        }
        for (int i = 0; i < channelList.size(); i++) {
            RadioButton rb = (RadioButton) LayoutInflater.from(this).inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i).toString());
            rgTab.addView(rb);
        }
        initViewPager(response);
        //默认选择上个界面传过来的position
        rgTab.check(twoPosition);
        viewPager.setCurrentItem(twoPosition);
        txtTitle.setText(channelList.get(twoPosition));
        rgTab.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        viewPager.setCurrentItem(checkedId);
                        txtTitle.setText(channelList.get(checkedId));
                    }
                });
    }

    private void initViewPager(GoodsCategoryListEntity response) {

        ArrayList<GoodsCategoryListEntity.GoodsCategoryTreeBean.TmenuBean> tmenuList = response.goodsCategoryTree.get(firstPosition).tmenu;
        for (int i = 0; i < tmenuList.size() ; i++) {
            MainFragment mainFragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", tmenuList.get(i).id);
            mainFragment.setArguments(bundle);
            fragmentList.add(mainFragment);
        }
        adapter = new PageFragmentAdapter(super.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

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

    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     *
     * @param idx
     */
    private void setTab(int idx) {
        RadioButton rb = (RadioButton) rgTab.getChildAt(idx);
        rb.setChecked(true);
        int left = rb.getLeft();
        int width = rb.getMeasuredWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        super.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int len = left + width / 2 - screenWidth / 2;
        hvTab.smoothScrollTo(len, 0);//滑动ScroollView
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }


}
