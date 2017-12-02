package com.yskj.welcomeorchard.ui.redboxx;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Config;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.DateChooserDialog;
import com.yskj.welcomeorchard.dialog.HintLoginDialog;
import com.yskj.welcomeorchard.dialog.RedListDialog;
import com.yskj.welcomeorchard.entity.OrderPayEntity;
import com.yskj.welcomeorchard.entity.RedListEntity;
import com.yskj.welcomeorchard.entity.RedResultEntity;
import com.yskj.welcomeorchard.entity.RedSmallEntity;
import com.yskj.welcomeorchard.entity.ScanCodeTmpEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.payment.zfb.PayResult;
import com.yskj.welcomeorchard.utils.Animation;
import com.yskj.welcomeorchard.utils.AnimationUtils;
import com.yskj.welcomeorchard.utils.DoubleUtils;
import com.yskj.welcomeorchard.utils.Extension;
import com.yskj.welcomeorchard.utils.GridPasswordView;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.MethodUtils;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/21.
 */

public class RedListActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.redlist_pull_lv)
    PullToRefreshListView redlistPullLv;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.redboxx_deliver)
    RadioButton redboxxDeliver;
    @Bind(R.id.redboxx_send)
    RadioButton redboxxSend;
    @Bind(R.id.redboxx_group)
    RadioGroup redboxxGroup;
    @Bind(R.id.redlist_image)
    ImageView redlistImage;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.shouyi_view)
    LinearLayout shouyiView;
    @Bind(R.id.redboox_time)
    LinearLayout redbooxTime;
    private int cursor = 0, count = 10;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private RedListAdapter adapter;
    private RedalreadyAdapter alreadyadapter;
    private View popupWindow_view;
    private ImageView iv_excrete;
    private PopupWindow popupWindow;
    private RedResultEntity redresentity;
    private ArrayList<RedListEntity.DataBean.RedLogsBean> listBeenAll = new ArrayList<>();//已拆
    private ArrayList<RedSmallEntity.DataBean.RedLogsBean> listsmallAll = new ArrayList<>();
    private int type = 1, redid, redtype, forceOpen = 0;
    private RedSmallEntity.DataBean.RedLogsBean redLogsBean;
    private RedListEntity.DataBean.RedLogsBean redalreadyBean;
    private String status = "0";
    private TextView chaihong_amount;
    private double anounts, loseAmount;
    private UserInfoEntity userInfoEntity;
    private int removered;
    private int level, isVipOpen = 0;
    private int pay_type = 1, pay_types = 0;
    private double amount;
    private int years, months;
    private String pay_trade_no, data;
    private List<String> mYearList = new ArrayList<>();
    private List<String> mMountList = new ArrayList<>();
    private double copewith = 30.00;
    private static final int SDK_PAY_FLAG = 1;
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
                        Openvip();
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
    private String uservipid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redlist);
        ButterKnife.bind(this);
        AnimationUtils.startScale(redlistImage);
        txtTitle.setText("拆红包");
        token = caches.get("access_token");
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        years = t.year;
        months = t.month + 1;
            if (token.equals("null")) {
                HintLoginDialog dialog = new HintLoginDialog(context);
                dialog.show();
            } else {
                if (!caches.get("userinfo").equals("null")) {
                    userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                    }.getType());
                    amount = userInfoEntity.data.userVo.fundAccount;
                    level = userInfoEntity.data.userVo.level;
                }
                data = getIntent().getStringExtra("data");
                intviewlistview();
                finview();
                refervip();

        }
    }

    //查询会员特权（一键拆红包）状态
    private void refervip() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.USERVIP)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("vipKey", Config.VIPKEY)
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
                    int object = (int) map.get("data");
                    data = object + "";
                } else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }


    private void finview() {
        if (status.equals("0")) {
            getredlist();
        } else {
            getalreadyredlist();
        }
        alreadyadapter = new RedalreadyAdapter(listBeenAll);
        redlistPullLv.setAdapter(alreadyadapter);
        redboxxGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //待拆
                    case R.id.redboxx_deliver:
                        redbooxTime.setVisibility(View.GONE);
                        cursor = 0;
                        type = 1;
                        status = "0";
                        redlistImage.setVisibility(View.INVISIBLE);
                        listsmallAll.clear();
                        listBeenAll.clear();
                        alreadyadapter = new RedalreadyAdapter(listBeenAll);
                        redlistPullLv.setAdapter(alreadyadapter);
                        getredlist();
                        break;
                    //已拆
                    case R.id.redboxx_send:
                        cursor = 0;
                        type = 2;
                        redbooxTime.setVisibility(View.VISIBLE);
                        status = "1";
                        redlistImage.setVisibility(View.GONE);
                        listsmallAll.clear();
                        listBeenAll.clear();

                        getalreadyredlist();
                        break;

                }
            }
        });
        redboxxGroup.check(R.id.redboxx_deliver);
    }

    private void intviewlistview() {
        redlistImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (data.equals("0") || data.equals("1")) {
                    Calloff dialog = new Calloff(context);
                    dialog.show();
                } else {
                    showToast("您还未开通或权限已过期，请重新购买");
                }
                return false;
            }
        });
        redlistPullLv.setMode(PullToRefreshBase.Mode.BOTH);
        redlistPullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor = 0;
                if (status.equals("0")) {
                    getredlist();
                } else {
                    getalreadyredlist();
                }

                MethodUtils.stopRefresh(redlistPullLv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor = cursor + 10;
                if (status.equals("0")) {
                    getredlist();
                } else {
                    getalreadyredlist();
                }
                MethodUtils.stopRefresh(redlistPullLv);
            }
        });

    }

    //待拆红包列表
    private void getredlist() {
        OkHttpUtils.get().url(Urls.REDSMAL).addHeader("Authorization", token)
                .addParams("cursor", cursor + "")
                .addParams("count", count + "")
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
                    RedSmallEntity redSmallEntity = new Gson().fromJson(response, new TypeToken<RedSmallEntity>() {
                    }.getType());
                    cursor = redSmallEntity.data.cursor;
                    if (redSmallEntity.data.redLogs.size() != 0) {
                        if (cursor == 0) {
                            listsmallAll.clear();
                        }
                        listsmallAll.addAll(redSmallEntity.data.redLogs);
                        adapter = new RedListAdapter(listsmallAll);
                        redlistPullLv.setAdapter(adapter);
                    } else {
                        showToast("没有数据了");
                    }
                }

            }
        });
    }

    //已拆红包列表
    private void getalreadyredlist() {
        OkHttpUtils.get().url(Urls.STAYREDLIST).addHeader("Authorization", token)
                .addParams("status", status)
                .addParams("year",years+"").addParams("month",months+"")
                .addParams("cursor", cursor + "").addParams("count", count + "")
                .build().execute(new RedlistCallBalk());
    }

    @OnClick({R.id.img_back, R.id.redlist_image,
            R.id.redboox_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.redlist_image:
                if (data.equals("-1")) {
                    ExcreteDialog dialog = new ExcreteDialog(context);
                    dialog.show();
                } else {
                    isVipOpen = 1;
                    openredbag(view);
                }
                break;
            case R.id.redboox_time:
                cursor = 0;
                showExitGameAlert();
                break;
        }
    }

    /**
     * 日期选择器
     */
    private void showExitGameAlert() {

        Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
        int year=calendar.get(Calendar.YEAR);
        for (int i = 2010; i <= year; i++) {
            mYearList.add(i+"");
        }
        for (int i = 1; i <=12 ; i++) {
            mMountList.add(i+"");
        }
        DateChooserDialog dialog = new DateChooserDialog(context);
        dialog.show();
        dialog.setAddressData(mYearList,mMountList);
        dialog.setAddresskListener(new DateChooserDialog.OnAddressCListener() {
            @Override
            public void onClick(String year, String mount) {
                years = Integer.parseInt(year);
                months = Integer.parseInt(mount) ;
                time.setText(+ years +
                        "年" + months +
                        "月");
                listBeenAll.clear();
                alreadyadapter.notifyDataSetChanged();
                getalreadyredlist();
            }
        });

    }
    //一键拆红包
    private void openredbag(final View v) {
        OkHttpUtils.post().url(Urls.CRED)
                .addHeader("Authorization", token)
                .addParams("forceOpen", forceOpen + "")
                .addParams("isVipOpen", "1")
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
                final int code = (int) map.get("code");
                if (code == 0) {
                    redresentity = new Gson().fromJson(response, new TypeToken<RedResultEntity>() {
                    }.getType());
                    anounts = redresentity.data.openAmount;
                    amount = DoubleUtils.add(amount, anounts);
                    loseAmount = redresentity.data.loseAmount;
                    listBeenAll.clear();
                    alreadyadapter = new RedalreadyAdapter(listBeenAll);
                    redlistPullLv.setAdapter(alreadyadapter);
                    startOpenRedbag(v,  "1", 5, redresentity.data.openAmount);
                } else if (code == 763) {
                    new AlertDialog.Builder(context).setTitle("系统提示")
                            .setMessage(MessgeUtil.geterr_code(code))
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    forceOpen = 1;
                                    openredbag(v);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                        }
                    }).show();//在按键响应事件中显示此对话框
                } else if (code == 764) {
                    new AlertDialog.Builder(context).setTitle("系统提示")
                            .setMessage("红包已过期")
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    getredlist();
                                }
                            }).show();//在按键响应事件中显示此对话框
                } else if (code == 762) {
                    new AlertDialog.Builder(context).setTitle("系统提示")
                            .setMessage("红包不存在,或可拆通贝为零")
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    getredlist();
                                }
                            }).show();//在按键响应事件中显示此对话框
                } else {
                    new AlertDialog.Builder(context).setTitle("系统提示")
                            .setMessage(MessgeUtil.geterr_code(code))
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                }
                cursor=0;
            }
        });
    }


    public class RedlistCallBalk extends Callback<RedListEntity> {
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
        public RedListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            RedListEntity redListEntity = new Gson().fromJson(s, new TypeToken<RedListEntity>() {
            }.getType());
            return redListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(RedListEntity response, int id) {
            if (response.code == 0) {
                if (cursor == 0) {
                    listBeenAll.clear();
                }
                if (response.data.redLogs.size() != 0) {
                    listBeenAll.addAll(response.data.redLogs);
                    alreadyadapter = new RedalreadyAdapter(listBeenAll);
                    redlistPullLv.setAdapter(alreadyadapter);
                } else {
                    showToast("没有数据了");
                }

            } else {
                showToast(MessgeUtil.geterr_code(response.code));
            }
        }
    }

    //待拆红包adapter
    public class RedListAdapter extends BaseAdapter {
        private ArrayList<RedSmallEntity.DataBean.RedLogsBean> redlist;
        private HashMap<Integer, Boolean> isSelected;

        public RedListAdapter(ArrayList<RedSmallEntity.DataBean.RedLogsBean> redlist) {
            this.redlist = redlist;

            isSelected = new HashMap<>();
            initDate();
        }

        // 初始化isSelected的数据
        private void initDate() {
            isSelected.clear();
            for (int i = 0; i < redlist.size(); i++) {
                getIsSelected().put(i, true);
            }
        }

        @Override
        public int getCount() {
            if (redlist == null || redlist.size() == 0) {
                return 0;
            }
            return redlist.size();
        }

        @Override
        public Object getItem(int position) {
            return redlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void remove(int code) {
            if (redlist.size() == 1) {
                getredlist();
            }
            if (redLogsBean != null && redlist.contains(redLogsBean)) {
                redlist.remove(redLogsBean);
                if (code == 764) {
                    Iterator<RedSmallEntity.DataBean.RedLogsBean> iterator = redlist.iterator();
                    while (iterator.hasNext()) {
                        RedSmallEntity.DataBean.RedLogsBean item = iterator.next();
                        if (redLogsBean.realRedId == item.realRedId) {
                            iterator.remove();
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                cursor = redlist.size();
            }

        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewRedlistHolder holder = null;
            if (convertView == null) {
                holder = new ViewRedlistHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_redlist, null);
                holder.red_msg = (TextView) convertView.findViewById(R.id.red_msg);
                holder.red_msg_type = (TextView) convertView.findViewById(R.id.red_msg_type);
                holder.left_layout = (LinearLayout) convertView.findViewById(R.id.left_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewRedlistHolder) convertView.getTag();
            }
            final RedSmallEntity.DataBean.RedLogsBean redBean = redlist.get(position);
            if (redBean.type == 0) {
                holder.red_msg_type.setText("销售红包");
            } else if (redBean.type == 1) {
                holder.red_msg_type.setText("绩效红包 ");
            } else if (redBean.type == 2) {
                holder.red_msg_type.setText("消费红包");
            } else if (redBean.type == 3) {
                holder.red_msg_type.setText("分享红包");
            }else if(redBean.type == 4){
                holder.red_msg_type.setText("掌柜红包");
            }else if(redBean.type == 5){
                holder.red_msg_type.setText("拓展红包");
            }
            holder.left_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        if (redBean.type == 2 || redBean.type == 3) {
                            applyOpenRedbag(v,position);
                        } else {
                            if (userInfoEntity.data.userVo.gpaccount > 0) {
                                applyOpenRedbag(v,position);
                            } else {
                                new AlertDialog.Builder(context).setTitle("系统提示")
                                        .setMessage("红包值为0，不能获取收益，请购物补充红包值")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            }
                                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                                    public void onClick(DialogInterface dialog, int which) {//响应事件
                                    }
                                }).show();//在按键响应事件中显示此对话框
                            }
                        }
                    } else {
                        if (redBean.type == 2 || redBean.type == 3) {
                            startActivity(new Intent(context, RedDetialActivity.class)
                                    .putExtra("redId", redLogsBean.realRedId + "")
                                    .putExtra("openAmount", redLogsBean.amount + "")
                            );
                        }

                    }
                }
                //拆红包
                private void applyOpenRedbag(final View v, final int postion) {
                    OkHttpUtils.post().url(Urls.CRED)
                            .addHeader("Authorization", token)
                            .addParams("forceOpen", forceOpen + "")
                            .addParams("isVipOpen", "0")
                            .addParams("redCode", listsmallAll.get(position).code + "")
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
                            stopMyDialog();
                            Map<String, Object> map = JSONFormat.jsonToMap(response);
                            final int code = (int) map.get("code");
                            if (code == 0) {
                                redresentity = new Gson().fromJson(response, new TypeToken<RedResultEntity>() {
                                }.getType());
                                anounts = redresentity.data.openAmount;
                                redLogsBean = listsmallAll.get(position);
                                redid = redLogsBean.realRedId;
                                redtype = redLogsBean.type;
                                loseAmount = redresentity.data.loseAmount;
                                amount = DoubleUtils.add(amount, anounts);
                                startOpenRedbag(v, redLogsBean.realRedId + "", redLogsBean.type, redresentity.data.openAmount);
                                remove(code);
                            } else if (code == 763) {
                                new AlertDialog.Builder(context).setTitle("系统提示")
                                        .setMessage(MessgeUtil.geterr_code(code))
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                forceOpen = 1;
                                                applyOpenRedbag(v,postion);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                    public void onClick(DialogInterface dialog, int which) {//响应事件
                                    }
                                }).show();//在按键响应事件中显示此对话框
                            } else if (code == 764) {
                                new AlertDialog.Builder(context).setTitle("系统提示")
                                        .setMessage("红包已过期")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                remove(code);
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                            } else if (code == 762) {
                                new AlertDialog.Builder(context).setTitle("系统提示")
                                        .setMessage("红包不存在,或可拆通贝为零")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                remove(code);
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                            } else {
                                new AlertDialog.Builder(context).setTitle("系统提示")
                                        .setMessage(MessgeUtil.geterr_code(code))
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                            }
                        }
                    });
                }

            });
            return convertView;
        }

        public class ViewRedlistHolder {
            TextView red_msg, red_msg_type;
            LinearLayout left_layout;
        }

        public HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            this.isSelected = isSelected;
        }
    }

    //已拆红包adapter
    public class RedalreadyAdapter extends BaseAdapter {
        private ArrayList<RedListEntity.DataBean.RedLogsBean> readylist;
        private HashMap<Integer, Boolean> isSelected;

        public RedalreadyAdapter(ArrayList<RedListEntity.DataBean.RedLogsBean> readylist) {
            this.readylist = readylist;
            isSelected = new HashMap<>();
        }


        @Override
        public int getCount() {
            if (readylist == null || readylist.size() == 0) {
                return 0;
            }
            return readylist.size();
        }

        @Override
        public Object getItem(int position) {
            return readylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewRedlistHolder holder = null;
            if (convertView == null) {
                holder = new ViewRedlistHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_redlist, null);
                holder.red_msg = (TextView) convertView.findViewById(R.id.red_msg);
                holder.red_msg_type = (TextView) convertView.findViewById(R.id.red_msg_type);
                holder.left_layout = (LinearLayout) convertView.findViewById(R.id.left_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewRedlistHolder) convertView.getTag();
            }
            redalreadyBean = readylist.get(position);
            String mess = "null";
            if (redalreadyBean.redType == 0) {
                mess = "已拆销售红包" + redalreadyBean.amount;
            } else if (redalreadyBean.redType == 1) {
                mess = "已拆绩效红包" + redalreadyBean.amount;
            } else if (redalreadyBean.redType == 2) {
                mess = "已拆消费红包" + redalreadyBean.amount;
            } else if (redalreadyBean.redType == 3) {
                mess = "已拆分享红包" + redalreadyBean.amount;
            }else if(redalreadyBean.redType == 4){
                mess = "已拆掌柜红包" + redalreadyBean.amount;
            }else if(redalreadyBean.redType == 5){
                mess = "已拆拓展红包" + redalreadyBean.amount;
            }
            holder.red_msg_type.setText(mess);
            holder.left_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (readylist.get(position).redType == 2 || readylist.get(position).redType == 3) {
                        Intent intent = new Intent(context, RedDetialActivity.class);
                        intent.putExtra("redId", readylist.get(position).redId);
                        intent.putExtra("openAmount", readylist.get(position).amount + "");
                        startActivity(intent);
                    }
                }


            });
            return convertView;
        }

        public class ViewRedlistHolder {
            TextView red_msg, red_msg_type;
            LinearLayout left_layout;
        }
    }

    //拆红包动画
    private void startOpenRedbag(View v, final String id, final int type, final double amount) {
        // 获取自定义布局文件xml的视图
        popupWindow_view = getLayoutInflater().inflate(R.layout.pop_chaihongbao, null, false);
        iv_excrete = (ImageView) popupWindow_view.findViewById(R.id.iv_excrete);
        chaihong_amount = (TextView) popupWindow_view.findViewById(R.id.chaihong_amount);
        //图片开始旋转
        startRotate();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int ScrHeight = dm.heightPixels;
        int ScrWidth = dm.widthPixels;
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        if (type != 2) {
            popupWindow_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        if (loseAmount <= 0) {
                            chaihong_amount.setText("共拆取+" + StringUtils.getStringtodouble(anounts));
                        } else {
                            chaihong_amount.setText("共拆取+" + StringUtils.getStringtodouble(anounts) + "\n损失+" + StringUtils.getStringtodouble(loseAmount));
                        }
                    }
                }
            });
        }

        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.Dialog);
        Animation.setbackGround(this, 0.5f);
        popupWindow.showAtLocation(v, Gravity.LEFT, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Animation.setbackGround(RedListActivity.this, 1f);
                String mess = "null";
                if (type == 2) {
                    startActivity(new Intent(context, RedDetialActivity.class).putExtra("redId", id + "")
                            .putExtra("openAmount", StringUtils.getStringtodouble(anounts)));
                } else {
                    if (loseAmount <= 0) {
                        mess = "共拆取" + StringUtils.getStringtodouble(anounts);

                    } else {
                        mess = "共拆取+" + StringUtils.getStringtodouble(anounts) + "\n损失+" + StringUtils.getStringtodouble(loseAmount);
                    }
                    RedListDialog dialog = new RedListDialog(context, mess);
                    dialog.show();
                }

            }
        });
    }

    //旋转
    ObjectAnimator anim1;

    private void startRotate() {
        //旋转
        anim1 = ObjectAnimator.ofFloat(iv_excrete, "rotationY",
                0, 180);
        anim1.setDuration(500);
        anim1.setRepeatCount(3);
        anim1.start();
        handler_w.sendEmptyMessageDelayed(2, 1500);

    }

    Handler handler_w = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    iv_excrete.clearAnimation();
                    iv_excrete.setVisibility(View.GONE);
                    if (redtype == 2 ) {
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    } else {
                        if (loseAmount <= 0) {
                            chaihong_amount.setText("共拆取+" + StringUtils.getStringtodouble(anounts));
                        } else {
                            chaihong_amount.setText("共拆取+" + StringUtils.getStringtodouble(anounts) + "\n损失+" + StringUtils.getStringtodouble(loseAmount));
                        }

                    }
            }
        }
    };



    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class ExcreteDialog extends Dialog implements View.OnClickListener {
        private TextView btnCancle, excrete_true;
        private TextView btnSure, tv_balance;
        private Context context;
        private OrderPayAdapter adapter;
        private int[] imgList;
        private String[] payType;
        private int selectedPosition = 0;
        private NoScrollListView listView;

        private boolean isvip = false;

        public ExcreteDialog(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_excrete_all);
            initView();
        }

        private void initView() {
            caches.put("ordertype", "2");
            btnCancle = (TextView) findViewById(R.id.btn_cancle);
            btnSure = (TextView) findViewById(R.id.btn_sure);
            listView = (NoScrollListView) findViewById(R.id.listView);
            tv_balance = (TextView) findViewById(R.id.tv_balance);
            excrete_true = (TextView) findViewById(R.id.excrete_true);
            copewith = 30.00;
            if (amount >= 30) {
                imgList = new int[]{R.mipmap.pay_mark_money};
                payType = new String[]{"余额支付"};
                isvip = true;
                tv_balance.setText(StringUtils.getStringtodouble(copewith));
                excrete_true.setText(StringUtils.getStringtodouble(0.00));
            } else {
                copewith = DoubleUtils.sub(copewith, amount);
                imgList = new int[]{R.mipmap.zhifu_pay_ico, R.mipmap.wei_pay_ico};
                payType = new String[]{"支付宝支付", "微信支付"};
                isvip = false;
                tv_balance.setText(StringUtils.getStringtodouble(amount));
                excrete_true.setText(StringUtils.getStringtodouble(copewith));
            }
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
            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
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
                SetUp();
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    dismiss();
                    if (isvip) {
                        SurePwdDialog dialog = new SurePwdDialog(context);
                        dialog.show();
                    } else {
                        getexcrete();
                    }
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }
    }
