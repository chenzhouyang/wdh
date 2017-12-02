package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.CatograyAdapter;
import com.yskj.welcomeorchard.adapter.GoodsAdapter;
import com.yskj.welcomeorchard.adapter.GoodsDetailAdapter;
import com.yskj.welcomeorchard.adapter.ProductAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.CatograyBean;
import com.yskj.welcomeorchard.entity.GoodsBean;
import com.yskj.welcomeorchard.entity.ItemBean;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.MeacherEntitiy;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.localserver.LocalServerDetailActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.view.MyListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 作者：陈宙洋
 * 日期：2017/8/01
 * 描述：本地生活商品列表
 */
public class ChooseDishActivity extends BaseActivity implements GoodsAdapter.ModifySkip{
    //控件
    private ListView lv_catogary, lv_good;
    private ImageView iv_logo,choose_image,image_back,tv_car;
    private TextView choose_name,choose_location,choose_propagation;
    private  TextView tv_count,tv_totle_money;
    Double totleMoney = 0.00;
    private TextView bv_unm;
    private LinearLayout rl_bottom;
    //分类和商品
    private List<CatograyBean> list = new ArrayList<CatograyBean>();
    private CatograyAdapter catograyAdapter;//分类的adapter
    private GoodsAdapter goodsAdapter;//分类下商品adapter
    ProductAdapter productAdapter;//底部购物车的adapter
    GoodsDetailAdapter goodsDetailAdapter;//套餐详情的adapter
    private static DecimalFormat df;
    private LinearLayout ll_shopcar;
    //底部数据
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    private SparseArray<GoodsBean.DataBean.LocalLifesBean> selectedList;
    //套餐
    private View bottomDetailSheet;
    private List<GoodsBean.DataBean.LocalLifesBean> listBean = new ArrayList<>();
    private Handler mHanlder;
    private ViewGroup anim_mask_layout;//动画层
    private int cursor = 0;
    private String shopid,sell,distace;
    private LoadingCaches caches = LoadingCaches.getInstance();
    double allCloudOffse = 0.00;//抵充总积分
    double allAmount = 0.00;//总金额
    private String listgoods = "null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dish);
        mHanlder = new Handler(getMainLooper());
        initView();
        initData();
        addListener();
        getmerchant();
        tv_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet();
            }
        });
    }
    //请求商家信息
    private void getmerchant(){
        OkHttpUtils.get().url(Urls.SHOPDETAIL).addParams("shopId",shopid).build().execute(new StringCallback() {
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
                MeacherEntitiy macherentity = new Gson().fromJson(response,new TypeToken<MeacherEntitiy>(){}.getType());
                if(macherentity.code == 0){
                    GlideImage.loadImage(context,choose_image,macherentity.data.localShop.cover,R.mipmap.default_image);
                    choose_name.setText(macherentity.data.localShop.shopName);
                    choose_location.setText(macherentity.data.localShop.detailAddress);
                    choose_propagation.setText(macherentity.data.localShop.shopName);

                }
            }
        });
    }
//初始化组件
    public void initView() {
        choose_image = (ImageView) findViewById(R.id.choose_image);
        image_back = (ImageView) findViewById(R.id.image_back);
        choose_name = (TextView) findViewById(R.id.choose_name);
        choose_location = (TextView) findViewById(R.id.choose_location);
        choose_propagation = (TextView) findViewById(R.id.choose_propagation);
        lv_catogary = (ListView) findViewById(R.id.lv_catogary);
        lv_good = (ListView) findViewById(R.id.lv_good);
        tv_car = (ImageView) findViewById(R.id.tv_car);
        //底部控件
        rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);
        tv_count = (TextView) findViewById(R.id.tv_count);
        bv_unm = (TextView) findViewById(R.id.bv_unm);
        tv_totle_money= (TextView) findViewById(R.id.tv_totle_money);
        ll_shopcar= (LinearLayout) findViewById(R.id.ll_shopcar);
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
        selectedList = new SparseArray<>();
        df = new DecimalFormat("0.00");
        tv_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caches.get("access_token").equals("null")){
                    startActivity(new Intent(context, LoginActivity.class));
                }
                if(selectedList!=null&&selectedList.size()>0){
                    provisionalOrder();
                }else {
                    showToast("请选择商品");
                }

            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CatograyBean catograyBean3 = new CatograyBean();
        catograyBean3.setCount(3);
        catograyBean3.setKind("推荐");
        catograyBean3.setList(listBean);
        list.add(catograyBean3);
        //默认值
        listBean.clear();
        listBean.addAll(list.get(0).getList());
        shopid = getIntent().getStringExtra("shopid");
        sell = getIntent().getStringExtra("address");
        distace = getIntent().getStringExtra("distance");
        //分类
        catograyAdapter = new CatograyAdapter(context, list);
        lv_catogary.setAdapter(catograyAdapter);
        catograyAdapter.notifyDataSetChanged();
        //商品
        goodsAdapter = new GoodsAdapter(context, listBean, catograyAdapter);
        goodsAdapter.setModifySkip(this);
        lv_good.setAdapter(goodsAdapter);
        goodsAdapter.notifyDataSetChanged();
    }
