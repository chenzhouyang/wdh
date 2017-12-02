package com.yskj.welcomeorchard.ui.verify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.ui.setting.OverPaymentActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.ShowErrorCode;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 作者： chen
 * 时间： 2017/9/19.
 * 描述：果果乐支付密码
 */

public class OrchardPasswordActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.et_new_pwd)
    EditText etNewPwd;
    @Bind(R.id.et_new_pwd_sure)
    EditText etNewPwdSure;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_get_code)
    Button btnGetCode;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.btn_sure)
    Button btnSure;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orchard_password);
        ButterKnife.bind(this);
        txtTitle.setText("修改支付密码");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
    }

    @OnClick({R.id.img_back, R.id.btn_sure,R.id.btn_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //返回
                finish();
                break;
            case R.id.btn_sure:
//                确定
                if(etNewPwd.getText().length() == 0){
                    showToast("请输入新密码");
                }else if(etNewPwdSure.getText().length() == 0){
                    showToast("请确认密码");
                }else if (etNewPwd.getText().length()!= 6) {
                    showToast("输入新密码不合法");
                }else if(!etNewPwd.getText().toString().equals(etNewPwdSure.getText().toString())){
                    showToast("两次输入密码不同");
                }else if(etCode.getText().length()==0){
                    showToast("请输入验证码");
                }else {
                    modifyPayPassWord();
                }

                break;
            case R.id.btn_get_code:
                gotCode();
                break;
        }
    }
    //提交数据
    private void modifyPayPassWord() {
        OkHttpUtils.post().url(Urls.NEWPAYPASSWORD)
                .addHeader(Config.HEADER, caches.get("access_token"))
                .addParams("sms",etCode.getText().toString())
                .addParams("newPass",  etNewPwd.getText().toString())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                      isLogin(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Map<String, Object> map = JSONFormat.jsonToMap(response);
                        int code = (int) map.get("code");
                        showToast(ShowErrorCode.getErrorMessage(code));
                        if (code==0){
                           //修改成功
                            showToast("密码修改成功");
                            AppManager.getInstance().killActivity(OverPaymentActivity.class);
                            AppManager.getInstance().killActivity(OrchardPasswordActivity.class);
                        }
                    }
                });
    }
    //获取验证码
    private void gotCode() {
        SharedPreferences share2    = context.getSharedPreferences("mobile", 0);
        String mobile  = share2.getString("mobile", "null");
        if(mobile.equals("null")){
            return;
        }
        OkHttpUtils.post().url(Urls.GOTCODE).addParams("mobile", mobile)
                .addParams("bizType","3")
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
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");

                if (code == 0) {
                    time.start();
                    showToast("验证码发送成功");
                }else {
                    showToast(ShowErrorCode.getErrorMessage(code));
                }
            }
        });
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btnGetCode.setText("重发");
            btnGetCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            if(btnGetCode!=null){
                btnGetCode.setClickable(false);
                btnGetCode.setText("(" + millisUntilFinished / 1000 + "秒)");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }
}
