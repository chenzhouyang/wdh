package com.yskj.welcomeorchard.ui.commoditydetails;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.ProperGridViewAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ApproveDialog;
import com.yskj.welcomeorchard.dialog.HintLoginDialog;
import com.yskj.welcomeorchard.entity.CommodityEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.store.ShoppingCartActivity;
import com.yskj.welcomeorchard.utils.GlideCatchUtil;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.UMengShareUtils;
import com.yskj.welcomeorchard.widget.CountDownClock;
import com.yskj.welcomeorchard.widget.NoScrollGridView;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.yskj.welcomeorchard.widget.PullUpToLoadMore;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/4/13.
 */

public class CommodityDetailsActiviy extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.ptlm)
    PullUpToLoadMore ptlm;
    @Bind(R.id.commodity_goods_name)
    TextView commodityGoodsName;
    @Bind(R.id.commodity_describe)
    TextView commodityDescribe;
    @Bind(R.id.commodity_old_price)
    TextView commodityOldPrice;
    @Bind(R.id.commodity_sold)
    TextView commoditySold;
    @Bind(R.id.commodity_repertory)
    TextView commodityRepertory;
    @Bind(R.id.commodity_show_property)
    TextView commodityShowProperty;
    @Bind(R.id.commodity_property)
    LinearLayout commodityProperty;
    @Bind(R.id.commodity_price)
    TextView commodityPrice;
    @Bind(R.id.commodity_web)
    WebView commodityWeb;
    @Bind(R.id.commodity_share)
    ImageView commodityShare;
    @Bind(R.id.tv_put_cart)
    TextView tvPutCart;
    @Bind(R.id.tv_buy)
    TextView tvBuy;
    @Bind(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @Bind(R.id.count_down_clock)
    CountDownClock countDownClock;
    @Bind(R.id.commodity_count_down)
    LinearLayout commodityCountDown;
    @Bind(R.id.commodity_art)
    TextView commodityArt;
    @Bind(R.id.art_ll)
    LinearLayout artLl;
    private ArrayList image;
    private CommodityEntity commodityEntity;
    private PopupWindow mPropertyWindow;
    private static final int OPERATE_USER = 0;
    private int mOperate, mCount = 1;
    private ArrayList<String> commoditylist = new ArrayList<>();
    private ArrayList<CommodityEntity.StandsrdBean> gridviewlist = new ArrayList<>();
    private Map<String, ArrayList<CommodityEntity.StandsrdBean>> standardlist = new HashMap<>();
    private Map<String, CommodityEntity.NormsBean> normsBeanMap = new HashMap<>();
    private String allitemid = null;
    private Map<Integer, String> tagList = new HashMap<>();
    private TextView nameTv, tv_good_art, priceTv, propertyTv;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private String umurl, goodsid, imageurlid;
    private String  optype, commodityart;
    private EditText amountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        ButterKnife.bind(this);
        GlideCatchUtil.getInstance().clearCacheMemory();
        GlideCatchUtil.getInstance().clearCacheDiskSelf();

        txtTitle.setText("商品详情");
        goodsid = getIntent().getStringExtra("goodid");
        if (goodsid.indexOf("http") == -1) {
            //不包含
            goodsid = Urls.COMMODITY + goodsid;
        }
        setDate();


    }

    /**
     * 网络请求数据
     */
    private void setDate() {
        OkHttpUtils.get().url(goodsid).build().execute(new CommodityCallBack());
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

    @OnClick({R.id.img_back, R.id.commodity_property
            , R.id.commodity_share, R.id.tv_buy, R.id.tv_put_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy:
                optype = "1";
                showPropertyWindow();
                break;
            case R.id.tv_put_cart:
                optype = "2";
                showPropertyWindow();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.commodity_property:
                optype = "1";
                showPropertyWindow();
                break;
            case R.id.commodity_share:
                if (!caches.get("access_token").equals("null")) {
                    umurl = Urls.UMSTR + commodityEntity.goodsBean.goodsId + "&spreader=" + caches.get("spreadCode");
                    UMengShareUtils.uMengShare(context,umurl,commodityEntity.goodsBean.goodsName,commodityEntity.goodsBean.originalImg);
                } else {
                    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                            .setMessage("您还没有登录，请先登录。")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }
                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            dialog.dismiss();
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
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
    private class CommodityCallBack extends Callback<CommodityEntity> {
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
        public CommodityEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            commodityEntity = new Gson().fromJson(s, new TypeToken<CommodityEntity>() {
            }.getType());
            return commodityEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onResponse(final CommodityEntity response, int id) {
            if (response.error_code.equals("000")) {
                imageurlid = response.goodsBean.goodsId;
                commodityGoodsName.setText(response.goodsBean.goodsName);
                commodityDescribe.setText(response.goodsBean.goodsRemark);
                commodityPrice.setText("¥" + response.goodsBean.shopPrice);
                commodityOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                commodityOldPrice.setText("¥" + response.goodsBean.marketPrice);
                commodityArt.setVisibility(View.GONE);
                artLl.setVisibility(View.GONE);
                if (response.goodsBean.giveIntegral.length()!=0&&Math.floor(Double.parseDouble(response.goodsBean.giveIntegral)/3)>0) {

                    commodityArt.setText("赠送" + Math.floor(Double.parseDouble(response.goodsBean.giveIntegral)/3)+"积分");
                } else {
                    artLl.setVisibility(View.GONE);
                }

                if (response.goodsBean.promType.equals("4")) {
                    commoditySold.setVisibility(View.GONE);
                } else {
                    commoditySold.setText("已售:" + response.goodsBean.sales_sum);

                }
                commodityRepertory.setText("库存:" + response.goodsBean.storeCount);


                if (response.goodsBean.rest_time_toEnd != null && !response.goodsBean.rest_time_toEnd.equals("")
                        && response.goodsBean.rest_time_toEnd.length() != 0
                        && Integer.parseInt(response.goodsBean.rest_time_toEnd) > 0) {
                    commodityCountDown.setVisibility(View.VISIBLE);
                    countDownClock.setTimes(Long.parseLong(response.goodsBean.rest_time_toEnd));
                    //已经在倒计时的时候不再开启计时
                    if (!countDownClock.isRun()) {
                        countDownClock.beginRun();

                    }
                } else {
                    commodityCountDown.setVisibility(View.GONE);
                }


                standardlist = response.standardlist;
                normsBeanMap = response.normsBeanMap;
                for (Map.Entry<String, ArrayList<CommodityEntity.StandsrdBean>> entry : standardlist.entrySet()) {
                    commoditylist.add(entry.getKey());
                }
                //添加轮播图
                image = new ArrayList();
                ArrayList<CommodityEntity.GoogsimageBean> adList = response.googsimageBean;
                if (adList != null && adList.size() != 0) {
                    for (int i = 0; i < adList.size(); i++) {
                        image.add(Ips.PHPURL + adList.get(i).imageUrl);
                    }
                } else {
                    return;
                }
                commodityWeb.loadUrl(Urls.GOODSDETA + imageurlid);
                commodityWeb.getSettings().setDomStorageEnabled(true);
                //初始化首页轮播图
                initCB();
            }

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

    private void showPropertyWindow() {
        if (mPropertyWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.good_property_select_layout, null);
            mPropertyWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPropertyWindow.setContentView(contentView);
//在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
            mPropertyWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mPropertyWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//点击空白处时，隐藏掉pop窗口
            mPropertyWindow.setFocusable(true);
            mPropertyWindow.setBackgroundDrawable(new BitmapDrawable());
            backgroundAlpha(1f);

            //添加pop窗口关闭事件
            mPropertyWindow.setOnDismissListener(new poponDismissListener());
            mPropertyWindow.setAnimationStyle(R.style.AnimationSEX);
            nameTv = (TextView) contentView.findViewById(R.id.tv_good_description);
            priceTv = (TextView) contentView.findViewById(R.id.tv_price);
            tv_good_art = (TextView) contentView.findViewById(R.id.tv_good_art);
            propertyTv = (TextView) contentView.findViewById(R.id.tv_select_property);
            ImageView imgClose = (ImageView) contentView.findViewById(R.id.imgView_close);
            ImageView imgIcon = (ImageView) contentView.findViewById(R.id.imgView_good_icon);
            final Button decreaseBtn = (Button) contentView.findViewById(R.id.btn_decrease);
            Button increaseBtn = (Button) contentView.findViewById(R.id.btn_increase);
            amountTv = (EditText) contentView.findViewById(R.id.tv_amount);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(amountTv.getWindowToken(), 0);
            amountTv.setCursorVisible(false);
            amountTv.addTextChangedListener(new TextWatcher() {
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
                        amountTv.setText("1");
                        amountTv.setSelection(amountTv.getText().length());
                    }
                }
            });
            NoScrollListView propertyLv = (NoScrollListView) contentView.findViewById(R.id.lv_commodity_property);
            TextView cartTv = (TextView) contentView.findViewById(R.id.tv_put_true);
            final TextView confirmTv = (TextView) contentView.findViewById(R.id.tv_confirm);
            View placeholderView = contentView.findViewById(R.id.view_placeholder);
            GlideImage.loadImage(context, imgIcon, commodityEntity.goodsBean.originalImg, R.mipmap.img_error);
            priceTv.setText("价格：¥" + commodityEntity.goodsBean.shopPrice + "");
            nameTv.setText("库存：" + commodityEntity.goodsBean.storeCount + "");
            tv_good_art.setVisibility(View.GONE);
            if (commodityEntity.goodsBean.giveIntegral.length()!=0) {
                tv_good_art.setText("赠送" + Math.floor(Double.parseDouble(commodityEntity.goodsBean.giveIntegral)/3)+"积分");
            } else {
                tv_good_art.setVisibility(View.GONE);
            }
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.imgView_close:
                            allitemid = "";
                            mPropertyWindow.dismiss();
                            mPropertyWindow.setOnDismissListener(new poponDismissListener());
                            break;
                        case R.id.btn_decrease:
                            if (mCount == 1) {
                                decreaseBtn.setEnabled(false);
                            } else {
                                --mCount;
                                amountTv.setText(mCount + "");
                            }
                            break;
                        case R.id.btn_increase:
                            decreaseBtn.setEnabled(true);
                            ++mCount;
                            amountTv.setText(mCount + "");
                            break;
                        case R.id.tv_put_true:
                            if (caches.get("access_token").equals("null")) {
                                HintLoginDialog dialog = new HintLoginDialog(context);
                                dialog.show();
                            } else {
                                userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                                }.getType());
                                id = userInfoEntity.data.userVo.id;
                                if (commoditylist == null || commoditylist.size() == 0) {
                                    //过境商品为true
                                    if (isTransit(commodityEntity.goodsBean.goodsTag)) {
                                        UserInfoEntity.DataBean.RealnameVoBean realname = userInfoEntity.data.realnameVo;
                                        if (realname == null || realname.name == null) {
                                            ApproveDialog dialog = new ApproveDialog(context, "进口物品，保税清关，需要进行实名认证信息！请实名认证后再购买！", "3");
                                            dialog.show();
                                        } else {
                                            addToShoppingCart(optype);
                                        }
                                    } else {
                                        addToShoppingCart(optype);
                                    }
                                    return;
                                }
                                if (allitemid != null) {
                                    CommodityEntity.NormsBean normsBean = normsBeanMap.get(allitemid.substring(0, allitemid.length() - 1));
                                    if (normsBean != null) {
                                        //过境商品为true
                                        if (isTransit(commodityEntity.goodsBean.goodsTag)) {
                                            UserInfoEntity.DataBean.RealnameVoBean realname = userInfoEntity.data.realnameVo;
                                            if (realname == null || realname.name == null) {
                                                ApproveDialog dialog = new ApproveDialog(context, "进口物品，保税清关，需要进行实名认证信息！请实名认证后再购买！", "3");
                                                dialog.show();
                                            } else {
                                                addToShoppingCart(optype);
                                            }
                                        } else {
                                            addToShoppingCart(optype);
                                        }
                                    } else {
                                        showToast("请选择商品属性");
                                    }
                                } else {
                                    showToast("请选择商品属性");
                                }
                            }


                            break;
                    }
                }
            };
            imgClose.setOnClickListener(clickListener);
            placeholderView.setOnClickListener(clickListener);
            decreaseBtn.setOnClickListener(clickListener);
            increaseBtn.setOnClickListener(clickListener);
            cartTv.setOnClickListener(clickListener);
            confirmTv.setOnClickListener(clickListener);
            propertyLv.setAdapter(new PropertyAdapter(context, standardlist));
        }
        if (mOperate == OPERATE_USER) {
            mPropertyWindow.getContentView().findViewById(R.id.layout_bottom).setVisibility(View.VISIBLE);
            mPropertyWindow.getContentView().findViewById(R.id.tv_confirm).setVisibility(View.GONE);
        } else {
            mPropertyWindow.getContentView().findViewById(R.id.layout_bottom).setVisibility(View.GONE);
            mPropertyWindow.getContentView().findViewById(R.id.tv_confirm).setVisibility(View.VISIBLE);
        }
        if (!mPropertyWindow.isShowing()) {
            mPropertyWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    //添加到购物车
    private void addToShoppingCart(final String optype) {
        String[] paramnameList;
        String[] paramList;
        StringBuilder stringBuilder = null;
        if (allitemid != null) {
            if (allitemid.contains("_")) {
                paramnameList = allitemid.split("_");
                paramList = allitemid.split("_");
            } else {
                paramnameList = allitemid.split(" ");
                paramList = allitemid.split(" ");
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("{");
            for (int i = 0; i < paramnameList.length; i++) {
                if (i != paramList.length - 1) {
                    stringBuilder.append("\"" + paramnameList[i] + "\":\"" + paramList[i] + "\",");
                } else {
                    stringBuilder.append("\"" + paramnameList[i] + "\":\"" + paramList[i] + "\"");
                }
            }
            stringBuilder.append("}");
        }


        StringBuilder strJson = new StringBuilder();
        if (allitemid == null || allitemid.equals("")) {
            strJson.append("{\"goods_id\":\"" + commodityEntity.goodsBean.goodsId + "\"," + "\"goods_num\":\"" + amountTv.getText().toString() + "\"," + "\"uid\":" + id + "}");
        } else {
            strJson.append("{\"goods_id\":\"" + commodityEntity.goodsBean.goodsId + "\"," + "\"goods_num\":\"" + amountTv.getText().toString() + "\"," + "\"goods_spec\":" + stringBuilder + "," + "\"uid\":" + id + "}");
        }
        //加入删除购物车   1加入购物车 2 删除
        OkHttpUtils.postString().url(Urls.ADDTOSHOPPINGCART + 1).content(strJson.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                String code = map.get("error_code").toString();
                String error_msg = map.get("error_msg").toString();
                if (code.equals("000")) {
                    switch (optype) {
                        case "1":
                            Intent intent = new Intent(context, ShoppingCartActivity.class);
                            intent.putExtra("type", "1");
                            startActivity(intent);
                            break;
                        case "2":
                            showToast("添加成功");
                            break;
                    }
                } else {
                    showToast(error_msg);
                }
            }
        });
    }

    //判断是否是过境商品
    private boolean isTransit(String gtag) {
        if (gtag == null || gtag.equals("")) {
            return false;
        } else {
            if (gtag.equals("1")) {
                return true;
            }
            return false;
        }
    }

    public class PropertyAdapter extends BaseAdapter {
        private Map<String, ArrayList<CommodityEntity.StandsrdBean>> standardlist;
        private Context context;
        private ArrayList<CommodityEntity.StandsrdBean> standsrdBeen;

        public PropertyAdapter(Context context, Map<String, ArrayList<CommodityEntity.StandsrdBean>> gridviewlist) {
            this.context = context;
            this.standardlist = gridviewlist;
            for (int i = 0; i < commoditylist.size(); i++) {
                tagList.put(i, "");
            }
        }

        private int setposition(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return commoditylist.size();
        }

        @Override
        public Object getItem(int position) {
            return commoditylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHodler holder = null;
            if (convertView == null) {
                holder = new ViewHodler();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_property, null);
                holder.item_property_tv = (TextView) convertView.findViewById(R.id.item_property_tv);
                holder.item_property_gv = (NoScrollGridView) convertView.findViewById(R.id.item_property_gv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHodler) convertView.getTag();
            }
            holder.item_property_tv.setText(commoditylist.get(position));
            standsrdBeen = standardlist.get(commoditylist.get(position));
            final ProperGridViewAdapter adapter = new ProperGridViewAdapter(context, standsrdBeen);
            holder.item_property_gv.setAdapter(adapter);
            holder.item_property_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    adapter.setSeclection(pos);
                    adapter.notifyDataSetChanged();
                    String data = standardlist.get(commoditylist.get(position)).get(pos).itemId + "";
                    tagList.put(position, data + "_");
                    allitemid = "";
                    for (int i = 0; i < tagList.size(); i++) {
                        allitemid += tagList.get(i);
                    }

                    CommodityEntity.NormsBean normsBean = normsBeanMap.get(allitemid.substring(0, allitemid.length() - 1));
                    if (normsBean != null) {
                        nameTv.setText("库存：" + normsBean.storeCount + "");
                        if (commodityEntity.flash_sale_price != null && !commodityEntity.flash_sale_price.equals("") && commodityEntity.flash_sale_price.length() != 0) {
                            priceTv.setText("价格：¥" + commodityEntity.flash_sale_price + "");
                        } else {
                            priceTv.setText("价格：¥" + normsBean.price + "");
                        }
                        tv_good_art.setVisibility(View.GONE);
                        if (normsBean.giveIntegral.length()!=0&&Math.floor(Double.parseDouble(normsBean.giveIntegral)/3)>0) {
                            tv_good_art.setText("赠送" + Math.floor(Double.parseDouble(normsBean.giveIntegral)/3)+"积分");
                        } else {
                            tv_good_art.setVisibility(View.GONE);
                        }

                        propertyTv.setText("规格：" + normsBean.keyName);
                    }
                }
            });
            return convertView;
        }

        public class ViewHodler {
            TextView item_property_tv;
            NoScrollGridView item_property_gv;
        }
    }

    protected void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }
}
