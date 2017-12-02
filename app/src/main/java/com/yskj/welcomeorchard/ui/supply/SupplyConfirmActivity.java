package com.yskj.welcomeorchard.ui.supply;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SupplyConfirmAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AddressEntity;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.PostTageEntity;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.http.AddressCallBack;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.address.AddressActivity;
import com.yskj.welcomeorchard.ui.address.PerfectActivity;
import com.yskj.welcomeorchard.ui.order.OrderPayActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 作者： chen
 * 时间： 2017/8/29
 * 描述：微商货源确认订单
 */

public class SupplyConfirmActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.confirm_order)
    LinearLayout confirmOrder;
    @Bind(R.id.address_ll)
    LinearLayout address_ll;
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.tv_all_price)
    TextView tvAllPrice;
    @Bind(R.id.tv_pay_order)
    TextView tvPayOrder;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.tv_all_freight)
    TextView tv_all_freight;
    private List<LocalServerEntity> list;
    private List<LocalServerNumEntity> localServerNumEntityList;
    private SupplyConfirmAdapter supplyConfirmAdapter;
    private double allPrice;
    private int allNum;
    private int id;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private AddressEntity  addressEntity;
    private String addressid;
    private String listgoods = "null";
    private String postage = "null";
    private double totalPostage;
    private List<LocalServerEntity> mListGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplyconfirm);
        ButterKnife.bind(this);
        txtTitle.setText("确认订单");
        iniview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getaddresslist();
    }

    /**
     * 商品数据
     */
    private void iniview() {
        listgoods = "null";

        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        if(userInfoEntity!=null){
            id = userInfoEntity.data.userVo.id;
        }

        localServerNumEntityList = (List<LocalServerNumEntity>) getIntent().getSerializableExtra("numEntityList");
        list = (List<LocalServerEntity>) getIntent().getSerializableExtra("list");
        supplyConfirmAdapter = new SupplyConfirmAdapter(context,localServerNumEntityList,list);
        listView.setAdapter(supplyConfirmAdapter);
        for (int i = 0; i < localServerNumEntityList.size(); i++) {
            allPrice += localServerNumEntityList.get(i).getPrice()*localServerNumEntityList.get(i).getCount();
            allNum += localServerNumEntityList.get(i).getCount();
            listgoods = listgoods +"&goods=" +localServerNumEntityList.get(i).getGoodsid()+":"+localServerNumEntityList.get(i).getParameterid()+":"+localServerNumEntityList.get(i).getCount();
            postage = postage+"&goods="+localServerNumEntityList.get(i).getGoodsid()+":"+localServerNumEntityList.get(i).getCount();
        }
        tvCount.setText(Html.fromHtml("订单金额：<font <color=\"#fd5072\">¥"
                + StringUtils.getStringtodouble(allPrice+totalPostage)+"</font>"));
        tvAllPrice.setText(Html.fromHtml("共"+allNum+"件商品  合计：¥"
                + StringUtils.getStringtodouble(allPrice+totalPostage)+"(运费："+StringUtils.getStringtodouble(totalPostage)+")"));
        tv_all_freight.setText("¥"+StringUtils.getStringtodouble(totalPostage));
    }
    /**
     * 获取邮费
     */
    private void getpostage(){
        OkHttpUtils.post().url(Urls.POSTAGE+ "?" + postage.replace("null&", ""))
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("addressId",addressid+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                PostTageEntity postTageEntity = new Gson().fromJson(response,PostTageEntity.class);
                if(postTageEntity.code == 0){
                    totalPostage = postTageEntity.data.totalPostage;
                }
            }
        });
    }

    /**
     * 收货地址
     */
    private void getaddresslist() {
        OkHttpUtils.get().url(Urls.ADDRESS + id).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
            isLogin(e);
            }

            @Override
            public void onResponse(String s, int i) {
                addressEntity = new Gson().fromJson(s,AddressEntity.class);
                if(addressEntity.error_code.equals("000")){
                    if(addressEntity!=null&&addressEntity.addressList.size()>0){
                        for (int j = 0; j < addressEntity.addressList.size(); j++) {
                            if(addressEntity.addressList.get(j).isDefault.equals("1")){
                                tvName.setText(addressEntity.addressList.get(j).consignee);
                                tvNumber.setText(addressEntity.addressList.get(j).mobile);
                                tvAddress.setText(addressEntity.addressList.get(j).fullAddress+addressEntity.addressList.get(j).address);
                                addressid = addressEntity.addressList.get(j).addressId;
                            }
                        }
                        getpostage();
                    }
                }else {
                    if(caches.get("access_token").equals("null")){
                        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                .setMessage("您还没有登录是否去登录？")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        startActivity(new Intent(context, LoginActivity.class));
                                    }
                                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                finish();
                            }
                        }).show();//在按键响应事件中显示此对话框
                        return;
                    }else {
                        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                .setMessage("您还没有设置收货地址，是否现在设置？")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        startActivity(new Intent(context,PerfectActivity.class)
                                                .putExtra("addresstype","3"));
                                    }
                                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                    }

                }


            }
        });
    }

    @OnClick({R.id.img_back, R.id.tv_pay_order,R.id.address_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_pay_order:
                establishOrder();
                break;
            case R.id.address_ll:
                caches.put("addresscode","9");
                startActivity(new Intent(context, AddressActivity.class));
                break;
        }
    }
    /**
     * 创建临时订单
     */
    private void  establishOrder(){
        OkHttpUtils.post().url(Urls.MGORDER+ "?" + listgoods.replace("null&", ""))
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("addressId",addressid+"")
                .build().execute(new StringCallback() {
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
            public void onError(Call call, Exception e, int i) {
                isLogin(e);
            }

            @Override
            public void onResponse(String s, int postion) {
                Map<String, Object> map = JSONFormat.jsonToMap(s);
                if(map!=null){
                    int code = (int) map.get("code");
                    if(code == 0){
                        for (int i = 0; i < localServerNumEntityList.size(); i++) {
                            DataSupport.deleteAll(LocalServerNumEntity.class,"parameterid=?",localServerNumEntityList.get(i).getParameterid()+"");
                        }
                        mListGoods = DataSupport.findAll(LocalServerEntity.class);
                        for (LocalServerEntity item : mListGoods) {
                            List<LocalServerNumEntity> list = DataSupport.where("shopid=?",item.getShopid()+"").find(LocalServerNumEntity.class);
                            if(list.size()<=0){
                                DataSupport.deleteAll(LocalServerEntity.class,"shopid=?",item.getShopid()+"");
                            }
                        }
                        ScanCodeTmpEntity setUpEntity = new Gson().fromJson(s, ScanCodeTmpEntity.class);
                        startActivity(new Intent(context, OrderPayActivity.class).putExtra("type","3")
                                .putExtra("allAmount", StringUtils.getStringtodouble(allPrice+totalPostage)+"")
                                .putExtra("tid",setUpEntity.data.orderId+""));
                    }else {
                        showToast(MessgeUtil.geterr_code(code));
                    }
                }

            }
        });
    }
}
