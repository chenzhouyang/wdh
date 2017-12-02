package com.yskj.welcomeorchard.ui.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.CallUpDialog;
import com.yskj.welcomeorchard.entity.InfoEntity;
import com.yskj.welcomeorchard.entity.MoneyInfoEntity;
import com.yskj.welcomeorchard.entity.PrizeListEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.accounts.AccountsActivity;
import com.yskj.welcomeorchard.ui.deposit.BankListActivity;
import com.yskj.welcomeorchard.ui.deposit.DepositActivity;
import com.yskj.welcomeorchard.ui.luckpan.LuckPanActivity;
import com.yskj.welcomeorchard.ui.merchant.MerChantActivity;
import com.yskj.welcomeorchard.ui.payment.PayMentActivity;
import com.yskj.welcomeorchard.ui.performance.PerformanceActivity;
import com.yskj.welcomeorchard.ui.profit.ProfitActivity;
import com.yskj.welcomeorchard.ui.push.PushMessageActivity;
import com.yskj.welcomeorchard.ui.qrcode.QrCodeActivity;
import com.yskj.welcomeorchard.ui.redboxx.RedListActivity;
import com.yskj.welcomeorchard.ui.revenue.RevenueActivity;
import com.yskj.welcomeorchard.ui.setting.SettingActivity;
import com.yskj.welcomeorchard.ui.supply.SurceOrderActivity;
import com.yskj.welcomeorchard.utils.CropImageActivity;
import com.yskj.welcomeorchard.utils.DialogImageActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MerChant;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/1/13.
 * 我的
 */

