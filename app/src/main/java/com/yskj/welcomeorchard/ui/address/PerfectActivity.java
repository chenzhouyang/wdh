package com.yskj.welcomeorchard.ui.address;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.AddressEntity;
import com.yskj.welcomeorchard.entity.City;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.ui.order.ConfirmOrderActivity;
import com.yskj.welcomeorchard.utils.CommonUtils;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yskj.welcomeorchard.utils.StringUtils.stringFilter;

/**
 * Created by YSKJ-02 on 2017/1/14.
 * 添加收货地址
 */

public class PerfectActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.perfect_consignee_name)
    EditText perfectConsigneeName;
    @Bind(R.id.perfect_phone)
    EditText perfectPhone;
    @Bind(R.id.perfect_address_tv)
    TextView perfectAddressTv;
    @Bind(R.id.perfect_address_ll)
    LinearLayout perfectAddressLl;
    @Bind(R.id.perfect_address_detial)
    EditText perfectAddressDetial;
    @Bind(R.id.verify_tv)
    TextView verifyTv;
    @Bind(R.id.perfect_true)
    LinearLayout perfectTrue;
    private City city;
    private String Province, citys, District,Provinceid, citysid, Districtid,type,url;
    private boolean isphoneNum = false;
    private AddressEntity.AddressListBean addressbean;
    private Intent intent;
    private int id;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect);
        ButterKnife.bind(this);
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"),new TypeToken<UserInfoEntity>(){}.getType());
        if(caches.get("access_token").equals("null")){
           startActivity(new Intent(context, LoginActivity.class));
        }
        id = userInfoEntity.data.userVo.id;
        intview();
    }

    private void intview() {
        txtTitle.setText("编辑地址");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 收件人只允许字母、数字和汉字
        perfectConsigneeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = perfectConsigneeName.getText().toString();
                String str = stringFilter(editable.toString());
                if(!editable.equals(str)){
                    perfectConsigneeName.setText(str);
                    //设置新的光标所在位置
                    perfectConsigneeName.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //校验手机号
        perfectPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(CommonUtils.isPhoneNumer(perfectPhone.getText().toString())){
                    isphoneNum = true;
                }else {
                    isphoneNum = false;
                }

            }
        });
        type = getIntent().getStringExtra("addresstype");
        if(type.equals("0")){
            addressbean = (AddressEntity.AddressListBean) getIntent().getSerializableExtra("address");
            perfectConsigneeName.setText(addressbean.consignee);
            perfectAddressTv.setText(addressbean.fullAddress);
            perfectPhone.setText(addressbean.mobile);
            perfectAddressDetial.setText(addressbean.address);
            Provinceid = addressbean.province;
            citysid = addressbean.city;
            Districtid = addressbean.district;

        }

    }

    @OnClick({R.id.img_back, R.id.perfect_address_ll, R.id.perfect_true})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                break;
            case R.id.perfect_address_ll:
                Intent in = new Intent(context, CitySeletActivity.class);
                in.putExtra("city", city);
                startActivityForResult(in, 1);
                break;
            case R.id.perfect_true:
                truesave();
                break;
        }
    }

    private void truesave() {
        if(perfectConsigneeName.getText().toString().isEmpty()){
            showToast("请输入收件人姓名");
        }else if(!isphoneNum||perfectPhone.getText().toString().isEmpty()){
            showToast("请输入正确的手机号");
        }else if(perfectAddressTv.getText().toString().equals("选择所在地")){
            showToast("请选择城市");
        }else if(perfectAddressDetial.getText().toString().isEmpty()){
            showToast("请输入详细地址");

        }else {
            if (type.equals("0")){
                url = Urls.GENXINADDRESS+id + "/address_id/" + addressbean.addressId + "/name/" +
                        encipher(perfectConsigneeName.getText().toString().trim()) + "/province/" + Provinceid + "/city/" + citysid + "/area/" +
                        Districtid + "/detail/" + encipher(perfectAddressDetial.getText().toString().trim()) + "/mobile/" +
                        encipher(perfectPhone.getText().toString().trim()) + "";
            }else {
                url = Urls.ADDADDRESS + id + "/name/" + encipher(perfectConsigneeName.getText().toString().trim()) +
                        "/province/" + Provinceid + "/city/" + citysid + "/area/" + Districtid + "/detail/" +
                        encipher(perfectAddressDetial.getText().toString().trim()) + "/mobile/" + encipher(perfectPhone.getText().toString().trim()) + "/defaul/1";
            }
            OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    try{
                        String code = (String) map.get("error_code");
                        if(code.equals("000")){
                            if(type.equals("0")){
                                showToast("信息修改成功");
                                startActivity(new Intent(context, AddressActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                AppManager.getInstance().killActivity(PerfectActivity.this);
                            }else if (type.equals("1")){
                                showToast("信息保存成功");
                                if(caches.get("addresscode").equals("0")){
                                    startActivity(new Intent(context, ConfirmOrderActivity.class).putExtra("cartlist",getIntent().getSerializableExtra("cartlist")).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                                AppManager.getInstance().killActivity(PerfectActivity.this);
                            }else {
                                AppManager.getInstance().killActivity(PerfectActivity.this);
                            }
                        }else {
                            showToast("信息保存失败");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8) {
            if (requestCode == 1) {
                Province = data.getStringExtra("Province");
                citys = data.getStringExtra("City");
                District = data.getStringExtra("Area");
                perfectAddressTv.setText(Province + citys + District);
                Provinceid = data.getStringExtra("ProvinceId");
                citysid = data.getStringExtra("CityId");
                Districtid = data.getStringExtra("AreaId");
            }
        }
    }
    private String encipher(String str) {
        String newStr = null;
        byte[] temp = new byte[0];//这里写原编码方式
        try {
            temp = str.getBytes("utf-8");
            byte[] newtemp = new String(temp, "utf-8").getBytes("gbk");//这里写转换后的编码方式
            newStr = new String(newtemp, "gbk");//这里写转换后的编码方式
            return  newStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newStr;
    }
}
