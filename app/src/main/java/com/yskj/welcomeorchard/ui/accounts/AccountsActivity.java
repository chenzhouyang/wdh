package com.yskj.welcomeorchard.ui.accounts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AccountListAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ApproveDialog;
import com.yskj.welcomeorchard.dialog.PassWordDialog;
import com.yskj.welcomeorchard.entity.AccounsEntity;
import com.yskj.welcomeorchard.entity.AccountListEntity;
import com.yskj.welcomeorchard.entity.MoneyInfoEntity;
import com.yskj.welcomeorchard.entity.RealNameEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MobileEncryption;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by YSKJ-02 on 2017/1/13.
 * 转账
 */

public class AccountsActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.verify_tv)
    TextView verifyTv;
    @Bind(R.id.accounts_pull_lv)
    XRecyclerView accountsPullLv;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.my_accounts_amount)
    EditText accountsAmount;
    @Bind(R.id.accounts_phone)
    EditText accountsPhone;
    @Bind(R.id.accounts_password)
    EditText accountsPassword;
    @Bind(R.id.accounts_pull_scrollview)
    PullToRefreshScrollView accountsPullScrollview;
    @Bind(R.id.accounts_name)
    TextView accountsName;
    private AccounsEntity accounsEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int count = 10, cursor = 0;
    private UserInfoEntity userInfoEntity;
    private AccountListAdapter adapter;
    private ArrayList<AccountListEntity.DataBean.ListBean> listBeenAll = new ArrayList<>();
    private ArrayList<AccountListEntity.DataBean.ListBean> listBeenpage = new ArrayList<>();
    private boolean accounts = true,amount = false,Phone = false,Password = false;//判断实名认证是否存在该用户
    private MoneyInfoEntity moneyInfoEntity;
    private GridLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);
        txtTitle.setText("转账");

        accountsAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        accountsAmount.setText(s);
                        accountsAmount.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    accountsAmount.setText(s);
                    accountsAmount.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        accountsAmount.setText(s.subSequence(0, 1));
                        accountsAmount.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(accountsAmount.length() == 0){
                    showToast("请填写转账金额");
                    amount = false;
                }else {
                    amount = true;
                }
                if(amount&&Phone&&Password){
                    verifyTv.setBackgroundResource(R.drawable.login_btn_true);
                }else {
                    verifyTv.setBackgroundResource(R.drawable.login_btn);
                }
            }
        });
        accountsPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MobileEncryption.isMobileNO(accountsPhone.getText().toString())) {
                        Phone = true;
                        getname(accountsPhone.getText().toString());
                }else {
                    Phone = false;
                }
                if(amount&&Phone&&Password){
                    verifyTv.setBackgroundResource(R.drawable.login_btn_true);
                }else {
                    verifyTv.setBackgroundResource(R.drawable.login_btn);
                }
            }
        });
        accountsPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(accountsPassword.length() < 6){
                    Password = false;
                }else {
                    Password = true;
                }
                if(amount&&Phone&&Password){
                    verifyTv.setBackgroundResource(R.drawable.login_btn_true);
                }else {
                    verifyTv.setBackgroundResource(R.drawable.login_btn);
                }
            }
        });
        //提现列表
        getaccountlist();
        iniscrollview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        getmoenyinfor();
        if (!userInfoEntity.data.accountPasswordExist) {
            caches.put("type", "account");
            PassWordDialog dialog = new PassWordDialog(context, "您还没有设置支付密码,是否前往设置");
            dialog.show();
        }
        UserInfoEntity.DataBean.RealnameVoBean realname = userInfoEntity.data.realnameVo;
        if (realname == null || realname.name == null) {
            ApproveDialog dialog = new ApproveDialog(context, "您还未实名认证，是否前往认证？", "2");
            dialog.show();
        }
    }
    /**
     * 请求资金
     */
    private void getmoenyinfor(){
        OkHttpUtils.get().url(Urls.MONEYINFO).addHeader("Authorization", caches.get("access_token"))
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
                moneyInfoEntity = new Gson().fromJson(response,new TypeToken<MoneyInfoEntity>(){}.getType());
                if(moneyInfoEntity.code == 0){
                    accountsAmount.setHint("最大额度" + moneyInfoEntity.data.fundAccount);
                }

            }
        });
    }
    //查询是否实名认证
    private void getname(String mobile) {
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
                if (code == 0) {
                    accounts = true;
                    RealNameEntity realNameEntity = new Gson().fromJson(response,new TypeToken<RealNameEntity>(){}.getType());
                    accountsName.setText(realNameEntity.data.name);
                }else if(code == 101){
                    accounts = false;
                    showToast("该用户还没有实名认证");
                    accountsPhone.setText("");
                    accountsName.setText("");
                }else {
                    accountsPhone.setText("");
                    accountsName.setText("");
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    //刷新提现列表
    private void iniscrollview() {
        adapter = new AccountListAdapter(context, listBeenAll);
        layoutManager = new GridLayoutManager(context, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        accountsPullLv.setLayoutManager(layoutManager);
        accountsPullLv.setHasFixedSize(true);
        accountsPullLv.setPullRefreshEnabled(false);
        accountsPullLv.addItemDecoration(new DividerGridItemDecoration(context, 15, R.drawable.divider));
        accountsPullLv.setAdapter(adapter);
        accountsPullScrollview.setMode(PullToRefreshBase.Mode.BOTH);
        accountsPullScrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor = 0;
                getaccountlist();
                accountsPullScrollview.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor++;
                getaccountlist();
                accountsPullScrollview.onRefreshComplete();
            }
        });
    }

    //提现列表
    private void getaccountlist() {
        OkHttpUtils.get().url(Urls.ACCOUNTLIST).addHeader("Authorization", caches.get("access_token"))
                .addParams("count", count + "").addParams("cursor", cursor * 10 + "")
                .build().execute(new AccountListCallBack());
    }

    private void setaccount() {

        OkHttpUtils.post().url(Urls.ACCOUNTS).addHeader("Authorization", caches.get("access_token")).addParams("mobile", accountsPhone.getText().toString())
                .addParams("amount", accountsAmount.getText().toString()).addParams("paymentPwd", accountsPassword.getText().toString())
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
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    accounsEntity = new Gson().fromJson(response, new TypeToken<AccounsEntity>() {
                    }.getType());
                    new AlertDialog.Builder(AccountsActivity.this).setTitle("系统提示")
                            .setMessage("转账成功")

                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    getmoenyinfor();
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .show();//在按键响应事件中显示此对话框
                } else {
                    new AlertDialog.Builder(AccountsActivity.this).setTitle("系统提示")
                            .setMessage(MessgeUtil.geterr_code(code))
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                }
                            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框
                }

            }
        });
    }

    @OnClick({R.id.img_back, R.id.verify_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.verify_tv:
                //提现
                if (accountsAmount.length() == 0) {
                    showToast("请入转账金额");
                }else if (moneyInfoEntity.data.fundAccount-Double.parseDouble(accountsAmount.getText().toString())<0){
                    showToast("转账金额过大");
                }else if (accountsPhone.length() == 0) {
                    showToast("请输入对方的手机号");
                } else if(!MobileEncryption.isMobileNO(accountsPhone.getText().toString())) {
                    showToast("请输入正确的手机号");
                }else if(!accounts){
                    showToast("该用户还没有实名认证");
                }else if (accountsPassword.length() == 0) {
                    showToast("请输入支付密码");
                } else  if(!Password){
                    showToast("支付密码为6位数字");
                }else {
                    setaccount();
                }

                break;
        }
    }

    public class AccountListCallBack extends Callback<AccountListEntity> {
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
        public AccountListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            AccountListEntity accountListEntity = new Gson().fromJson(s, new TypeToken<AccountListEntity>() {
            }.getType());
            return accountListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(AccountListEntity response, int id) {
            if (response.code == 0) {
                listBeenpage.clear();
                listBeenpage.addAll(response.data.list);
                if (listBeenpage.size() != 0) {
                    if (cursor == 0) {
                        listBeenAll.clear();
                    }
                    listBeenAll.addAll(listBeenpage);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "暂时没有转账记录", Toast.LENGTH_SHORT).show();
                }

            } else {
                String message = MessgeUtil.geterr_code(response.code);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }


        }
    }
}
