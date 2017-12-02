package com.yskj.welcomeorchard.ui.address;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AddressEntity;
import com.yskj.welcomeorchard.entity.ShoppingCartEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.http.AddressCallBack;
import com.yskj.welcomeorchard.ui.order.ConfirmOrderActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YSKJ-02 on 2017/1/14.
 * 收货地址列表
 */

public class AddressActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.address_lv)
    ListView addressLv;
    @Bind(R.id.add_dizhi)
    RadioButton addDizhi;
    private List<AddressEntity> addressentitylist;
    public static final int SHUAXIN = 1;
    public static final int CONFIRM = 2;
    public static final int CONFSUPP = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHUAXIN:
                    getaddresslist();
                    break;
                case CONFIRM:
                    startActivity(new Intent(context, ConfirmOrderActivity.class).putExtra("addresstype", "1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case CONFSUPP:
                    AppManager.getInstance().killActivity(AddressActivity.class);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int id;
    private UserInfoEntity userInfoEntity;
    private ArrayList<ShoppingCartEntity.CartListBean> cartListBeenIsChecked = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        txtTitle.setText("收货地址");
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        id = userInfoEntity.data.userVo.id;
        cartListBeenIsChecked = (ArrayList<ShoppingCartEntity.CartListBean>) getIntent().getSerializableExtra("cartlist");
        getaddresslist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getaddresslist();
    }

    private void getaddresslist() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.ADDRESS + id).build().execute(new AddressCallBack(context, addressLv, handler));
        stopMyDialog();
    }

    @OnClick({R.id.img_back, R.id.add_dizhi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.add_dizhi:
                if (caches.get("addresscode").equals("0")) {
                    startActivity(new Intent(context, PerfectActivity.class).putExtra("cartlist", cartListBeenIsChecked).putExtra("addresstype", "1").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    startActivity(new Intent(context, PerfectActivity.class).putExtra("addresstype", "1"));
                }

                break;
        }
    }



}