private void SetUp(){
   OkHttpUtils.post().url(Urls.SETUP).addHeader("Authorization", caches.get("access_token"))
           .addParams("vipKey",Config.VIPKEY)
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
           if(code == 0){
               ScanCodeTmpEntity setUpEntity = new Gson().fromJson(response,new TypeToken<ScanCodeTmpEntity>(){}.getType());
               uservipid = setUpEntity.data.orderId+"";
               caches.put("tid",uservipid);
           }else if(code ==923){
               int userid = (int) map.get("data");
               uservipid = userid+"" ;
               caches.put("tid",uservipid);
           }else {
               showToast(MessgeUtil.geterr_code(code));
           }
       }
   });
}
    //调取支付
    private void getexcrete() {
        OkHttpUtils.post().url(Urls.PAY).addHeader("Authorization", caches.get("access_token"))
                .addParams("price", DoubleUtils.toDecimalString2(copewith))
                .addParams("order_type","3")
                .addParams("order_id",uservipid)
                .addParams("pay_type", pay_type + "")
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
                    JSONObject payinfor = (JSONObject) map.get("data");
                    final String PY_info;
                    try {
                        pay_trade_no = payinfor.getString("pay_trade_no");
                        if (pay_type == 1) {
                            PY_info = (String) payinfor.get("pay_info");
                            Runnable payRunnable = new Runnable() {
                                public void run() {
                                    // 构造PayTask 对象
                                    PayTask aliPay = new PayTask((Activity) context);
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
                            if (Extension.isWeixinAvilible(context)) {
                                caches.put("pay_trade_no", pay_trade_no + "");
                                caches.put("level", level + "");
                                String appid = params.getString("appid");
                                IWXAPI api = WXAPIFactory.createWXAPI(context, appid, false);
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
                                Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    //付款成功调取接口
    private void Openvip() {
        OkHttpUtils.post().url(Urls.OPENVIP).addHeader("Authorization", caches.get("access_token"))
                .addParams("vipKey", Config.VIPKEY)
                .addParams("userVipId",uservipid)
                .addParams("tradeNo", pay_trade_no)
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
                    data = "1";
                    showToast("开通成功，您可以享受vip服务了");
                } else if(code == 925){
                    data = "1";
                    showToast("开通成功，您可以享受vip服务了");
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    //用通贝支付
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
                    startMyDialog();
                    OkHttpUtils.post().url(Urls.OPENVIP).addHeader("Authorization", caches.get("access_token"))
                            .addParams("vipKey", Config.VIPKEY)
                            .addParams("userVipId",uservipid)
                            .addParams("accountPassword", pwdView.getPassWord())
                            .build().execute(new StringCallback() {
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
                                data = "1";
                                showToast("开通成功，您可以享受vip服务了");
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
            tv_forget_pwd.setText(StringUtils.getStringtodouble(copewith));

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
            InputMethodManager inputmanger = (InputMethodManager) RedListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class Calloff extends Dialog implements View.OnClickListener {
        private Context context;
        private ImageView notice_select;
        private TextView btn_cancle;

        public Calloff(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_call_all);
            initView();
        }

        private void initView() {
            notice_select = (ImageView) findViewById(R.id.notice_select);
            btn_cancle = (TextView) findViewById(R.id.btn_cancle);
            if (!data.equals("0")) {
                notice_select.setImageResource(R.mipmap.red_set_on);
            }
            notice_select.setOnClickListener(this);
            btn_cancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_cancle:
                    dismiss();
                    break;
                case R.id.notice_select:
                    if (data.equals("1")) {
                        //关闭自动续费
                        closevip();
                    } else {
                        //开启自动续费
                        openvip();
                    }
                    break;
            }
        }

        //关闭自动续费
        private void closevip() {
            startMyDialog();
            OkHttpUtils.post().url(Urls.CLOSEVIP)
                    .addHeader("Authorization", caches.get("access_token"))
                    .addParams("vipKey", Config.VIPKEY)
                    .build().execute(new StringCallback() {
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
                        data = "0";
                        notice_select.setImageResource(R.mipmap.red_set_off);
                        showToast("自动续费关闭成功，将会影响您下个月的使用，请及时开启");
                    } else {
                        showToast(MessgeUtil.geterr_code(code));
                    }
                }
            });

        }

        //开启自动续费
        private void openvip() {
            OkHttpUtils.post().url(Urls.UPDATE)
                    .addHeader("Authorization", caches.get("access_token"))
                    .addParams("vipKey", Config.VIPKEY)
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
                        data = "1";
                        notice_select.setImageResource(R.mipmap.red_set_on);
                        showToast("自动续费功能已开启");
                    } else {
                        showToast(MessgeUtil.geterr_code(code));
                    }

                }
            });
        }
    }
}
