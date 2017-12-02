package com.yskj.welcomeorchard.ui.deposit;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ApproveDialog;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by YSKJ-02 on 2017/1/13.
 * 添加银行卡
 */

public class AddBankActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.addbank_truename)
    TextView addbankTruename;
    @Bind(R.id.addbank_diglog_ll)
    LinearLayout addbankDiglogLl;
    @Bind(R.id.addbank_number_et)
    EditText addbankNumberEt;
    @Bind(R.id.addbank_next)
    TextView addbankNext;
    @Bind(R.id.addbank_bank)
    TextView addbankBank;
    private String cardType;
    private String type;
    private String url;
    private String cardid;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity infoEntity;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbank);
        ButterKnife.bind(this);
        token = caches.get("access_token");
        txtTitle.setText("添加银行卡");
        infoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        UserInfoEntity.DataBean.RealnameVoBean realname = infoEntity.data.realnameVo;
        if(realname !=null&&realname.name!=null){
            addbankTruename.setText(infoEntity.data.realnameVo.name);
        }else {
            ApproveDialog dialog = new ApproveDialog(context,"您还未实名认证，是否前往认证？","1");
            dialog.show();
        }
        type = getIntent().getStringExtra("type");
        if(type.equals("1")){
            addbankTruename.setText(getIntent().getStringExtra("name"));
            addbankBank.setText(getIntent().getStringExtra("bank"));
            addbankNumberEt.setText(getIntent().getStringExtra("cardNo"));
            cardType = getIntent().getStringExtra("cardType");
            cardid = getIntent().getStringExtra("cardid");
            url = Urls.UPDATEBANK;
        }else {
            url = Urls.ADDBANK;
        }
    }

    @OnClick({R.id.img_back, R.id.addbank_diglog_ll, R.id.addbank_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.addbank_diglog_ll:
                BankDialog dialog = new BankDialog(context);
                dialog.show();
                break;
            case R.id.addbank_next:
                if(addbankTruename.length()==0){
                    showToast("请输入您的姓名");
                }else if(addbankBank.getText().toString().equals("...")){
                    showToast("请选择要添加的银行卡");
                }else if(addbankNumberEt.length()== 0){
                    showToast("请填写您的银行卡");
                }else {
                    if(type.equals("1")){
                        setupdatabank();
                    }else {
                        setbank();
                    }

                }
                break;
        }
    }
    //添加银行卡
    private void setbank(){
        startMyDialog();

        OkHttpUtils.post().url(url).addHeader("Authorization",token)
                .addParams("bank",addbankBank.getText().toString()).addParams("cardNo",addbankNumberEt.getText().toString())
                .addParams("name",addbankTruename.getText().toString()).addParams("cardType",cardType+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    showToast("添加成功");
                    getuserinfo();
                    startActivity(new Intent(context,BankListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    //修改银行卡
    private void setupdatabank(){
        startMyDialog();

        OkHttpUtils.post().url(url)
                .addHeader("Authorization",token)
                .addParams("bank",addbankBank.getText().toString()).addParams("cardNo",addbankNumberEt.getText().toString())
                .addParams("cardId",cardid)
                .addParams("name",addbankTruename.getText().toString()).addParams("cardType",cardType+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    if(type.equals("1")){
                        showToast("修改成功");
                    }else {
                        showToast("添加成功");
                    }

                    startActivity(new Intent(context,BankListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    public class BankDialog extends Dialog {
        private Context context;
        private ListView dialog_lv;

        public BankDialog(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_bank);
            initView();
            setbank();
        }

        private void setbank() {
            //准备要添加的数据条目
            final List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("bankimage", R.mipmap.china_pay_ico);//中国银行
            item.put("banktext", "中国银行");
            items.add(item);
            item = new HashMap<>();
            item.put("bankimage", R.mipmap.buess_pay_ico);//中国工商银行
            item.put("banktext", "工商银行");
            items.add(item);
            item = new HashMap<>();
            item.put("bankimage", R.mipmap.jian_pay_ico);//中国建设银行
            item.put("banktext", "建设银行");
            items.add(item);
            item = new HashMap<>();
            item.put("bankimage", R.mipmap.long_pay_ico);//中国农业银行
            item.put("banktext", "农业银行");
            items.add(item);
            item = new HashMap<>();
            item.put("bankimage", R.mipmap.zhao_pay_ico);//招商银行
            item.put("banktext", "招商银行");
            items.add(item);
            item = new HashMap<>();
            item.put("bankimage", R.mipmap.jiao_pay_ico);//交通银行
            item.put("banktext", "交通银行");
            items.add(item);
            item = new HashMap<>();
            item.put("bankimage", R.mipmap.zhifu_pay_ico);//支付宝
            item.put("banktext", "支付宝");
            items.add(item);
           /* item = new HashMap<String, Object>();
            item.put("bankimage", R.mipmap.wei_pay_ico);//微信
            item.put("banktext", "微信");
            items.add(item);*/
            //实例化一个适配器
            SimpleAdapter adapter = new SimpleAdapter(context,
                    items,
                    R.layout.item_dialog_bank,
                    new String[]{"bankimage", "banktext"},
                    new int[]{R.id.item_dialog_bank_image, R.id.item_dialog_bank_tv});
            dialog_lv.setAdapter(adapter);
            dialog_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismiss();
                    addbankBank.setText(items.get(position).get("banktext")+"");
                    if(position == 7){
                        cardType = "2";
                    }else if(position == 6){
                        cardType = "1";
                    }else {
                        cardType = "0";
                    }
                }
            });
        }

        private void initView() {
            dialog_lv = (ListView) findViewById(R.id.dialog_lv);
        }

    }
    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", token)
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
}
