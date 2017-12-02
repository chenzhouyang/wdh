package com.yskj.welcomeorchard.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ApproveDialog;
import com.yskj.welcomeorchard.entity.ComPanEntity;
import com.yskj.welcomeorchard.entity.InfoEntity;
import com.yskj.welcomeorchard.entity.LargeGoodOrderEntitiy;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.payment.zfb.PayResult;
import com.yskj.welcomeorchard.ui.order.OrderListActivity;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.Extension;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.GridPasswordView;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.PhotoUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

import static com.yskj.welcomeorchard.R.id.intro_amount;
import static com.yskj.welcomeorchard.R.id.iv_shop_cover;


/**
 * Created by YSKJ-02 on 2017/3/20.
 */

public class IntroActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(intro_amount)
    TextView introAmount;
    @Bind(R.id.intro_Introduction)
    TextView introIntroduction;
    @Bind(R.id.intro_min_amount)
    TextView introMinAmount;
    @Bind(R.id.intro_money)
    RadioButton introMoney;
    @Bind(R.id.intro_pay)
    RadioButton introPay;
    @Bind(R.id.intro_wx)
    RadioButton introWx;
    @Bind(R.id.intro_bank)
    RadioButton introBank;
    @Bind(R.id.home_radio_button_group)
    RadioGroup homeRadioButtonGroup;
    @Bind(iv_shop_cover)
    ImageView currentImageView;
    @Bind(R.id.intro_order)
    TextView introOrder;
    @Bind(R.id.intro_money_amount)
    LinearLayout introMoneyAmount;
    @Bind(R.id.intro_remit)
    EditText introRemit;
    @Bind(R.id.intro_number)
    EditText introNumber;
    @Bind(R.id.intro_amount_treo)
    EditText introAmountTreo;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.compan_name)
    TextView companName;
    @Bind(R.id.compan_khh)
    TextView companKhh;
    @Bind(R.id.compan_card)
    TextView companCard;
    @Bind(R.id.intro_discern)
    LinearLayout intro_discern;
    private double totalmoney;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private double foundamount, isenough;
    private boolean isenoughs = false, isamount = false;
    private PopupWindow popupWindow;
    private Bitmap bitMap;
    String pictureId = "-1";
    private String pay_trade_no, orderid = "1", goodName;
    /**
     * 从相机来的照片路径
     */
    private String path;
    /**
     * 从相册选择照片
     */
    public static final int ALBUM = 1;
    /**
     * 从相机拍照获取照片
     */
    public static final int CAMERA = 2;
    /**
     * 照片来源
     */
    public int pictureFromWhere = 0;
    /**
     * 当前要上传的图片的file路径
     */
    private File mCurrentPhotoFile;
    /**
     * 当前存储的照片名
     */
    private String currentPictureName;
    private int currentOperate = 1001;
    private static final int FRONT_PICTURE = 11;
    private static final String FRONT_NAME = "front";
    private int paytype = -1;
    private static final int SDK_PAY_FLAG = 1;
    private String goodsid,goodsspec;
    private StringBuilder stringBuilders;
    private String id;
    String type;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        setmoeny();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showToast("支付结果确认中");
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showToast("您还未安装支付宝客户端");
                        } else {
                            showToast("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(context, LoginActivity.class));
            return;
        }
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        foundamount = userInfoEntity.data.userVo.fundAccount;
        if (getIntent().getStringExtra("discern").equals("0")) {
            totalmoney = Double.parseDouble(getIntent().getStringExtra("price"));
            introAmount.setText("价格" + StringUtils.getStringtodouble(totalmoney));
            introIntroduction.setText(getIntent().getStringExtra("description"));
            txtTitle.setText(getIntent().getStringExtra("key_name"));
             stringBuilders = new StringBuilder();
            stringBuilders.append("{");
            stringBuilders.append("\""+"套餐"+"\":\""+getIntent().getStringExtra("key")+"\"");
            stringBuilders.append("}");
            goodsid = getIntent().getStringExtra("goods_id");
            goodName =getIntent().getStringExtra("key_name");
            company();
            iniview();
        } else {
            introAmount.setText("汇款、银行转账");
            introMoney.setVisibility(View.GONE);
            introPay.setVisibility(View.GONE);
            introWx.setVisibility(View.GONE);
            intro_discern.setVisibility(View.GONE);
            introBank.setChecked(true);
            introMoneyAmount.setVisibility(View.VISIBLE);
            id = getIntent().getStringExtra("id");
            amend();
        }


        introAmountTreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        introAmountTreo.setText(s);
                        introAmountTreo.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    introAmountTreo.setText(s);
                    introAmountTreo.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        introAmountTreo.setText(s.subSequence(0, 1));
                        introAmountTreo.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        finview();
    }

    //    获取公司账号
    private void company() {
        OkHttpUtils.get().url(Urls.INFOR)
                .addHeader("Authorization", caches.get("access_token"))
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
                ComPanEntity comPanEntity = new Gson().fromJson(response, new TypeToken<ComPanEntity>() {
                }.getType());
                if (comPanEntity.code == 0) {
                    companName.setText("开户名："+comPanEntity.data.cardOwner);
                    companCard.setText("汇款账号："+comPanEntity.data.cardNo+"");
                    companKhh.setText("开户行："+comPanEntity.data.cardOpenBank);
                }
            }
        });
    }

    //修改汇款信息
    private void amend() {

        OkHttpUtils.get().url(Urls.DETAIL)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("lgoId",id)
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
                LargeGoodOrderEntitiy largeGoodOrderEntitiy = new Gson().fromJson(response, new TypeToken<LargeGoodOrderEntitiy>() {
                }.getType());
                if (largeGoodOrderEntitiy.code == 0) {
                    introRemit.setText(largeGoodOrderEntitiy.data.cardOwner);
                    introNumber.setText(largeGoodOrderEntitiy.data.rechargeCard + "");
                    introAmountTreo.setText(DoubleUtils.toDecimalString2(largeGoodOrderEntitiy.data.rechargeAmount));
                    orderid = largeGoodOrderEntitiy.data.orderId;
                    goodName = largeGoodOrderEntitiy.data.goodName;
                    GlideImage.loadImage(context,currentImageView,largeGoodOrderEntitiy.data.credentials.get(0).url,R.mipmap.img_error);
                    pictureId = largeGoodOrderEntitiy.data.credentials.get(0).id;
                } else {
                    showToast(MessgeUtil.geterr_code(largeGoodOrderEntitiy.code));
                }

            }
        });
    }

    private void iniview() {
        isenough = DoubleUtils.sub(totalmoney, foundamount);
        if (foundamount >= totalmoney) {
            introMoney.setVisibility(View.VISIBLE);
            introPay.setVisibility(View.GONE);
            introWx.setVisibility(View.GONE);
            introMoney.setChecked(true);
            isenough = 0.00;
            isenoughs = true;
            paytype = 0;
        } else {
            isenoughs = false;
            if (isenough > 5000) {
                isamount = true;
                introMoney.setVisibility(View.GONE);
                introPay.setVisibility(View.GONE);
                introWx.setVisibility(View.GONE);
                introBank.setChecked(true);
                paytype = 3;
                introMoneyAmount.setVisibility(View.VISIBLE);
            } else {
                isamount = false;
                introMoney.setVisibility(View.GONE);
                introPay.setVisibility(View.VISIBLE);
                introWx.setVisibility(View.VISIBLE);
                introPay.setChecked(true);
                paytype = 1;
            }

        }
        introMinAmount.setText("最低汇款金额：" + StringUtils.getStringtodouble(isenough));

    }

    private void finview() {

        homeRadioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //可用通贝
                    case R.id.intro_money:
                        paytype = 0;
                        introMoneyAmount.setVisibility(View.GONE);
                        break;
                    //支付宝
                    case R.id.intro_pay:
                        paytype = 1;
                        introMoneyAmount.setVisibility(View.GONE);
                        break;
                    //微信
                    case R.id.intro_wx:
                        paytype = 2;
                        introMoneyAmount.setVisibility(View.GONE);
                        break;
                    //汇款
                    case R.id.intro_bank:
                        paytype = 3;
                        introMoneyAmount.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.intro_order, iv_shop_cover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.intro_order:
                //请求订单
                if(getIntent().getStringExtra("discern").equals("0")){
                    if (paytype == 3 ) {
                        if (introRemit.length() == 0) {
                            showToast("请填写持卡人姓名");
                        } else if (introNumber.length() == 0) {
                            showToast("请填写汇款卡号");
                        } else if (introAmountTreo.length() == 0) {
                            showToast("请填写汇款金额");
                        } else if(Double.parseDouble(introAmountTreo.getText().toString())<isenough){
                            showToast("汇款金额不能小于最低汇款金额");
                        }else if (pictureId.equals("-1")) {
                            showToast("请上传银行交易流水号、银行汇款回执单");
                        } else {
                            if(userInfoEntity.data.realnameVo==null&&userInfoEntity.data.realnameVo.name==null){
                                ApproveDialog dialog = new ApproveDialog(context,"您还未实名认证，是否前往认证？","4");
                                dialog.show();
                            }else {
                                wholesale();
                            }
                        }
                    }else {
                        wholesale();
                    }

                }else {
                    reserve();
                }

                break;
            case iv_shop_cover:
                dialogShow(view);
                break;

        }
    }
