package com.yskj.welcomeorchard.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LoginEntity;
import com.yskj.welcomeorchard.home.HomeActivity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MobileEncryption;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/13.
 */

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.register_phone)
    EditText registerPhone;
    @Bind(R.id.register_gain)
    TextView registerGain;
    @Bind(R.id.login_forget_password)
    TextView loginForgetPassword;
    @Bind(R.id.login_register)
    TextView loginRegister;
    @Bind(R.id.register_btn)
    Button registerBtn;
    @Bind(R.id.register_code)
    EditText registerCode;
    @Bind(R.id.register_spreadMobile)
    EditText registerSpreadMobile;
    private TimeCount time;
    private boolean ismobile;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private boolean register = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        txtTitle.setText("注册");
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
        registerSpreadMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MobileEncryption.isMobileNO(registerSpreadMobile.getText().toString())) {
                    registerBtn.setBackgroundResource(R.drawable.login_btn_true);
                }
            }
        });

        registerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MobileEncryption.isMobileNO(registerPhone.getText().toString())) {
                    getname(registerPhone.getText().toString());
                }
            }
        });
            String type = getIntent().getStringExtra("type");
            if(type.equals("1")){
                registerSpreadMobile.setText(getIntent().getStringExtra("mobile"));
            }
    }

    //查询是否实名认证
    private void getname(String mobile) {
        OkHttpUtils.get().url(Urls.FINDBYMOBILE)
                .addParams("mobile", mobile)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (e.getMessage().equals("502")) {
                    register = false;
                }
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    register = true;
                } else {
                    register = false;
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.login_register,
            R.id.register_btn, R.id.register_gain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.login_register:
                startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.register_btn:
                if (registerPhone.length() == 0) {
                    showToast("请输入您的手机号");
                } else if (!MobileEncryption.isMobileNO(registerPhone.getText().toString())) {
                    showToast("请输入正确的手机号");
                    registerPhone.setText("");
                } else if(registerSpreadMobile.length() ==0){
                    showToast("请输入推广人的手机号");
                }else  if(!MobileEncryption.isMobileNO(registerSpreadMobile.getText().toString())){
                    showToast("请输入正确的分享人手机号");
                    registerPhone.setText("");
                } else if(registerSpreadMobile.length() ==0){
                    showToast("请输入分享人的手机号");
                }else  if(!MobileEncryption.isMobileNO(registerSpreadMobile.getText().toString())){
                    showToast("请输入正确的手机号");
                    registerSpreadMobile.setText("");
                }else  if (registerCode.length() == 0) {
                    showToast("请输入收到的验证码");
                } else {
                    verify();
                }
                break;
            case R.id.register_gain:
                if (registerPhone.length() == 0) {
                    showToast("请输入您的手机号");
                } else if (!MobileEncryption.isMobileNO(registerPhone.getText().toString())) {
                    showToast("请输入正确的手机号");
                } else if (register) {
                    showToast("该手机已注册");
                    registerPhone.setText("");
                } else {
                    getverify();
                }
                break;
        }
    }

    //注册
    private void voidgetregister() {
        startMyDialog();
        OkHttpUtils.post().url(Urls.REGISTER).addParams("mobile", registerPhone.getText().toString())
                .addParams("spreadMobile",registerSpreadMobile.getText().toString())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                            .setMessage("注册成功,登陆密码和支付密码为手机号的后六位，请及时更改")//设置显示的内容
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    login();
                                }
                            }).setCancelable(false)
                            .show();//在按键响应事件中显示此对话框
                    showToast("注册成功");

                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    //验证验证码
    private void verify() {
        startMyDialog();
        PostFormBuilder params = new PostFormBuilder();
        params.addParams("","");
        OkHttpUtils.post().url(Urls.VIERFICODE)
                .addParams("mobile",registerPhone.getText().toString())
                .addParams("code",registerCode.getText().toString())
                .addParams("type","1").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    voidgetregister();
                } else {
                    String message = MessgeUtil.geterr_code(code);
                    showToast(message);
                }
            }
        });

    }


    //验证成功自动登陆
    private void login() {
        OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                .addHeader("Authorization", Ips.AUTHORIZATION)
                .addParams("username", registerPhone.getText().toString())
                .addParams("password", registerPhone.getText().toString().substring(5, 11))
                .addParams("grant_type", "password").build()
                .execute(new LoginCallBack());
    }

    private class LoginCallBack extends Callback<LoginEntity> {

        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            stopMyDialog();
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
        }

        @Override
        public LoginEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginEntity loginEntity = new Gson().fromJson(s, new TypeToken<LoginEntity>() {
            }.getType());
            return loginEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (e.getMessage().contains("401") || e.getMessage().contains("403") || e.getMessage().contains("400")) {
                showToast("帐号或密码错误");
            } else if (e.getMessage().contains("502")) {
                showToast("服务器维护中");
            } else {
                showToast("网络链接错误");
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            SharedPreferences share2 = getSharedPreferences("isFirstIn", 0);
            SharedPreferences.Editor editor2 = share2.edit();
            editor2.putString("isFirstIn", "0");
            editor2.commit();
            caches.put("access_token", Config.TOKENHEADER+response.accessToken);
            caches.put("php_token",response.accessToken);
            caches.put("refresh_token",response.refreshToken);
            caches.put("token",response.accessToken);
            caches.put("token_type",response.tokenType);
            caches.put("expires_in",response.expiresIn+"");
            caches.put("scope",response.scope);
            getuserinfo();
            SharedPreferences share3 = getSharedPreferences("mobile", 0);
            SharedPreferences.Editor editor3 = share3.edit();
            editor3.putString("mobile", registerPhone.getText().toString());
            editor3.commit();
            SharedPreferences share = getSharedPreferences("password", 0);
            SharedPreferences.Editor editor = share.edit();
            editor.putString("password", registerPhone.getText().toString().substring(5, 11));
            editor.commit();
            sp.edit().putString("access_token",Config.TOKENHEADER+ response.accessToken).commit();
            sp.edit().putString("refresh_token",response.refreshToken).commit();
            sp.edit().putString("page","0").commit();
            sp.edit().putString("mobile",registerPhone.getText().toString()).commit();
            sp.edit().putString("password",registerPhone.getText().toString().substring(5, 11)).commit();
            startActivity(new Intent(context, HomeActivity.class));
        }
    }

    private void getuserinfo() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);

            }

            @Override
            public void onResponse(String response, int id) {
                caches.put("userinfo", response);
            }
        });
    }

    //获取验证码
    private void getverify() {
        startMyDialog();
        OkHttpUtils.post().url(Urls.VIERFI)
                .addParams("mobile", registerPhone.getText().toString())
                .addParams("type", "1").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
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

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            registerGain.setText("重新发送验证码");
            registerGain.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            registerGain.setClickable(false);
            registerGain.setText("重新发送(" + millisUntilFinished / 1000 + "秒)");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        time.cancel();
    }
}
