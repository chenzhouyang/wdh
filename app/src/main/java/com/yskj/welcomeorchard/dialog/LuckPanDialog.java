package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AddressEntity;
import com.yskj.welcomeorchard.entity.PrizeResultEntity;
import com.yskj.welcomeorchard.entity.RegisterEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.address.AddressActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/5/17on 17:53.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class LuckPanDialog extends Dialog {

    private TextView tvDes,tvName;
    private ImageView imgSure,imgGoods,imgEditAddress,imgGoodsSure;
    private LinearLayout llBg,llBgGoods;

    private TextView etNme,etPhone,etAddress;

    private PrizeResultEntity.ListBean prizeResult;
    private String money;

    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;

    private Context context;
    private int uid;
    private ArrayList<AddressEntity.AddressListBean> addressEntityList;
    private String addressId;

    public LuckPanDialog(Context context, PrizeResultEntity.ListBean prizeResult, String money) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.prizeResult = prizeResult;
        this.money = money;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_luck_pan);
        initView();
    }


    private void initView() {
        tvDes = (TextView) findViewById(R.id.tv_des);
        imgSure = (ImageView) findViewById(R.id.img_sure);
        llBg = (LinearLayout) findViewById(R.id.ll_bg);

        llBgGoods = (LinearLayout) findViewById(R.id.ll_bg_goods);
        imgGoods = (ImageView) findViewById(R.id.img_goods);
        tvName = (TextView) findViewById(R.id.tv_name);
        etNme = (TextView) findViewById(R.id.et_name);
        etPhone = (TextView) findViewById(R.id.et_phonenum);
        etAddress = (TextView) findViewById(R.id.et_address);
        imgGoodsSure = (ImageView) findViewById(R.id.img_goods_sure);
        imgEditAddress = (ImageView) findViewById(R.id.img_edit_address);
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {}.getType());
        switch (prizeResult.type) {
            //0  未中奖  1 获得积分  2 获得商品
            case "0":
                llBgGoods.setVisibility(View.GONE);
                llBg.setVisibility(View.VISIBLE);
                llBg.setBackgroundResource(R.mipmap.prize_failed);
                tvDes.setText("您与大奖擦身而过啦\n别气馁再接再厉哟");
                imgSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;
            case "1":
                llBgGoods.setVisibility(View.GONE);
                llBg.setVisibility(View.VISIBLE);
                if (!caches.get("userinfo").equals("null")) {
                    String name = userInfoEntity.data.userVo.nickName;
                    llBg.setBackgroundResource(R.mipmap.prize_jifen);
                    tvDes.setText(name + money);
                    imgSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                }
                break;
            case "2":
                llBgGoods.setVisibility(View.VISIBLE);
                llBg.setVisibility(View.GONE);
                llBgGoods.setBackgroundResource(R.mipmap.prize_jifen);
                GlideImage.loadImage(context,imgGoods,prizeResult.code,R.mipmap.img_error);
                tvName.setText(prizeResult.name);
                getCommitAddressData();
                imgGoodsSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commitAddress();
                    }
                });

                imgEditAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        context.startActivity(new Intent(context, AddressActivity.class));
                    }
                });
                break;
        }
    }

    private void getCommitAddressData() {
        if (!caches.get("userinfo").equals("null")) {
            uid = userInfoEntity.data.userVo.id;
            OkHttpUtils.get().url(Urls.ADDRESS + uid).build().execute(new AddressCallBack());
        }
    }

    private class AddressCallBack extends Callback<AddressEntity>{
        @Override
        public AddressEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            AddressEntity addressentitylist = new Gson().fromJson(s,new TypeToken<AddressEntity>(){}.getType());
            return addressentitylist;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(AddressEntity response, int id) {
            addressEntityList = response.addressList;
            if(addressEntityList!=null&&addressEntityList.size()!=0){
                for (AddressEntity.AddressListBean address:addressEntityList) {
                    if (address.isDefault.equals("1")){
                        addressId = address.addressId;
                        etNme.setText(address.consignee);
                        etPhone.setText(address.mobile);
                        etAddress.setText(address.fullAddress+address.address);
                    }
                }
            }else {
                Toast.makeText(context,"您还未添加收货地址",Toast.LENGTH_SHORT).show();
                dismiss();
                context.startActivity(new Intent(context, AddressActivity.class));
            }
        }
    }

    private void commitAddress() {
        OkHttpUtils.get().url(Urls.PRIZECOMMITADDRESS+"/uid/"+uid+"/id/"+prizeResult.id+"/rec_id/"+prizeResult.recId+"/address_id/"+addressId).build().execute(new commitAddressCallBack());
    }

    private class commitAddressCallBack extends Callback<RegisterEntity>{

        @Override
        public RegisterEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            RegisterEntity addressentitylist = new Gson().fromJson(s,new TypeToken<RegisterEntity>(){}.getType());
            return addressentitylist;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            Toast.makeText(context,"网络请求错误",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(RegisterEntity response, int id) {
            if (response.error_code.equals("000")){
                Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }
}
