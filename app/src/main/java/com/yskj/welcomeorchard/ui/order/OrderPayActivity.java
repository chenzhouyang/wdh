package com.yskj.welcomeorchard.ui.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.PassWordDialog;
import com.yskj.welcomeorchard.entity.MoneyInfoEntity;
import com.yskj.welcomeorchard.entity.OrderPayEntity;
import com.yskj.welcomeorchard.entity.PayOrderAgainEntity;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.payment.zfb.PayResult;
import com.yskj.welcomeorchard.ui.advertising.ChooseDishActivity;
import com.yskj.welcomeorchard.ui.localserver.LocalServerOrderActivity;
import com.yskj.welcomeorchard.ui.supply.ShoppingCartActivity;
import com.yskj.welcomeorchard.ui.supply.SourceParticularsActivity;
import com.yskj.welcomeorchard.ui.supply.SupplyConfirmActivity;
import com.yskj.welcomeorchard.ui.supply.SurceOrderActivity;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.Extension;
import com.yskj.welcomeorchard.utils.GridPasswordView;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-JH on 2017/1/15.
 */

public class OrderPayActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.order_pay)
    TextView orderPay;
    private static final int SDK_PAY_FLAG = 1;
    private final String mMode = "00";
    @Bind(R.id.listView)
    NoScrollListView listView;
    @Bind(R.id.tv_sn)
    TextView tvSn;
    @Bind(R.id.tv_balance)
    TextView tvBalance;
    @Bind(R.id.tv_sure_pay)
    TextView tvSurePay;
    @Bind(R.id.subbt)
    ImageView subbt;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.addbt)
    ImageView addbt;
    @Bind(R.id.order_pay_num)
    LinearLayout orderPayNum;
    @Bind(R.id.order_name)
    TextView orderName;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.order_total_ll)
    LinearLayout orderTotalLl;
    @Bind(R.id.temporary_order)
    TextView temporaryOrder;
    @Bind(R.id.order_select)
    LinearLayout orderSelect;
    @Bind(R.id.order_sn_ll)
    LinearLayout order_sn_ll;

    private int pay_type = 1, php_pay_type = -1;
    private String pay_trade_no, orderid, orderSn, user_money, total_amount;
    private double price, prices;
    private double amount, totalamount, cloudOffset, handleprice, copewith;
    private int[] imgList;
    private String[] payType;
    private String url;
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
                        if (type.equals("0")) {
                            setmoeny();
                        } else {
                            Localpay();
                        }
                    } else {

                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OrderPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OrderPayActivity.this, "您还未安装支付宝客户端", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(OrderPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    //记录最后点击位置来实现单选
    private int selectedPosition = 0;
    private OrderPayAdapter adapter;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int uid;
    private UserInfoEntity userInfoEntity;
    private boolean accountPasswordExist;
    private String type, lifeid;
    private int num = 1;
    private Intent intent;
    private String tid;
    private PayOrderAgainEntity payOrderAgainEntity;
    private MoneyInfoEntity moneyInfoEntity;
    private String orderType = "1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(context, LoginActivity.class));
        } else {
            if (!caches.get("userinfo").equals("null")) {
                userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                }.getType());
                uid = userInfoEntity.data.userVo.id;
                accountPasswordExist = userInfoEntity.data.accountPasswordExist;
            }
        }
        getmoenyinfor();

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
                    amount = moneyInfoEntity.data.fundAccount;
                    totalamount = moneyInfoEntity.data.cloudAccount;
                    initView();
                }

            }
        });
    }
    private void initList() {
        final ArrayList<OrderPayEntity> arrayList = new ArrayList<>();
        for (int i = 0; i < imgList.length; i++) {
            OrderPayEntity orderPayEntity = new OrderPayEntity();
            orderPayEntity.setChacked(false);
            orderPayEntity.setImgId(imgList[i]);
            orderPayEntity.setPayType(payType[i]);
            arrayList.add(orderPayEntity);
        }
        adapter = new OrderPayAdapter(context, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                pay_type = position + 1;
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        txtTitle.setText("订单支付");
        imgBack.setOnClickListener(this);
        orderPay.setOnClickListener(this);
        subbt.setOnClickListener(this);
        addbt.setOnClickListener(this);
        temporaryOrder.setOnClickListener(this);
        intent = getIntent();
        type = intent.getStringExtra("type");//0、商城购物，1、本地生活单个购买，2、本地生活多个购买
         caches.put("orderwxtype",type);
        amount = moneyInfoEntity.data.fundAccount;
        if (type.equals("0")) {
            //商城购买
            orderType = "1";
            if(intent.getStringExtra("ordertype").equals("1")){
                OkHttpUtils.get().url(Urls.AGAINPAY+caches.get("php_token")+"/order_id/"+orderid).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                         payOrderAgainEntity = new Gson().fromJson(response,new TypeToken<PayOrderAgainEntity>(){}.getType());
                        if(payOrderAgainEntity.errorCode.equals("000")){
                        }

                    }
                });
            }
            orderid = intent.getStringExtra("order_id");
            price = Double.parseDouble(intent.getStringExtra("order_amount"));
            orderSn = intent.getStringExtra("orderSn");
            user_money = intent.getStringExtra("user_money");
            total_amount = intent.getStringExtra("total_amount");
            tvSn.setText(orderSn);
            tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(Double.parseDouble(total_amount)));

            if (amount < Double.parseDouble(total_amount)) {
                imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                payType = new String[]{"支付宝支付", "微信支付"};//, "银联支付", "微信支付", R.mipmap.wei_pay_ico
                //total_amount:商品总价，amount：个人余额
                prices = DoubleUtils.sub(Double.parseDouble(total_amount), amount);
                tvBalance.setText("￥" + StringUtils.getStringtodouble(amount));
                tvSurePay.setText("￥" + StringUtils.getStringtodouble(prices));
                price = prices;
            } else {
                imgList = new int[]{R.mipmap.pay_mark_money};
                payType = new String[]{"余额支付"};
                php_pay_type = 0;
                tvBalance.setText("￥" + StringUtils.getStringtodouble(Double.parseDouble(user_money)));
                tvSurePay.setText("￥" + StringUtils.getStringtodouble(price));
            }

        } else if(type.equals("1")){
            //本地生活购买单个商品
            orderType = "2";
            caches.put("ordertype", "1");
            orderPayNum.setVisibility(View.VISIBLE);
            orderTotalLl.setVisibility(View.VISIBLE);
            orderPay.setVisibility(View.GONE);
            temporaryOrder.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            orderSelect.setVisibility(View.GONE);
            orderName.setText("商品名称");
            tvSn.setText(intent.getStringExtra("name"));
            price = Double.parseDouble(intent.getStringExtra("price"));
            lifeid = intent.getStringExtra("lifeId");
            caches.put("orderlifeid", lifeid);
            tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(price));
            //判断积分是否足够
            cloudOffset = Double.parseDouble(intent.getStringExtra("cloudOffset"));
            double xxtotal = totalamount >= cloudOffset ? cloudOffset : totalamount;
            tvTotal.setText("￥" + StringUtils.getStringtodouble(xxtotal));
            //判断可用余额是否足够
            double pricetatal = price; //商品总价
            double shopCloudOffset = DoubleUtils.mul(cloudOffset, num); //商品总计可用积分抵扣
            double mCloudOffset = totalamount >= shopCloudOffset ? shopCloudOffset : totalamount;
            tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(pricetatal));
            tvTotal.setText("￥" + StringUtils.getStringtodouble(mCloudOffset));

            if (DoubleUtils.add(amount, mCloudOffset) >= pricetatal) {
                imgList = new int[]{R.mipmap.pay_mark_money};
                payType = new String[]{"余额支付"};
                tvBalance.setText("￥" + StringUtils.getStringtodouble(pricetatal - mCloudOffset));
                tvSurePay.setText("￥0.00");
            } else {
                handleprice = DoubleUtils.sub(pricetatal, mCloudOffset);
                copewith = DoubleUtils.sub(handleprice, amount);
                tvBalance.setText("￥" + StringUtils.getStringtodouble(amount));
                tvSurePay.setText("￥" + StringUtils.getStringtodouble(copewith));
                imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                payType = new String[]{"支付宝支付", "微信支付"};//, "银联支付", "微信支付"
            }

        }else if(type.equals("2")){
            //本地生活购买多个商品
            orderType = "2";
            caches.put("ordertype", "1");
            orderPayNum.setVisibility(View.GONE);
            orderTotalLl.setVisibility(View.VISIBLE);
            orderPay.setVisibility(View.VISIBLE);
            temporaryOrder.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            orderSelect.setVisibility(View.VISIBLE);
            orderName.setVisibility(View.GONE);
            tvSn.setVisibility(View.GONE);
            price = Double.parseDouble(intent.getStringExtra("allAmount"));
            tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(price));
            tid = intent.getStringExtra("tid");
            //判断积分是否足够
            cloudOffset = Double.parseDouble(intent.getStringExtra("allCloudOffse"));
            double xxtotal = totalamount >= cloudOffset ? cloudOffset : totalamount;
            tvTotal.setText("￥" + StringUtils.getStringtodouble(xxtotal));
            //判断可用余额是否足够
            double pricetatal = price; //商品总价
            double mCloudOffset = totalamount >= cloudOffset ? cloudOffset : totalamount;
            tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(pricetatal));
            tvTotal.setText("￥" + StringUtils.getStringtodouble(mCloudOffset));

            if (DoubleUtils.add(amount, mCloudOffset) >= pricetatal) {
                imgList = new int[]{R.mipmap.pay_mark_money};
                payType = new String[]{"余额支付"};
                tvBalance.setText("￥" + StringUtils.getStringtodouble(pricetatal - mCloudOffset));
                tvSurePay.setText("￥0.00");
            } else {
                handleprice = DoubleUtils.sub(pricetatal, mCloudOffset);
                copewith = DoubleUtils.sub(handleprice, amount);
                tvBalance.setText("￥" + StringUtils.getStringtodouble(amount));
                tvSurePay.setText("￥" + StringUtils.getStringtodouble(copewith));
                imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                payType = new String[]{"支付宝支付", "微信支付"};//, "银联支付", "微信支付"
            }
        }else if(type.equals("3")){
            orderType = "8";
            caches.put("ordertype", "1");
            total_amount = intent.getStringExtra("allAmount");
            tid = intent.getStringExtra("tid");
            orderid = tid;
            tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(Double.parseDouble(total_amount)));
            order_sn_ll.setVisibility(View.GONE);
            if (amount < Double.parseDouble(total_amount)) {
                imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                payType = new String[]{"支付宝支付", "微信支付"};//, "银联支付", "微信支付", R.mipmap.wei_pay_ico
                //total_amount:商品总价，amount：个人余额
                prices = DoubleUtils.sub(Double.parseDouble(total_amount), amount);
                tvBalance.setText("￥" + StringUtils.getStringtodouble(amount));
                tvSurePay.setText("￥" + StringUtils.getStringtodouble(prices));
                price = prices;
            } else {
                imgList = new int[]{R.mipmap.pay_mark_money};
                payType = new String[]{"余额支付"};
                php_pay_type = 0;
                tvBalance.setText("￥" + StringUtils.getStringtodouble(Double.parseDouble(total_amount)));
                tvSurePay.setText("￥" + 0.00);
            }
        }
        initList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AppManager.getInstance().killActivity(ConfirmOrderActivity.class);
        AppManager.getInstance().killActivity(OrderPayActivity.class);
        AppManager.getInstance().killActivity(ChooseDishActivity.class);
        AppManager.getInstance().killActivity(SupplyConfirmActivity.class);
        AppManager.getInstance().killActivity(SourceParticularsActivity.class);
        AppManager.getInstance().killActivity(ShoppingCartActivity.class);
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.img_back:
                AppManager.getInstance().killActivity(ConfirmOrderActivity.class);
                AppManager.getInstance().killActivity(OrderPayActivity.class);
                AppManager.getInstance().killActivity(SupplyConfirmActivity.class);
                AppManager.getInstance().killActivity(SourceParticularsActivity.class);
                AppManager.getInstance().killActivity(ShoppingCartActivity.class);
                break;
            //立即购买
            case R.id.order_pay:
                if (type.equals("0")) {
                    if (prices < 0 && pay_type == 1) {
                        php_pay_type = 4;
                        getorder();
                    } else if (prices < 0 && pay_type == 2) {
                        php_pay_type = 5;
                        getorder();
                    } else if (prices > 0 && pay_type == 1) {
                        php_pay_type = 4;
                        getorder();
                    } else if (prices > 0 && pay_type == 2) {
                        php_pay_type = 5;
                        getorder();
                    } else if (prices >= 0 && php_pay_type == 0) {
                        if (accountPasswordExist) {
                            SurePwdDialog dialog = new SurePwdDialog(context);
                            dialog.show();
                        } else {
                            PassWordDialog dialog = new PassWordDialog(context, "您还没有设置支付密码,是否前往设置");
                            dialog.show();
                        }
                    }
                } else {
                    if (Double.parseDouble(tvSurePay.getText().toString().replace("￥", "")) > 0) {
                        getorder();

                    } else {
                        if (accountPasswordExist) {
                            SurePwdDialog dialog = new SurePwdDialog(context);
                            dialog.show();
                        } else {
                            PassWordDialog dialog = new PassWordDialog(context, "您还没有设置支付密码,是否前往设置");
                            dialog.show();
                        }
                    }
                }
                break;
            //数量加减
            case R.id.subbt:
            case R.id.addbt:
                if (v.getId() == R.id.subbt) {
                    num--;
                } else if (v.getId() == R.id.addbt) {
                    num++;
                }
                if (num > 30) {
                    num--;
                } else if (num <= 0) {
                    num = 1;
                }
                tvNum.setText(num + "");
                double pricetatal = DoubleUtils.mul(price, num); //商品总价
                double shopCloudOffset = DoubleUtils.mul(cloudOffset, num); //商品总计可用积分抵扣
                double mCloudOffset = totalamount >= shopCloudOffset ? shopCloudOffset : totalamount;
                tvTotalPrice.setText("￥" + StringUtils.getStringtodouble(pricetatal));
                amount = moneyInfoEntity.data.fundAccount;
                tvTotal.setText("￥" + StringUtils.getStringtodouble(mCloudOffset));
                if (DoubleUtils.add(amount, mCloudOffset) >= pricetatal) {
                    imgList = new int[]{R.mipmap.pay_mark_money};
                    payType = new String[]{"余额支付"};
                    tvBalance.setText("￥" + StringUtils.getStringtodouble(pricetatal - mCloudOffset));
                    tvSurePay.setText("￥0.00");
                } else {
                    handleprice = DoubleUtils.sub(pricetatal, mCloudOffset);
                    copewith = DoubleUtils.sub(handleprice, amount);
                    tvBalance.setText("￥" + StringUtils.getStringtodouble(amount));
                    tvSurePay.setText("￥" + StringUtils.getStringtodouble(copewith));
                    imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                    payType = new String[]{"支付宝支付", "微信支付"};//, "银联支付", "微信支付", R.mipmap.wei_pay_ico
                }
                initList();
                break;
            //生成临时订单
            case R.id.temporary_order:
                gettemporary();
                break;
        }
    }
    private void gettemporary() {
        OkHttpUtils.post().url(Urls.LOCALINGTEMPORARY)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("orderType",orderType)
                .addParams("goods",lifeid+"-"+num)
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
                    ScanCodeTmpEntity setUpEntity = new Gson().fromJson(response, new TypeToken<ScanCodeTmpEntity>() {
                    }.getType());
                    tid = setUpEntity.data.orderId + "";
                    caches.put("tid", tid);
                    orderPay.setVisibility(View.VISIBLE);
                    temporaryOrder.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    orderSelect.setVisibility(View.VISIBLE);
                    orderPayNum.setVisibility(View.GONE);
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    /**
     * 网络请求订单号
     */
    private void getorder() {
        if (type.equals("1")||type.equals("2")) {
            //pay_type		1 = 支付宝 2 = 微信 3 = 银联  price
            OkHttpUtils.post().url(Urls.PAY).addHeader("Authorization", caches.get("access_token"))
                    .addParams("price", DoubleUtils.toDecimalString2(copewith))
                    .addParams("order_type", orderType)
                    .addParams("order_id", tid)
                    .addParams("pay_type", pay_type + "")
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
                            if (pay_type == 1) {
                                PY_info = (String) payinfor.get("pay_info");

                                Runnable payRunnable = new Runnable() {
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask aliPay = new PayTask(
                                                OrderPayActivity.this);
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
                            } else if (pay_type == 2) {
                                JSONObject params = payinfor.getJSONObject("pay_info");
                                if (Extension.isWeixinAvilible(getApplicationContext())) {
                                    if (type.equals("0")) {
                                        caches.put("orderid", orderid + "");
                                    }
                                    caches.put("tid", orderid);
                                    caches.put("ordernum", num + "");
                                    caches.put("pay_trade_no", pay_trade_no + "");
                                    caches.put("orderSn", orderSn + "");
                                    caches.put("price", price + "");
                                    caches.put("php_pay_type", php_pay_type + "");
                                    caches.put("user_money", user_money + "");
                                    caches.put("uid", uid + "");
                                    String appid = params.getString("appid");
                                    IWXAPI api = WXAPIFactory.createWXAPI(OrderPayActivity.this, appid, false);
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
                            } else if (pay_type == 3) {
                                JSONObject params = payinfor.getJSONObject("pay_info");
                                String tn = params.getString("tn");
                                //调用银联支付的支付
                                UPPayAssistEx.startPay(OrderPayActivity.this, null, null, tn, mMode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        } else {
            //pay_type		1 = 支付宝 2 = 微信 3 = 银联  price
            OkHttpUtils.post().url(Urls.PAY).addHeader("Authorization", caches.get("access_token"))
                    .addParams("order_id", orderid).
                    addParams("price", DoubleUtils.toDecimalString2(price))
                    .addParams("order_type", orderType)
                    .addParams("pay_type", pay_type + "")
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
                            if (pay_type == 1) {
                                PY_info = (String) payinfor.get("pay_info");

                                Runnable payRunnable = new Runnable() {
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask aliPay = new PayTask(
                                                OrderPayActivity.this);
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
                            } else if (pay_type == 2) {
                                JSONObject params = payinfor.getJSONObject("pay_info");
                                if (Extension.isWeixinAvilible(getApplicationContext())) {
                                    caches.put("orderid", orderid);
                                    caches.put("tid", orderid);
                                    caches.put("pay_trade_no", pay_trade_no);
                                    caches.put("orderSn", orderSn);
                                    caches.put("price", price + "");
                                    caches.put("php_pay_type", php_pay_type + "");
                                    caches.put("user_money", user_money + "");
                                    caches.put("uid", uid + "");
                                    String appid = params.getString("appid");
                                    IWXAPI api = WXAPIFactory.createWXAPI(OrderPayActivity.this, appid);
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
                            } else if (pay_type == 3) {
                                JSONObject params = payinfor.getJSONObject("pay_info");
                                String tn = params.getString("tn");
                                //调用银联支付的支付
                                UPPayAssistEx.startPay(OrderPayActivity.this, null, null, tn, mMode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast(MessgeUtil.geterr_code(code));
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                Bundle b = data.getExtras();
                String str = b.getString("pay_result");
                if (str.equals("success")) {
                    if (type.equals("0")) {
                        setmoeny();
                    } else {
                        Localpay();
                    }
                } else if (str.equals("fail")) {
                    showToast("购买失败");
                } else if (str.equals("cancel")) {
                    showToast("支付取消");
                }
                break;
        }
    }


    public class OrderPayAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<OrderPayEntity> arrayList;
        private Map<Integer, Boolean> selectedMap;//保存checkbox是否被选中的状态

        public OrderPayAdapter(Context context, ArrayList<OrderPayEntity> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            selectedMap = new HashMap<Integer, Boolean>();
            initData();
        }

        public void initData() {

            for (int i = 0; i < arrayList.size(); i++) {
                selectedMap.put(i, false);
            }
        }

        @Override
        public int getCount() {
            if (arrayList == null || arrayList.size() == 0)
                return 0;
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_order_pay, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            OrderPayEntity orderPayEntity = arrayList.get(position);
            holder.imageView.setImageResource(orderPayEntity.imgId);
            holder.textView.setText(orderPayEntity.payType);
            if (selectedPosition == 0) {
                if (position == 0) {
                    holder.img_choose_icon.setImageResource(R.mipmap.fzg);
                } else {
                    holder.img_choose_icon.setImageResource(R.mipmap.fzf);
                }
            } else {
                if (selectedPosition == position) {
                    holder.img_choose_icon.setImageResource(R.mipmap.fzg);
                } else {
                    holder.img_choose_icon.setImageResource(R.mipmap.fzf);
                }
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView, img_choose_icon;
            TextView textView;

            public ViewHolder(View itemView) {
                imageView = (ImageView) itemView.findViewById(R.id.img_type);
                textView = (TextView) itemView.findViewById(R.id.tv_type);
                img_choose_icon = (ImageView) itemView.findViewById(R.id.img_choose_icon);
            }
        }
    }

    //商城充值完成后完成订单交易
    private void setmoeny() {

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
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
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
                    if (type.equals("0")) {
                        //商城购买
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
                                isLogin(e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Map<String, Object> map = JSONFormat.jsonToMap(response);
                                int code = (int) map.get("code");
                                if (code == 0) {
                                    startActivity(new Intent(context, OrderListActivity.class).putExtra("orderpaytype", "0").putExtra("orderstaut", "2").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                } else {
                                    showToast(MessgeUtil.geterr_code(code));
                                }
                            }
                        });
                    } else {
                        String url = null;
                        if(type.equals("2")||type.equals("1")){
                            url = Urls.CREATE;
                        }else if(type.equals("3")){
                            url = Urls.MGORDERPAY;
                        }
                        //本地生活购买
                        OkHttpUtils.post().url(url)
                                .addHeader("Authorization", caches.get("access_token"))
                                .addParams("orderId", tid)
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
                                isLogin(e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Map<String, Object> map = JSONFormat.jsonToMap(response);
                                int code = (int) map.get("code");
                                if (code == 0) {
                                    showToast("购买成功");
                                    if(type.equals("2")||type.equals("1")){
                                        startActivity(new Intent(context, LocalServerOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }else if(type.equals("3")){
                                        startMyDialog();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                /**
                                                 *要执行的操作
                                                 */
                                                stopMyDialog();
                                                startActivity(new Intent(context, SurceOrderActivity.class).putExtra("orderstaut","2").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            }
                                        }, 1000);//3秒后执行Runnable中的run方法
                                    }

                                } else {
                                    showToast(MessgeUtil.geterr_code(code));
                                }
                            }
                        });
                    }
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            if (type.equals("0")) {
                tv_forget_pwd.setText(price + "");
            } else {
                tv_forget_pwd.setText(handleprice + "");
            }

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
            InputMethodManager inputmanger = (InputMethodManager) OrderPayActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void Localpay() {
        String url = null;
        if(type.equals("2")||type.equals("1")){
            url = Urls.CREATE;
        }else if(type.equals("3")){
            url = Urls.MGORDERPAY;
        }
        OkHttpUtils.post().url(url)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId", tid)
                .addParams("rechargeOrderNo", pay_trade_no + "")
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
                    showToast("购买成功");
                    if(type.equals("2")||type.equals("1")){
                        startActivity(new Intent(context, LocalServerOrderActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }else if(type.equals("3")){
                        startMyDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 *要执行的操作
                                 */
                                stopMyDialog();
                                startActivity(new Intent(context, SurceOrderActivity.class).putExtra("orderstaut","2").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }
                        }, 1000);//3秒后执行Runnable中的run方法
                    }

                } else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        System.gc();
    }
}