public class MineActivity extends BaseActivity implements MerChant.Merchantonlint{
    @Bind(R.id.mlin_message)
    ImageView mlinMessage;
    @Bind(R.id.mlin_customer)
    ImageView mlinCustomer;
    @Bind(R.id.mlin_setting)
    ImageView mlinSetting;
    @Bind(R.id.mlin_portrait)
    CircleImageView mlinPortrait;
    @Bind(R.id.mlin_title_appellation)
    TextView mlinTitleAppellation;
    @Bind(R.id.mlin_title_nickname)
    TextView mlinTitleNickname;
    @Bind(R.id.mlin_capital)
    TextView mlinCapital;
    @Bind(R.id.mlin_accounts)
    TextView mlinAccounts;
    @Bind(R.id.mlin_deposit_ll)
    LinearLayout mlinDepositLl;
    @Bind(R.id.mlin_GP_tv)
    TextView mlinGPTv;
    @Bind(R.id.mlin_GP_ll)
    LinearLayout mlinGPLl;
    @Bind(R.id.mlin_earnings_tv)
    TextView mlinEarningsTv;
    @Bind(R.id.mlin_accounts_ll)
    LinearLayout mlinAccountsLl;
    @Bind(R.id.mlin_bank_ll)
    LinearLayout mlinBankLl;
    @Bind(R.id.mlin_integration_tv)
    TextView mlinIntegrationTv;
    @Bind(R.id.mlin_integration_ll)
    LinearLayout mlinIntegrationLl;
    @Bind(R.id.mlin_order_ll)
    LinearLayout mlinOrderLl;
    @Bind(R.id.mlin_payment_ll)
    LinearLayout mlinPaymentLl;
    @Bind(R.id.mlin_shipments_ll)
    LinearLayout mlinShipmentsLl;
    @Bind(R.id.mlin_takegoods_ll)
    LinearLayout mlinTakegoodsLl;
    @Bind(R.id.mlin_stoptakegoods_ll)
    LinearLayout mlinStoptakegoodsLl;
    @Bind(R.id.mlin_performance_ll)
    LinearLayout mlinPerformanceLl;
    @Bind(R.id.mlin_redboxx_ll)
    LinearLayout mlinRedboxxLl;
    @Bind(R.id.mlin_profit_ll)
    LinearLayout mlinProfitLl;
    @Bind(R.id.mlin_check_ll)
    LinearLayout mlinCheckLl;
    @Bind(R.id.mlin_indiana_ll)
    LinearLayout mlinIndianaLl;
    @Bind(R.id.mlin_turnplate_ll)
    LinearLayout mlinTurnplateLl;
    @Bind(R.id.mlin_qr)
    ImageView mlinQr;
    @Bind(R.id.order_payment)
    ImageView orderPayment;
    @Bind(R.id.order_payment_deliver)
    ImageView orderPaymentDeliver;
    @Bind(R.id.order_payment_take)
    ImageView orderPaymentTake;
    @Bind(R.id.order_payment_already)
    ImageView orderPaymentAlready;
    @Bind(R.id.card_count)
    TextView cardCount;
    @Bind(R.id.mnin_attestation)
    LinearLayout mninAttestation;
    @Bind(R.id.mlin_payment)
    TextView mlinPayment;
    @Bind(R.id.mine_merchant)
    ImageView MineMerchant;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    private PopupWindow popupWindow;
    private String ImageName;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private MoneyInfoEntity moneyInfoEntity;
    private String avatar;
    private File picture;
    private String fileId;
    private String shopStatus = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlin);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setview();
        getmoenyinfor();
    }
    private void setview(){
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        if(userInfoEntity!=null){
            avatar = userInfoEntity.data.userVo.avatar;
            GlideImage.loadImage(context,mlinPortrait,avatar,R.mipmap.default_image);
            shopStatus = userInfoEntity.data.shopStatus + "";
            if(shopStatus.equals("1")){
                MineMerchant.setImageResource(R.mipmap.my_here_on_ok);
            }
            mlinTitleNickname.setVisibility(View.INVISIBLE);
            cardCount.setText((int) userInfoEntity.data.cardCount + "");
            mlinTitleAppellation.setText(userInfoEntity.data.userVo.nickName);
        }

    }

    @OnClick({R.id.mlin_message, R.id.mlin_customer,
            R.id.mlin_setting, R.id.mlin_accounts_ll,
            R.id.mlin_bank_ll, R.id.mlin_performance_ll,
            R.id.mlin_redboxx_ll, R.id.mlin_profit_ll,
            R.id.mlin_accounts, R.id.mlin_qr, R.id.order_payment,
            R.id.order_payment_deliver, R.id.order_payment_take,
            R.id.order_payment_already, R.id.mlin_check_ll,
            R.id.mlin_portrait, R.id.mlin_title_appellation,
            R.id.mnin_attestation, R.id.mlin_payment,R.id.mlin_turnplate_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mlin_message:
                startActivity(new Intent(context, PushMessageActivity.class));
                break;
            case R.id.mlin_customer:
                //客服
                CallUpDialog dialog = new CallUpDialog(context, "是否拨打客服电话");
                dialog.show();
                break;
            case R.id.mlin_setting:
                //设置
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.mlin_accounts_ll:
                //转账
                startActivity(new Intent(context, AccountsActivity.class));
                break;
            case R.id.mlin_bank_ll:
                //银行卡

                startActivity(new Intent(context, BankListActivity.class));

                break;
            case R.id.mlin_performance_ll:
                //业务管理
                startActivity(new Intent(context, PerformanceActivity.class));
                break;
            case R.id.mlin_redboxx_ll:
                //红包列表
                startActivity(new Intent(context, RedListActivity.class).putExtra("data", "-2"));
                break;
            case R.id.mlin_profit_ll:
                //分红宝
                Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                t.setToNow(); // 取得系统时间。
                int date = t.monthDay;
                if(date == 1){
                    startActivity(new Intent(context, ProfitActivity.class));
                }else {
                    showToast("正在筹备中");
                }

                break;
            case R.id.mlin_accounts:
                //提现
                if (userInfoEntity!=null&&userInfoEntity.data.cardCount == 0) {
                    new AlertDialog.Builder(MineActivity.this).setTitle("系统提示")//设置对话框标题
                            .setMessage("您还没有添加银行卡")//设置显示的内容
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {//添加确定按钮
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                } else {
                    startActivity(new Intent(context, DepositActivity.class));
                }

                break;
            case R.id.mlin_qr:
                //我的二维码
                startActivity(new Intent(context, QrCodeActivity.class));
                break;
            case R.id.order_payment:
                //订单待付款
                startActivity(new Intent(context, SurceOrderActivity.class).putExtra("orderstaut", "1"));
                break;
            case R.id.order_payment_deliver:
                //待发货
                startActivity(new Intent(context, SurceOrderActivity.class).putExtra("orderstaut", "2"));
                break;
            case R.id.order_payment_take:
                //待收货
                startActivity(new Intent(context, SurceOrderActivity.class).putExtra("orderstaut", "3"));
                break;
            case R.id.order_payment_already:
                //已收货
                startActivity(new Intent(context, SurceOrderActivity.class).putExtra("orderstaut", "4"));
                break;
            case R.id.mlin_check_ll:
                //账单
                startActivity(new Intent(context, RevenueActivity.class));
                break;
            case R.id.mlin_portrait:
                //更换头像
                dialogShow(view);
                break;
            case R.id.mlin_title_appellation:
                //更改昵称
                NickNameDialog dialog1 = new NickNameDialog(context);
                dialog1.show();
                break;

            case R.id.mnin_attestation:
                //商家入住
                //商家
                judge(shopStatus);
                break;
            case R.id.mlin_payment:
                //付款
                startActivity(new Intent(context, PayMentActivity.class));
                break;
            case R.id.mlin_turnplate_ll:
                //积分大转盘
                OkHttpUtils.get().url(Urls.PRIZELIST).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrizeListEntity prizeListEntity = new Gson().fromJson(response,new TypeToken<PrizeListEntity>(){}.getType());
                        if(prizeListEntity.errorCode.equals("000")){
                            if(prizeListEntity.list.size()==6){
                                startActivity(new Intent(context, LuckPanActivity.class));
                            }else {
                                showToast("敬请期待...");
                            }
                        }else {
                            showToast("敬请期待...");
                        }

                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data == null) {
                picture = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);
                Uri uri = Uri.fromFile(picture);
                startImageZoom(uri);
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                }
            }
            setimagehead(picture);
        } else if (requestCode == 2) {
            if (data == null) {
                return;
            }
            startCropImage(data.getStringExtra("path"));

        } else if (requestCode == 3) {
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            Bitmap bm = extras.getParcelable("data");
            mlinPortrait.setImageBitmap(bm);
            saveBitmap(bm);
            File f = new File(Environment.getExternalStorageDirectory() + "", "share.png");
            if (f.exists()) {
                f.delete();
            }
            if (bm == null) {
                return;
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setimagehead(f);
        }
    }

    /**
     * 选择图片来源
     */
    private void dialogShow(View view) {
        getPhotoPopupWindow(R.layout.popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.anim_popup_dir);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
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
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /***
     * 图片PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    backgroundAlpha(1f);
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        Intent intent = new Intent(this, DialogImageActivity.class);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory()
                , ImageName)));
        startActivityForResult(intent, 1);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }


    /**
     * 相册裁剪
     *
     * @param photoPath
     */
    public void startCropImage(String photoPath) {
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.putExtra(CropImageActivity.IMAGE_PATH, photoPath);
        intent.putExtra(CropImageActivity.SCALE, true);
        intent.putExtra(CropImageActivity.ASPECT_X, 3);
        intent.putExtra(CropImageActivity.ASPECT_Y, 2);
        startActivityForResult(intent, 3);
    }

    /**
     * 拍照裁剪
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 210);
        intent.putExtra("outputY", 210);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    //压缩保存图片
    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/lsk/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "share.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 请求个人信息
     */
    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                caches.put("userinfo", response);
                userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                }.getType());
                avatar = userInfoEntity.data.userVo.avatar;
                GlideImage.loadImage(context,mlinPortrait,avatar,R.mipmap.default_image);
                shopStatus = userInfoEntity.data.shopStatus + "";
                if(shopStatus.equals("1")){
                    MineMerchant.setImageResource(R.mipmap.my_here_on_ok);
                }
                mlinTitleAppellation.setText(userInfoEntity.data.userVo.nickName);
                mlinTitleNickname.setVisibility(View.INVISIBLE);
                cardCount.setText((int) userInfoEntity.data.cardCount + "");
                mlinTitleNickname.setText(userInfoEntity.data.userVo.level + "");
            }
        });
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
                    mlinCapital.setText(StringUtils.getStringtodouble(moneyInfoEntity.data.fundAccount));
                    mlinIntegrationTv.setText(StringUtils.getStringtodouble(moneyInfoEntity.data.cloudAccount));
                    mlinGPTv.setText(StringUtils.getStringtodouble(moneyInfoEntity.data.gpaccount));
                    mlinEarningsTv.setText(StringUtils.getStringtodouble(moneyInfoEntity.data.totalAmount));
                }

            }
        });
    }

    /**
     *
     * @param updateimage
     */

    //上传头像
    private void setimagehead(File updateimage) {
        startMyDialog();
        OkHttpUtils.post().url(Urls.UPDATRSVTEA)
                .addParams("userId", userInfoEntity.data.userVo.id + "")
                .addFile("files", System.currentTimeMillis() + ".jpg", updateimage).build().execute(new StringCallback() {
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
                InfoEntity infoEntity = new Gson().fromJson(response, new TypeToken<InfoEntity>() {
                }.getType());
                if (infoEntity.code == 0) {
                    showToast("上传成功");
                    fileId = infoEntity.data.get(0);
                    setalter();
                } else {
                    showToast(MessgeUtil.geterr_code(infoEntity.code));
                }
            }
        });

    }

    //请求更新头像
    private void setalter() {
        OkHttpUtils.post().url(Urls.UPDATEPORFI)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("fileId", fileId)
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
                    getuserinfo();
                } else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });
    }

    @Override
    public void judge(String stutas) {
        if (stutas.equals("0")) {
            new AlertDialog.Builder(MineActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("待审批")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("1")) {
            try {
                SharedPreferences share2    = getSharedPreferences("mobile", 0);
                String mobile  = share2.getString("mobile", "null");
                SharedPreferences share    = getSharedPreferences("password", 0);
                String password  = share.getString("password", "null");
                ComponentName componentName = new ComponentName("com.yskj.wdh", "com.yskj.wdh.start.SplashActivity");
                Intent intent = new Intent();
                //  Intent intent = new Intent("chroya.foo");
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("password", password);
                intent.putExtras(bundle);
                intent.setComponent(componentName);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                new AlertDialog.Builder(MineActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("请下载商家版app")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                Intent viewIntent = new
                                Intent("android.intent.action.VIEW", Uri.parse(Ips.MALL));
                                startActivity(viewIntent);
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框

            }

        } else if (stutas.equals("2")) {
            new AlertDialog.Builder(MineActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("审批失败，请修改信息")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            Intent intent = new Intent(MineActivity.this, MerChantActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("where", 2);
                            startActivityForResult(intent, 102);
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("3")) {
            new AlertDialog.Builder(MineActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("店家已冻结，请联系管理员")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        } else if (stutas.equals("-1")) {
            new AlertDialog.Builder(MineActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("您还未提交申请信息，是否提交信息")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            Intent intent = new Intent(MineActivity.this, MerChantActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("where", 3);
                            startActivityForResult(intent, 101);
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    dialog.dismiss();
                }
            }).show();//在按键响应事件中显示此对话框
        }
    }

    public class NickNameDialog extends Dialog implements View.OnClickListener {
        private Button btnCancle;
        private Button btnSure;
        private Context context;
        private String diamessage;
        private EditText nickname;

        public NickNameDialog(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_nick_name);

            initView();
        }

        private void initView() {
            btnCancle = (Button) findViewById(R.id.btn_cancle);
            btnSure = (Button) findViewById(R.id.btn_sure);
            nickname = (EditText) findViewById(R.id.message_et);
            nickname.setHint(userInfoEntity.data.userVo.nickName);
            btnSure.setOnClickListener(this);
            btnCancle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sure:
                    if (nickname.length() != 0) {
                        OkHttpUtils.post().url(Urls.UPDATENICKNAME).addHeader("Authorization", caches.get("access_token"))
                                .addParams("nickName", nickname.getText().toString()).build().execute(new StringCallback() {
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
                                    dismiss();
                                    showToast("昵称修改成功");
                                    getuserinfo();
                                } else {
                                    showToast(MessgeUtil.geterr_code(code));
                                }
                            }
                        });
                    } else {
                        showToast("请填写昵称");
                    }
                    break;
                case R.id.btn_cancle:
                    dismiss();
                    break;
            }
        }
    }


}
