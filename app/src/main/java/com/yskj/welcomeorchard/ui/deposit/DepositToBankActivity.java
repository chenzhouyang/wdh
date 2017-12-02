package com.yskj.welcomeorchard.ui.deposit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.PassWordDialog;
import com.yskj.welcomeorchard.entity.RecordEntiity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.verify.VerifyActivity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

import static com.yskj.welcomeorchard.R.id.money_ev;


/**
 * Created by YSKJ-02 on 2017/1/13.
 * 提现到银行卡
 */

public class DepositToBankActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.deposittobank_maxmoeny_tv)
    TextView deposittobankMaxmoenyTv;
    @Bind(money_ev)
    EditText moneyEv;
    @Bind(R.id.password_ev)
    EditText passwordEv;
    @Bind(R.id.amend_password)
    TextView amendPassword;
    @Bind(R.id.deposittobank_true)
    TextView deposittobankTrue;
    private RecordEntiity recordlbean;
    private String cardid;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposittobank);
        ButterKnife.bind(this);
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        txtTitle.setText("提现");
        cardid = getIntent().getStringExtra("cardid");
        if(!userInfoEntity.data.accountPasswordExist){
            caches.put("type","tobank");
            caches.put("cardid",cardid);
            PassWordDialog dialog = new PassWordDialog(context,"您还没有设置支付密码,是否前往设置");
            dialog.show();
        }

        deposittobankMaxmoenyTv.setText("当前可提最大额度"+ StringUtils.getStringtodouble(userInfoEntity.data.userVo.fundAccount));
        moneyEv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        moneyEv.setText(s);
                        moneyEv.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    moneyEv.setText(s);
                    moneyEv.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        moneyEv.setText(s.subSequence(0, 1));
                        moneyEv.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setbanktocard() {
        OkHttpUtils.post().url(Urls.REEORD)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("cardId",cardid).addParams("amount",moneyEv.getText().toString())
                .addParams("paymentPwd",passwordEv.getText().toString())
                .addParams("type","0").build().execute(new StringCallback() {
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
                if(code == 0){
                    showToast("提现成功");
                    getuserinfo();
                }else {
                    stopMyDialog();
                    String message = MessgeUtil.geterr_code(code);
                    showToast(message);
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.deposittobank_true
    ,R.id.amend_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.deposittobank_true:
                if(moneyEv.length() == 0){
                    showToast("请输入提现金额");
                }else if(Double.parseDouble(moneyEv.getText().toString())<100){
                    showToast("提现金额不能少于100");
                }else if (userInfoEntity.data.userVo.fundAccount-Double.parseDouble(moneyEv.getText().toString())<0){
                    showToast("提现金额过大");
                }else if(passwordEv.length() == 0){
                    showToast("请输入支付密码");
                }else {
                    setbanktocard();
                }

                break;
            case R.id.amend_password:
                caches.put("type","tobank");
                caches.put("cardid",cardid);
                startActivity(new Intent(context, VerifyActivity.class).putExtra("type","6"));
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    private void getuserinfo() {

        OkHttpUtils.get().url(Urls.MONEYINFO).addHeader("Authorization", caches.get("access_token"))
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
                AppManager.getInstance().killActivity(DepositActivity.class);
                AppManager.getInstance().killActivity(DepositToBankActivity.class);
                finish();
            }
        });
    }
}
