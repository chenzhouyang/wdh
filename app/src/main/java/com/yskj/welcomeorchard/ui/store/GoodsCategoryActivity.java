package com.yskj.welcomeorchard.ui.store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseFragmentActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.GoodsCategoryListEntity;
import com.yskj.welcomeorchard.fragment.FragmentInterface;
import com.yskj.welcomeorchard.fragment.GoodCateFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/12.
 * 商品分类
 */

public class GoodsCategoryActivity extends BaseFragmentActivity implements View.OnClickListener, FragmentInterface.OnGetUrlListener{
    private ArrayList<String> toolsList = new ArrayList<>();
    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflater;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private ViewPager shop_pager;
    private int currentItem = 0;
    private ShopAdapter shopAdapter;
    private GoodsCategoryListEntity registerEntity;
    private TextView toolsLeftTextViews[];
    private ImageView imgBack;

    private EditText edKeyword;
    private TextView tvRightText;
    public SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_goods_category);

        initView();
        initData();
    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.tools_scrlllview);
        shopAdapter = new ShopAdapter(getSupportFragmentManager());
        inflater = LayoutInflater.from(this);

        imgBack = (ImageView) findViewById(R.id.img_back);
        edKeyword = (EditText) findViewById(R.id.ed_keyword);
        tvRightText = (TextView) findViewById(R.id.tv_right_text);
        imgBack.setOnClickListener(this);
        edKeyword.setOnClickListener(this);
        tvRightText.setOnClickListener(this);
    }

    //初始化数据
    private void initData() {
        OkHttpUtils.get().url(Urls.GOODSCATEGORYLIST).build().execute(new GoodsCategotyListCallBack());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ed_keyword:
                goToSearch();
                break;
            case R.id.tv_right_text:
                goToSearch();
                break;
        }
    }

    //跳转到搜索页面
    private void goToSearch() {
        Intent intent = new Intent(GoodsCategoryActivity.this, SearchActivity.class);
        intent.putExtra("from", "goodsCategory");
        startActivity(intent);
    }

    @Override
    public void getUrl(String id) {

    }

    @Override
    public void getData(String goodsId) {

    }

    //用于tab选择位置时回掉
    @Override
    public void getfirstPosition(int firstPosition) {
        sp = getSharedPreferences("UserInfor", 0);
        sp.edit().putInt("tabPage", firstPosition).commit();
        finish();
    }

    @Override
    public void getTwoPosition(int firstPosition, int twoPosition) {
        Intent intent = new Intent(GoodsCategoryActivity.this, GoodsCategoryDetailActivity.class);
        intent.putExtra("firstPosition", firstPosition);
        intent.putExtra("twoPosition", twoPosition);
        startActivity(intent);
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
            if (response.errorCode.equals("000")) {
                showToolsView(response);
            }
        }
    }

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        shop_pager = (ViewPager) findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if (shop_pager.getCurrentItem() != arg0) shop_pager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView(GoodsCategoryListEntity response) {
        for (int i = 0; i < response.goodsCategoryTree.size(); i++) {
            toolsList.add(response.goodsCategoryTree.get(i).mobileName);
            toolsTextViews = new TextView[response.goodsCategoryTree.size()];
            toolsLeftTextViews = new TextView[response.goodsCategoryTree.size()];
            views = new View[response.goodsCategoryTree.size()];
        }
        for (int i = 0; i < response.goodsCategoryTree.size(); i++) {

            LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.tools);
            View view = inflater.inflate(R.layout.item_good_category_left, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.typename);
            textView.setText(toolsList.get(i));
            TextView textViewLeft = (TextView) view.findViewById(R.id.tv_left_red_line);
            textViewLeft.setText("");
            toolsLayout.addView(view);
            toolsTextViews[i] = textView;
            toolsLeftTextViews[i] = textViewLeft;
            views[i] = view;
        }
        initPager();
        changeTextColor(0);
    }

    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != id) {
                toolsTextViews[i].setBackgroundResource(android.R.color.white);
                toolsLeftTextViews[i].setBackgroundResource(android.R.color.white);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffe42020);
        toolsLeftTextViews[id].setBackgroundResource(R.color.goods_category_red);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());
        }
    };

    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = new GoodCateFragment();
            Bundle bundle = new Bundle();
            String str = toolsList.get(arg0);
            bundle.putString("typename", str);
            bundle.putSerializable("list", registerEntity.goodsCategoryTree.get(arg0).tmenu);
            bundle.putInt("firstPosition", arg0);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return toolsList.size();
        }
    }
}