//请求订单
    private void wholesale(){
        StringBuilder strJson = new StringBuilder();
        strJson.append("{\"goods_id\":\""+goodsid+"\","+"\"goods_num\":\""+"1"+"\","+"\"goods_spec\":"+stringBuilders+","+"\"token\":\""+caches.get("php_token")+"\"}");
        OkHttpUtils.postString().url(Urls.WHOLESALE).content(strJson.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
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

            }

            @Override
            public void onResponse(String response, int id) {
                Map<String ,Object> map = JSONFormat.jsonToMap(response);
                String code = (String) map.get("error_code");
                if(code.equals("000")){
                    orderid = (String) map.get("order_id");
                    if (isenoughs && paytype == 0 && !isamount) {
                        //通贝足够
                        SurePwdDialog dialog = new SurePwdDialog(context);
                        dialog.show();
                    } else if (isenoughs && paytype == 1 && !isamount) {
                        //通贝足够支付选择支付类型，支付宝
                        getamount();
                    } else if (isenoughs && paytype == 2 && !isamount) {
                        //通贝足够支付选择支付类型，微信
                        getamount();
                    } else if (isenoughs && paytype == 3 && !isamount) {
                        //通贝足够支付选择支付类型，汇款
                        //showToast("通贝足够支付选择支付类型，汇款");
                                reserve();
                    } else if (!isenoughs && paytype == 1 && !isamount) {
                        //通贝不足，最低金额小于5000，支付选择支付类型，支付宝
                        getamount();
                    } else if (!isenoughs && paytype == 2 && !isamount) {
                        //通贝不足，最低金额小于5000，支付选择支付类型，微信
                        getamount();
                    } else if (!isenoughs && paytype == 3 && !isamount) {
                        //通贝不足，最低金额小于5000，支付选择支付类型，汇款
                                reserve();
                    } else if (!isenoughs && paytype == 3 && isamount) {
                        //通贝不足，最低金额大于5000，支付类型，汇款
                                reserve();

                    }
                }
            }
        });
    }
    //预定
    private void reserve() {
        if(getIntent().getStringExtra("discern").equals("0")){
            OkHttpUtils.post().url(Urls.BESWPEAK).addHeader("Authorization", caches.get("access_token"))
                    .addParams("orderId", orderid)
                    .addParams("goodName", goodName)
                    .addParams("rechargeCard", introNumber.getText().toString())
                    .addParams("cardOwner", introRemit.getText().toString())
                    .addParams("rechargeAmount", introAmountTreo.getText().toString())
                    .addParams("type", "0")
                    .addParams("credential", pictureId)
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
                        showToast("预约成功");
                        startActivity(new Intent(context, IntroOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }else if(code ==911 ){
                        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                .setMessage("您有一次预约正在审批中，不能再次预约")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    }
                                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                    }else if(code == 915){
                        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                .setMessage("您有一次审批不通过的预约，请修改后再次预约")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                    }
                                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框

                    }else {
                        showToast(MessgeUtil.geterr_code(code));
                    }
                }
            });
        }else {
            OkHttpUtils.post().url(Urls.BESWPEAK).addHeader("Authorization", caches.get("access_token"))
                    .addParams("orderId", orderid)
                    .addParams("id",id)
                    .addParams("goodName", goodName)
                    .addParams("rechargeCard", introNumber.getText().toString())
                    .addParams("cardOwner", introRemit.getText().toString())
                    .addParams("rechargeAmount", introAmountTreo.getText().toString())
                    .addParams("type", "0")
                    .addParams("credential", pictureId)
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
                        showToast("预约成功");
                        startActivity(new Intent(context, IntroOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }else {
                        showToast(MessgeUtil.geterr_code(code));
                    }
                }
            });
        }

    }


    //支付宝，微信购买
    private void getamount() {
        OkHttpUtils.post().url(Urls.PAY).addHeader("Authorization", caches.get("access_token"))
                .addParams("price", DoubleUtils.toDecimalString2(isenough))
                .addParams("order_id",orderid)
                .addParams("order_type","1")
                .addParams("pay_type", paytype + "")
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
                    JSONObject payinfor = (JSONObject) map.get("data");
                    final String PY_info;
                    try {
                        pay_trade_no = payinfor.getString("pay_trade_no");
                        if (paytype == 1) {
                            PY_info = (String) payinfor.get("pay_info");

                            Runnable payRunnable = new Runnable() {
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask aliPay = new PayTask(
                                            IntroActivity.this);
                                    // 调用支付接口，获取支付结果
                                    String result = aliPay.pay(
                                            PY_info, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else if (paytype == 2) {
                            JSONObject params = payinfor.getJSONObject("pay_info");
                            if (Extension.isWeixinAvilible(getApplicationContext())) {
                                String appid = params.getString("appid");
                                caches.put("ordertype", "0");
                                caches.put("orderid", orderid);
                                caches.put("pay_trade_no", pay_trade_no);
                                IWXAPI api = WXAPIFactory.createWXAPI(IntroActivity.this, appid, false);
                                api.registerApp(appid);
                                PayReq req = new PayReq();
                                req.appId = params.getString("appid");
                                req.partnerId = params.getString("partnerid");
                                req.prepayId = params.getString("prepayid");
                                req.packageValue = "Sign=WXPay";
                                req.nonceStr = params.getString("noncestr");
                                req.timeStamp = params.getString("timestamp");
                                req.sign = params.getString("sign");
                                api.sendReq(req);

                            } else {
                                showToast("您还未安装微信客户端");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }


    //充值完成后完成订单交易
    private void setmoeny() {
        startMyDialog();

        OkHttpUtils.post().url(Urls.PAYORDER)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId", orderid)
                .addParams("tradeNo", pay_trade_no)
                .addParams("payType", "1")
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
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    /*orderpay();*/
                    startActivity(new Intent(context, OrderListActivity.class).putExtra("orderpaytype", "0").putExtra("orderstaut", "2").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    //可用通贝足够，填写唯多惠密码
    public class SurePwdDialog extends Dialog {
        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        public SurePwdDialog(Context context) {
            super(context, R.style.GiftDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);
            initView();
        }

        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            tv_forget_pwd.setVisibility(View.GONE);//不显示密码框下边的钱
            pwdView = (GridPasswordView) findViewById(R.id.pwd);
            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {
                }

                @Override
                public void onMaxLength(String psw) {
                    dismissJP();
                    dismiss();
                    //调取java接口完成订单
                    OkHttpUtils.post().url(Urls.PAYORDER)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("orderId", orderid)
                            .addParams("payType", "0")
                            .addParams("payPassword", pwdView.getPassWord())
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
                            stopMyDialog();
                            isLogin(e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            stopMyDialog();
                            Map<String, Object> map = JSONFormat.jsonToMap(response);
                            int code = (int) map.get("code");
                            if (code == 0) {
                                startActivity(new Intent(context, OrderListActivity.class).putExtra("orderpaytype", "0").putExtra("orderstaut", "2").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else {
                                showToast(MessgeUtil.geterr_code(code));
                            }
                        }
                    });
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_forget_pwd.setText(StringUtils.getStringtodouble(isenough));

            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
    }

    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) IntroActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 选择图片来源
     */
    private void dialogShow(View view) {
        getPhotoPopupWindow(R.layout.popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.anim_popup_dir);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /***
     * 获取图片PopupWindow实例
     */
    private void getPhotoPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPhotoPopuptWindow(resource, width, height, animationStyle);
        }
    }

    /**
     * 图片PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        // 点击其他地方消失
      /*  popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Logger.d("***********");
                if (popupWindow != null && popupWindow.isShowing()) {

                    popupWindow.dismiss();
                    popupWindow = null;
                }
                backgroundAlpha(1f);
                return false;
            }
        });*/
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        pictureFromWhere = CAMERA;
        doTakePhoto();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }

    }

    /**
     * 从相册选择照片
     */
    private void doSelectImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, currentOperate);
    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        pictureFromWhere = ALBUM;
        doSelectImageFromAlbum();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }


    private File getPicturePathFromAlbum(Intent data) {
        Uri uri = data.getData();
        uri = geturi(data);//解决方案
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 5;   // width，hight设为原来的十分一
            try {
                bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                compressImage(bitMap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
        } else {
            return null;
        }
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     *
     * @param intent
     * @return
     */
    public Uri geturi(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    private void getPicturePathFromCamera() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 5;   // width，hight设为原来的十分一
        try {
            bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mCurrentPhotoFile)), null, options);
            compressImage(bitMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (baos.toByteArray().length == 0) {
            return null;
        }
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 调用系统相机拍照
     */
    protected void doTakePhoto() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
            if (!file.exists()) {
                file.mkdirs();
            }
            mCurrentPhotoFile = new File(file, PhotoUtil.getRandomFileName() + ".jpg");
            path = Environment.getExternalStorageDirectory() + "/DCIM/Photo/" + PhotoUtil.getRandomFileName();
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, currentOperate);
        } catch (ActivityNotFoundException d) {
            Toast.makeText(this, "手机中无可用的图片", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            if (pictureFromWhere == ALBUM) {
                mCurrentPhotoFile = getPicturePathFromAlbum(data);
                if (bitMap != null) {
                    if (bitMap.getByteCount() > (1024 * 1024 * 3)) {
                        showToast("请上传小于3M的图片");
                        return;
                    }
                }

            } else {
                getPicturePathFromCamera();
            }
            currentPictureName = FRONT_NAME;
            //上传方法
            uploadPicture();
        }
    }

    /**
     * 上传图片方法，所有上传均走此方法，改变之前的一个图片一个方法导致的代码臃肿问题
     */
    private void uploadPicture() {
        if (mCurrentPhotoFile == null) {
            showToast("获取照片失败，请重试");
        } else {
            OkHttpUtils.post().url(Urls.UPDATRSVTEA)
                    .addParams("userId", userInfoEntity.data.userVo.id + "")
                    .addFile("files", System.currentTimeMillis() + ".jpg", mCurrentPhotoFile).build().execute(new StringCallback() {
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
                    InfoEntity infoEntity = new Gson().fromJson(response, new TypeToken<InfoEntity>() {
                    }.getType());
                    if (infoEntity.code == 0) {
                        showToast("上传成功");
                        pictureId = infoEntity.data.get(0);
                        sp.edit().putString(currentPictureName, pictureId).commit();
                        currentImageView.setImageBitmap(bitMap);
                        bitMap = null;
                    } else {
                        showToast(MessgeUtil.geterr_code(infoEntity.code));
                    }
                }
            });
        }

    }
}
