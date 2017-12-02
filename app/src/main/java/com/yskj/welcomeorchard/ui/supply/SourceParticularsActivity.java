package com.yskj.welcomeorchard.ui.supply;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SourceParAdapter;
import com.yskj.welcomeorchard.adapter.SourceParticularsAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.ParticularsEntitiy;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.yskj.welcomeorchard.widget.PullUpToLoadMore;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 陈宙洋
 * 2017/8/3.
 * 描述：微商货源商品详情activity
 */

public class SourceParticularsActivity extends BaseActivity implements SourceParAdapter.CheckInterface,SourceParAdapter.ModifyCountInterface{
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.particulars_goods_name)
    TextView particularsGoodsName;
    @Bind(R.id.particulars_describe)
    TextView particularsDescribe;
    @Bind(R.id.particulars_price)
    TextView particularsPrice;
    @Bind(R.id.particulars_sales)
    TextView particularsSales;
    @Bind(R.id.commodity_show_property)
    TextView commodityShowProperty;
    @Bind(R.id.commodity_property)
    LinearLayout commodityProperty;
    @Bind(R.id.commodity_web)
    ListView commodityWeb;
    @Bind(R.id.source_consume_integral)
    TextView sourceConsumeIntegral;
    @Bind(R.id.ptlm)
    PullUpToLoadMore ptlm;
    @Bind(R.id.tv_put_cart)
    TextView tvPutCart;
    @Bind(R.id.tv_buy)
    TextView tvBuy;
    @Bind(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @Bind(R.id.particulars_rb)
    RadioButton particularsRb;
    private ArrayList image;
    private ParticularsEntitiy commodityEntity;
    private PopupWindow mPropertyWindow;
    private TextView nameTv, tv_good_art, priceTv, propertyTv;
    private EditText amountTv;
    private int  mCount = 1,millId;
    private String goodsid,goodsname,imageurl;
    private List<ParticularsEntitiy.DataBean.ParameterListBean> standardlist = new ArrayList<>();
    private SourceParAdapter sourceParAdapter;
    private LocalServerEntity localServerEntity;
    private LocalServerNumEntity localServerNumEntity;
    private String shopname;
    private boolean isCheckd = false;
    private List<LocalServerEntity> mListGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sourceparticulars);
        ButterKnife.bind(this);
        txtTitle.setText("商品详情");
        setDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mPropertyWindow!=null){
            mPropertyWindow.dismiss();
        }
    }

    /**
     * 网络请求数据
     */
    private void setDate() {
        imageurl = getIntent().getStringExtra("imageurl");
        goodsid = getIntent().getStringExtra("goodsid");
        shopname = getIntent().getStringExtra("shopname");
        OkHttpUtils.get().url(Urls.BOUNTSDETIAL)
                .addParams("goodsId",goodsid).build().execute(new CommodityCallBack());
    }

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

    @OnClick({R.id.img_back, R.id.particulars_rb, R.id.tv_put_cart, R.id.tv_buy,R.id.commodity_property})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //返回键
                finish();
                break;
            case R.id.particulars_rb:
                //商铺
                startActivity(new Intent(context,SupplyActivity.class).putExtra("millid",millId+""));
                break;
            case R.id.tv_put_cart:
                //加入购物车

                showPropertyWindow();
                break;
            case R.id.tv_buy:
                //立即购买
                showPropertyWindow();
                break;
            case R.id.commodity_property:
                showPropertyWindow();
                break;
        }
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

    /**
     * 获取数据进行实例化
     */
    private class CommodityCallBack extends Callback<ParticularsEntitiy> {
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
        public ParticularsEntitiy parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            commodityEntity = new Gson().fromJson(s, new TypeToken<ParticularsEntitiy>() {
            }.getType());
            return commodityEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
                isLogin(e);
        }

        @Override
        public void onResponse(ParticularsEntitiy response, int id) {
            if (response.code == 0) {
                //添加轮播图
                image = new ArrayList();
                ArrayList<ParticularsEntitiy.DataBean.ImageListBean> adList = response.data.imageList;
                if (adList != null && adList.size() != 0) {
                    for (int i = 0; i < adList.size(); i++) {
                        image.add(adList.get(i).title);
                    }
                }
                millId = response.data.millId;
                goodsname = response.data.name;
                standardlist.addAll(response.data.parameterList);
                particularsGoodsName.setText("【"+shopname+"】"+response.data.name);
                particularsDescribe.setText(response.data.name);
                sourceConsumeIntegral.setText("赠送积分"+StringUtils.getStringtodouble(DoubleUtils.div(response.data.mAccount,10,2)));
                particularsPrice.setText("¥"+ StringUtils.getStringtodouble(response.data.price));
                particularsSales.setText("已售："+response.data.volume);
                if(response.data.contentList.size()>0){
                    SourceParticularsAdapter adapter = new SourceParticularsAdapter(context,response.data.contentList);
                    commodityWeb.setAdapter(adapter);
                }

                initCB();
            }
        }

    }
    /**
     * 选择套餐
     */
    private void showPropertyWindow() {
        if (mPropertyWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.sourceparticlars_select_layout, null);
            mPropertyWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPropertyWindow.setContentView(contentView);
//在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
            mPropertyWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPropertyWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            TextView tv_put_true = (TextView) contentView.findViewById(R.id.tv_put_true);
            ImageView imgView_close = (ImageView) contentView.findViewById(R.id.imgView_close);
//点击空白处时，隐藏掉pop窗口
            mPropertyWindow.setFocusable(true);
            mPropertyWindow.setBackgroundDrawable(new BitmapDrawable());
            backgroundAlpha(1f);
            //添加pop窗口关闭事件
            mPropertyWindow.setOnDismissListener(new poponDismissListener());
            mPropertyWindow.setAnimationStyle(R.style.AnimationSEX);
            NoScrollListView propertyLv = (NoScrollListView) contentView.findViewById(R.id.lv_commodity_property);
            View placeholderView = contentView.findViewById(R.id.view_placeholder);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.imgView_close:

                            for (int i = 0; i < standardlist.size(); i++) {
                                if(standardlist.get(i).isGroupSelected()){
                                    DataSupport.deleteAll(LocalServerNumEntity.class,"parameterid=?",standardlist.get(i).id+"");
                                }
                            }
                            mListGoods = DataSupport.findAll(LocalServerEntity.class);
                            for (LocalServerEntity item : mListGoods) {
                                List<LocalServerNumEntity> list = DataSupport.where("shopid=?",item.getShopid()+"").find(LocalServerNumEntity.class);
                                if(list.size()<=0){
                                    DataSupport.deleteAll(LocalServerEntity.class,"shopid=?",item.getShopid()+"");
                                }
                            }
                            clearCheck();
                            mPropertyWindow.dismiss();
                            mPropertyWindow.setOnDismissListener(new poponDismissListener());
                            break;
                        case R.id.tv_put_true:
                            for (int i = 0; i < standardlist.size(); i++) {
                                if(standardlist.get(i).isGroupSelected()){
                                    isCheckd = true;
                                }
                            }
                            if(isCheckd){
                                startActivity(new Intent(context,ShoppingCartActivity.class));
                            }else {
                                showToast("您还没有选择商品规格哦");
                            }
                            break;
                    }
                }
            };
            tv_put_true.setOnClickListener(clickListener);
            imgView_close.setOnClickListener(clickListener);
            placeholderView.setOnClickListener(clickListener);
            sourceParAdapter = new SourceParAdapter(context, standardlist);
            sourceParAdapter.setCheckInterface(this);//更改选中状态
            sourceParAdapter.setModifyCountInterface(this);//更新数量
            propertyLv.setAdapter(sourceParAdapter);
        }
        if (!mPropertyWindow.isShowing()) {
            mPropertyWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }


    /**
     * 取消时去除选中状态
     */
    public void clearCheck(){
        for (int i = 0; i <standardlist.size(); i++) {
            standardlist.get(i).setGroupSelected(false);
        }
        sourceParAdapter.notifyDataSetChanged();
    }
    /**
     * 规格更改选中
     * @param position  元素位置
     * @param isChecked 元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = standardlist.get(position);
        standardlist.get(position).setGroupSelected(isChecked);
        sourceParAdapter.notifyDataSetChanged();
        if(parameterListBean.isGroupSelected()){
            List<LocalServerNumEntity> shoppingDetailEntityList = DataSupport.where("parameterid = ?", parameterListBean.id+"").find(LocalServerNumEntity.class);
            List<LocalServerEntity> localServerEntityList = DataSupport.where("shopid = ?", millId+"").find(LocalServerEntity.class);
            if(localServerEntityList==null||localServerEntityList.size()==0){
                localServerEntity = new LocalServerEntity();
                localServerEntity.setGoodsid(goodsid);
                localServerEntity.setParameterid(parameterListBean.id);
                localServerEntity.setShopid(millId+"");
                localServerEntity.setShopname(shopname);
                localServerEntity.setEditing(false);
                localServerEntity.setGroupSelected(false);
                localServerEntity.save();
            }
            if(shoppingDetailEntityList==null||shoppingDetailEntityList.size()==0){
                localServerNumEntity = new LocalServerNumEntity();
                localServerNumEntity.setLocalServerEntity(localServerEntity);
                localServerNumEntity.setCount(parameterListBean.getNum());
                localServerNumEntity.setParameterid(parameterListBean.id);
                localServerNumEntity.setParametername(parameterListBean.name);
                localServerNumEntity.setShopid(millId+"");
                localServerNumEntity.setGoodsimage(imageurl);
                localServerNumEntity.setMaccount(parameterListBean.mAccount);
                localServerNumEntity.setGoodsid(goodsid);
                localServerNumEntity.setEditing(false);
                localServerNumEntity.setChildSelected(false);
                localServerNumEntity.setPrice(parameterListBean.price);
                localServerNumEntity.setGoodsname(goodsname);
                localServerNumEntity.save();
            }else {
                int currount = 0;
                for (LocalServerNumEntity item : shoppingDetailEntityList) {
                    if(item.getParameterid() == parameterListBean.id){
                        currount = item.getCount();
                    }
                }
                LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
                localServerUpdateNumEntity.setCount(parameterListBean.getNum()+currount);
                localServerUpdateNumEntity.updateAll("parameterid = ?",parameterListBean.id+"");
            }
        }else {
            DataSupport.deleteAll(LocalServerNumEntity.class,"parameterid = ?",parameterListBean.id+"");
        }

    }

    /**
     * 增加数量
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param stock       库存
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, int stock, boolean isChecked) {
        ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = standardlist.get(position);
        int currentCount = parameterListBean.getNum();
        currentCount++;
        if(currentCount>stock){
            showToast("数量超出库存");
        }else {
            parameterListBean.setNum(currentCount);
            ((TextView) showCountView).setText(currentCount + "");
            sourceParAdapter.notifyDataSetChanged();
            if(parameterListBean.isGroupSelected()){
                LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
                localServerUpdateNumEntity.setCount(parameterListBean.getNum());
                localServerUpdateNumEntity.updateAll("parameterid = ?",parameterListBean.id+"");
            }
        }
    }

    /**
     * 减少数量
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = standardlist.get(position);
        int currentCount = parameterListBean.getNum();
        if(currentCount == 1){
            return;
        }
        currentCount--;
        parameterListBean.setNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        sourceParAdapter.notifyDataSetChanged();
        if(parameterListBean.isGroupSelected()){
            LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
            localServerUpdateNumEntity.setCount(parameterListBean.getNum());
            localServerUpdateNumEntity.updateAll("parameterid = ?",parameterListBean.id+"");
        }
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }
}
