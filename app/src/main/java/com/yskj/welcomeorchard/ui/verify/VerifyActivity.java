package com.yskj.welcomeorchard.ui.verify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MobileEncryption;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;



/**
 * Created by YSKJ-02 on 2017/1/13.
 * 验证验证码
 */

public class VerifyActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.verify_gain)
    TextView verifyGain;
    @Bind(R.id.verify_number)
    EditText verifyNumber;
    @Bind(R.id.verify_tv)
    TextView verifyTv;
    @Bind(R.id.verify_phone)
    EditText verifyPhone;
    private TimeCount time;
    private String type;
    private boolean ismobile = false;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private boolean verfy = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);
        txtTitle.setText("验证验证码");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        type = getIntent().getStringExtra("type");
            SharedPreferences share2    = getSharedPreferences("mobile", 0);
            String mobile  = share2.getString("mobile", "null");
            if(mobile!=null&&!mobile.equals("null")){
                verifyPhone.setText(mobile);
                verifyPhone.setFocusable(false);
            }

        verifyPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MobileEncryption.isMobileNO(verifyPhone.getText().toString())) {
                   getname(verifyPhone.getText().toString());
                }
            }
        });
    }
//获取验证码
    private void getverify() {
            startMyDialog();
            OkHttpUtils.post().url(Urls.VIERFI)
                    .addParams("mobile", verifyPhone.getText().toString())
                    .addParams("type", type).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    stopMyDialog();
                }

                @Override
                public void onResponse(String response, int id) {
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    stopMyDialog();
                    if (code == 0) {
                        time.start();
                        showToast("验证码发送成功");
                    } else {
                        String message = MessgeUtil.geterr_code(code);
                        showToast(message);
                    }
                }
            });

    }

    //查询是否实名认证
    private void getname(String mobile) {
        if(type.equals("2")){
            OkHttpUtils.get().url(Urls.FINDBYMOBILE)
                    .addParams("mobile",mobile)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {
                        verfy = true;
                    }else {
                        verfy = false;
                    }
                }
            });
        }else {
            OkHttpUtils.get().url(Urls.GETREALNAME)
                    .addHeader("Authorization", caches.get("access_token"))
                    .addParams("mobile", mobile)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 725) {
                        verfy = false;
                    } else {
                        verfy = true;
                    }
                }
            });
        }
    }
    //验证验证码
    private void verify(){
        startMyDialog();
        OkHttpUtils.post().url(Urls.VIERFICODE).addParams("mobile",verifyPhone.getText().toString()).addParams("code",verifyNumber.getText().toString())
                .addParams("type",type).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    if(type.equals("2")){
                        startActivity(new Intent(context, SettingPasswordActivity.class).putExtra("type",type)
                                .putExtra("code","1").putExtra("mobile",verifyPhone.getText().toString()));
                    }else {
                        startActivity(new Intent(context, SettingPasswordActivity.class).putExtra("type",type).putExtra("code","1"));
                    }

                }else {
                    String message = MessgeUtil.geterr_code(code);
                    showToast(message);
                }
            }
        });

    }

    @OnClick({R.id.img_back, R.id.verify_tv,
            R.id.verify_gain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.verify_tv:
                if(verifyPhone.length()<=0){
                    showToast("请输入手机号");
                }else if(verifyNumber.length()!=0){
                    verify();
                }else {
                    showToast("请输入收到的验证码");
                }

                break;
            case R.id.verify_gain:
                if(verifyPhone.length()<=0){
                    showToast("请输入手机号");
                }else if( !MobileEncryption.isMobileNO(verifyPhone.getText().toString())){
                    showToast("请输入正确的手机号");
                    verifyPhone.setText("");
                }else if(!verfy){
                    showToast("该号码未注册过，请确认后再修改");
                }else {
                    getverify();
                }

                break;
        }
    }


    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            verifyGain.setText("重新发送验证码");
            verifyGain.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            verifyGain.setClickable(false);
            verifyGain.setText("重新发送(" + millisUntilFinished / 1000 + "秒)");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }
}