/**
 * 创建临时订单
 */
private void provisionalOrder(){
    OkHttpUtils.post().url(Urls.LOCALINGTEMPORARY+ "?" + listgoods.replace("null&", ""))
            .addHeader("Authorization", caches.get("access_token"))
            .addParams("orderType","2")
            .build().execute(new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int i) {
            isLogin(e);
        }

        @Override
        public void onResponse(String s, int i) {
            Map<String, Object> map = JSONFormat.jsonToMap(s);
            if(map!=null){
                int code = (int) map.get("code");
                if(code == 0){
                    ScanCodeTmpEntity setUpEntity = new Gson().fromJson(s, ScanCodeTmpEntity.class);
                    startActivity(new Intent(context, OrderPayActivity.class).putExtra("type","2")
                    .putExtra("allAmount", allAmount+"")
                    .putExtra("allCloudOffse",allCloudOffse+"")
                    .putExtra("tid",setUpEntity.data.orderId+""));
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }

        }
    });
            


}
    /**
     * 跳转详情
     * @param postion
     */
    @Override
    public void skip(int postion) {
        startActivity(new Intent(context, LocalServerDetailActivity.class)
                .putExtra("life_id",listBean.get(postion).lifeId+"")
                .putExtra("sell",listBean.get(postion).saleCount+"")
                .putExtra("distance",distace));
    }
    //填充数据
    private void initData() {
        OkHttpUtils.get().url(Urls.SHOPID)
                .addParams("shopId",shopid)
                .addParams("cursor",cursor+"")
                .addParams("count","10")
                .build().execute(new StringCallback() {
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
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                GoodsBean goodsBean = new Gson().fromJson(response,new TypeToken<GoodsBean>(){}.getType());
                if(goodsBean.code == 0){

                   listBean.addAll(goodsBean.data.localLifes);
                    goodsAdapter.notifyDataSetChanged();
                }
            }
        });


    }
    //添加监听
    private void addListener() {
//        lv_catogary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listBean.clear();
//                listBean.addAll(list.get(position).getList());
//                catograyAdapter.setSelection(position);
//                catograyAdapter.notifyDataSetChanged();
//                goodsAdapter.notifyDataSetChanged();
//            }
//        });
    }
    //创建套餐详情view
    public void showDetailSheet(List<ItemBean> listItem, String mealName){
        bottomDetailSheet = createMealDetailView(listItem,mealName);
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(listItem.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomDetailSheet);
            }
        }
    }
    //查看套餐详情
    private View createMealDetailView(List<ItemBean> listItem, String mealName){
        View view = LayoutInflater.from(this).inflate(R.layout.activity_goods_detail,(ViewGroup) getWindow().getDecorView(),false);
        ListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView tv_meal = (TextView) view.findViewById(R.id.tv_meal);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);
        int count=0;
        for(int i=0;i<listItem.size();i++){
            count = count+Integer.parseInt(listItem.get(i).getNote2());
        }
        tv_meal.setText(mealName);
        tv_num.setText("(共"+count+"件)");
        goodsDetailAdapter = new GoodsDetailAdapter(ChooseDishActivity.this,listItem);
        lv_product.setAdapter(goodsDetailAdapter);
        goodsDetailAdapter.notifyDataSetChanged();
        return view;
    }
    //创建购物车view
    private void showBottomSheet(){
        bottomSheet = createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else {
            if(selectedList.size()!=0){
                bottomSheetLayout.showWithSheetView(bottomSheet);
            }
        }
    }
    //查看购物车布局
    private View createBottomSheetView(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet,(ViewGroup) getWindow().getDecorView(),false);
        MyListView lv_product = (MyListView) view.findViewById(R.id.lv_product);
        TextView clear = (TextView) view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
        productAdapter = new ProductAdapter(ChooseDishActivity.this,goodsAdapter, selectedList);
        lv_product.setAdapter(productAdapter);
        return view;
    }
    //清空购物车
    public void clearCart(){
        selectedList.clear();
        if (list.size() > 0) {
            for (int j=0;j<list.size();j++){
                list.get(j).setCount(0);
                for(int i=0;i<list.get(j).getList().size();i++){
                    list.get(j).getList().get(i).setNum(0);
                }
            }
        }
        update(true);
    }

    //根据商品id获取当前商品的采购数量
    public int getSelectedItemCountById(int id){
        GoodsBean.DataBean.LocalLifesBean temp = selectedList.get(id);
        if(temp==null){
            return 0;
        }
        return temp.getNum();
    }
    public void handlerCarNum(int type, GoodsBean.DataBean.LocalLifesBean goodsBean, boolean refreshGoodList){
        if (type == 0) {
            GoodsBean.DataBean.LocalLifesBean temp = selectedList.get(goodsBean.lifeId);
            if(temp!=null){
                if(temp.getNum()<2){
                    goodsBean.setNum(0);
                    selectedList.remove(goodsBean.lifeId);

                }else{
                    int i =  goodsBean.getNum();
                    goodsBean.setNum(--i);
                }
            }
        } else if (type == 1) {
            GoodsBean.DataBean.LocalLifesBean temp = selectedList.get(goodsBean.lifeId);
            if(temp==null){
                goodsBean.setNum(1);
                selectedList.append(goodsBean.lifeId, goodsBean);
            }else{
                int i= goodsBean.getNum();
                goodsBean.setNum(++i);
            }
        }
        update(refreshGoodList);

    }
    //刷新布局 总价、购买数量等
    private void update(boolean refreshGoodList){
        int size = selectedList.size();
        int count =0;
        listgoods = "null";
        allCloudOffse = 0.00;
        for(int i=0;i<size;i++){
            GoodsBean.DataBean.LocalLifesBean item = selectedList.valueAt(i);
            count += item.getNum();
            totleMoney += DoubleUtils.mul(item.getNum(),item.teamBuyPrice);
            allCloudOffse+=DoubleUtils.mul(item.cloudOffset,item.getNum());
            listgoods = listgoods +"&goods=" +item.lifeId+"-"+item.getNum();
        }
        allAmount = totleMoney;
        tv_totle_money.setText("￥"+String.valueOf(df.format(totleMoney)));
        totleMoney = 0.00;
        if(count<1){
            tv_car.setImageResource(R.mipmap.locenserver_shopping_cart_grey);
            bv_unm.setVisibility(View.GONE);
        }else{
            tv_car.setImageResource(R.mipmap.locenserver_shopping_cart);
            bv_unm.setVisibility(View.VISIBLE);
        }

        bv_unm.setText(String.valueOf(count));

        if(productAdapter!=null){
            productAdapter.notifyDataSetChanged();
        }

        if(goodsAdapter!=null){
            goodsAdapter.notifyDataSetChanged();
        }

        if(catograyAdapter!=null){
            catograyAdapter.notifyDataSetChanged();
        }

        if(bottomSheetLayout.isSheetShowing() && selectedList.size()<1){
            bottomSheetLayout.dismissSheet();
        }
    }


    /**
     * @Description: 创建动画层
     * @param
     * @return void
     * @throws
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        tv_car.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 40;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(800);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }



}
