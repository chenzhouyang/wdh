package com.yskj.welcomeorchard.ui.localserver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerOrderRefundBean;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.EncodingHandlers;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by jianghe on 2016/11/17 0017.
 */
public class LocalServerOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.btn_refund)
    Button btn_refund;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_qrcode)
    ImageView img_qrcode;
    @Bind(R.id.tv_payment_code)
    TextView tv_payment_code;
    private String status, payCode, orderId;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private LocalServerOrderRefundBean bean;
    private GiveUpDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_server_order_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        status = intent.getStringExtra("status");
        payCode = intent.getStringExtra("payCode") == null ? "" : intent.getStringExtra("payCode");
        orderId = intent.getStringExtra("orderId");
        tv_payment_code.setText("付款码：" + payCode);
        btn_refund.setOnClickListener(this);
        initBtn();
        initQrCode();
    }

    private void initQrCode() {
        String url = payCode;
        if (url == null || url.equals("")) {
            return;
        }
        Bitmap qrCodeBitmap;
        try {
            qrCodeBitmap = EncodingHandlers.createQRCode(url, 500);
            img_qrcode.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void initBtn() {
        if (status.equals("0")) {
            btn_refund.setVisibility(View.VISIBLE);
        } else {
            btn_refund.setVisibility(View.GONE);
        }
    }


    private void refund() {
        dialog = new GiveUpDialog(context);
        dialog.show();
    }

    @OnClick({R.id.btn_refund, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refund:
                refund();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    public class GiveUpDialog extends Dialog implements View.OnClickListener {
        private Button btnCancle;
        private Button btnSure;

        public GiveUpDialog(Context context) {
            super(context, R.style.ShareDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_give_up);

            initView();
        }

        private void initView() {
            btnCancle = (Button) findViewById(R.id.btn_cancle);
            btnSure = (Button) findViewById(R.id.btn_sure);
            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    sureRefund();
                    dismiss();
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    showToast("退款已取消");
                    break;
            }
        }
    }

    private void sureRefund() {
        OkHttpUtils.post().url(Urls.LOCALSERVERREFUND)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("couponId",orderId)
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
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    getuserinfo();
                } else if(code == 739){
                    showToast("您已经使用过该消费券了");
                    finish();
                }else if(code == 738){
                    showToast("该消费券不存在");
                    finish();
                }else{
                    showToast("退款失败");
                    finish();
                }

            }
        });
    }
    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.MONEYINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int ids) {
                showToast("退款成功");
                finish();
            }
        });
    }
}
