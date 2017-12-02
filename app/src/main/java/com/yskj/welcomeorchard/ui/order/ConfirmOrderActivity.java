package com.yskj.welcomeorchard.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.ConfirmOrderAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AddressEntity;
import com.yskj.welcomeorchard.entity.ConfirmOrderEntity;
import com.yskj.welcomeorchard.entity.SubmitOrderEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.address.AddressActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.NumberFormat;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-JH on 2017/1/15.
 */

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.tv_note)
    TextView tvNote;
    @Bind(R.id.tv_expense)
    TextView tvExpense;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_all_price)
    TextView tvAllPrice;
    @Bind(R.id.tv_pay_order)
    TextView tvPayOrder;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.confirm_order)
    LinearLayout confirmOrder;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_count)
    TextView tvCount;
    private ArrayList<AddressEntity> addressEntityList;
    //默认地址默认为没有
    private boolean hasDefaultAddress = false;
    private ConfirmOrderEntity confirmOrderEntity;

    private String addressId;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private UserInfoEntity userInfoEntity;
    private String token;
    private int is_delivery = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.ADDRESS + id).build().execute(new AddressCallBack());


    }

    //获取默认地址
    public class AddressCallBack extends Callback<AddressEntity> {
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
        public AddressEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            AddressEntity addressentitylist = new Gson().fromJson(s, new TypeToken<AddressEntity>() {
            }.getType());
            return addressentitylist;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(AddressEntity response, int id) {
            if (response.addressList == null || response.addressList.size() == 0) {
                tvName.setText("姓名：");
                tvAddress.setText("你还没有收货地址，请添加！");
            }else {
                for (AddressEntity.AddressListBean addressEntity : response.addressList) {
                    if (addressEntity.isDefault.equals("1")) {
                        //有默认地址
                        hasDefaultAddress = true;
                        tvName.setText("姓名：" + addressEntity.consignee);
                        tvNumber.setText(addressEntity.mobile);
                        tvAddress.setText("收货地址：" + addressEntity.fullAddress);
                        addressId = addressEntity.addressId;
                    }
                }
                OkHttpUtils.get().url(Urls.CONFIRMORDER + token+"/address_id/"+addressId).build().execute(new confirmOrderCallBack());
            }

        }
    }

    //确认订单
    private class confirmOrderCallBack extends Callback<ConfirmOrderEntity> {
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
        public ConfirmOrderEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ConfirmOrderEntity confirmOrderEntity = new Gson().fromJson(s, new TypeToken<ConfirmOrderEntity>() {
            }.getType());
            return confirmOrderEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(ConfirmOrderEntity response, int id) {
            if (response.errorCode.equals("000")) {
                ConfirmOrderEntity.CarPriceBean carPrice = response.carPrice;
                tvPrice.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(carPrice.goodsFee, 0.00)));
                tvExpense.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(carPrice.postFee, 0.00)));
                tvBalance.setText("-￥" + String.format("%.2f", NumberFormat.convertToDouble(carPrice.balance, 0.00)));
                tvTotalPrice.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(carPrice.totalFee, 0.00)));
                tvCount.setText("共" + carPrice.anum + "件");
                tvAllPrice.setText("实付金额：￥" + String.format("%.2f", NumberFormat.convertToDouble(carPrice.payables, 0.00))
                        + "\n(含运费￥" + String.format("%.2f", NumberFormat.convertToDouble(carPrice.postFee, 0.00)) + ")");
                is_delivery = carPrice.isDelivery;
                if(is_delivery == 0){
                    tvPayOrder.setBackgroundResource(R.color.grey);
                }else {
                    tvPayOrder.setBackgroundResource(R.color.activity_red);
                }
                ConfirmOrderAdapter adapter = new ConfirmOrderAdapter(context,response.cartList);
                listView.setAdapter(adapter);
            } else {
                showToast(response.errorMsg);
            }
        }
    }

    private void initView() {
        EditText tv_confirm_note = (EditText) findViewById(R.id.tv_confirm_note);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_confirm_note.getWindowToken(),0);
        token = caches.get("php_token");
        id = userInfoEntity.data.userVo.id;
        txtTitle.setText("确认订单");
        imgBack.setOnClickListener(this);
        tvPayOrder.setOnClickListener(this);
        confirmOrder.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_pay_order:

                if (hasDefaultAddress) {
                    if(is_delivery == 1){
                        submitOrder();
                    }else {
                        showToast("该订单中包含不能发货的商品,请确认后操作");
                    }

                } else {
                    showToast("你还没有默认地址，请先设置默认地址");
                }
                break;
            case R.id.confirm_order:
                caches.put("addresscode","0");
                startActivity(new Intent(context, AddressActivity.class));
                break;
        }
    }
    //提交订单接口
    private void submitOrder() {
        OkHttpUtils.get().url(Urls.SUBMITORDER + token+"/address_id/" + addressId).build().execute(new submitOrderCallBack());
    }

    private class submitOrderCallBack extends Callback<SubmitOrderEntity> {
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
        public SubmitOrderEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            SubmitOrderEntity submitOrderEntity = new Gson().fromJson(s, new TypeToken<SubmitOrderEntity>() {
            }.getType());
            return submitOrderEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(SubmitOrderEntity response, int id) {
            if (response.errorCode.equals("000")) {
                Intent intent = new Intent(context, OrderPayActivity.class);
                intent.putExtra("type","0");
                intent.putExtra("ordertype","0");
                intent.putExtra("orderSn", response.orderSn);
                intent.putExtra("total_amount", response.totalAmount);
                intent.putExtra("user_money", response.userMoney);
                intent.putExtra("order_amount", response.orderAmount);
                intent.putExtra("order_id",response.orderid);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                showToast(response.errorMsg);
            }
        }
    }
}
