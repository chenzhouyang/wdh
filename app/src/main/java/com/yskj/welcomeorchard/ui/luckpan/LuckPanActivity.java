package com.yskj.welcomeorchard.ui.luckpan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Ips;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.LuckPanDialog;
import com.yskj.welcomeorchard.entity.PrizeListEntity;
import com.yskj.welcomeorchard.entity.PrizeResultEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.Util;
import com.yskj.welcomeorchard.view.LuckPanLayout;
import com.yskj.welcomeorchard.view.RotatePan;
import com.yskj.welcomeorchard.widget.NoticeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/5/16.
 */

public class LuckPanActivity extends BaseActivity implements RotatePan.AnimationEndListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private NoticeView noticeView;
    private String[] strs = new String[6];
    private List<Bitmap> bitmaps = new ArrayList<>();
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ArrayList<PrizeListEntity.ListBean> prizeList = new ArrayList<>();
    private int pos;
    private PrizeResultEntity.ListBean prizeResult;
    private UserInfoEntity userInfoEntity;
    private String deductCloudAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_luckpan);
        ButterKnife.bind(this);
        txtTitle.setText("积分大转盘");
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {}.getType());
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.startLuckLight();
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setAnimationEndListener(this);
        goBtn = (ImageView) findViewById(R.id.go);
        noticeView = (NoticeView) findViewById(R.id.noticeView);
        noticeView.getNotice();
        luckPanLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;

                int MinValue = Math.min(width, height);
                MinValue -= Util.dip2px(LuckPanActivity.this, 30) * 2;
                backHeight = MinValue / 2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = MinValue;
                lp.height = MinValue;

                luckPanLayout.setLayoutParams(lp);

                MinValue -= Util.dip2px(LuckPanActivity.this, 28) * 2;
                lp = (RelativeLayout.LayoutParams) rotatePan.getLayoutParams();
                lp.height = MinValue;
                lp.width = MinValue;
                rotatePan.setLayoutParams(lp);
                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight() / 2);
                goBtn.setLayoutParams(lp);

                getWindow().getDecorView().requestLayout();
            }
        });

        initData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                .setMessage("确定要退出吗？")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            public void onClick(DialogInterface dialog, int which) {//响应事件
                dialog.dismiss();
            }
        }).show();//在按键响应事件中显示此对话框
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.PRIZELIST).build().execute(new prizeListCallBack());
    }

    private class prizeResultCallBack extends Callback<PrizeResultEntity> {

        @Override
        public PrizeResultEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            PrizeResultEntity prizeResultEntity = new Gson().fromJson(s, new TypeToken<PrizeResultEntity>() {
            }.getType());
            return prizeResultEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(PrizeResultEntity response, int id) {
            if (response.errorCode.equals("000")) {

                for (int i = 0; i < prizeList.size(); i++) {
                    prizeResult = response.list;
                    if (prizeResult.id.equals(prizeList.get(i).id)) {
                        pos = i;
                        rotatePan.startRotate(pos);
                        luckPanLayout.setDelayTime(100);
                        goBtn.setEnabled(false);
                    }
                }
            }else {
                showdialog("积分不足，请获取积分之后再试！");
            }
        }
    }

    private class prizeListCallBack extends Callback<PrizeListEntity> {

        @Override
        public PrizeListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            PrizeListEntity prizeListEntity = new Gson().fromJson(s, new TypeToken<PrizeListEntity>() {
            }.getType());
            return prizeListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(PrizeListEntity response, int id) {

            if (response.errorCode.equals("000")) {
                prizeList.clear();
                prizeList.addAll(response.list);
                for (int i = 0; i < prizeList.size(); i++) {
                    PrizeListEntity.ListBean listBean = prizeList.get(i);
                    deductCloudAccount = listBean.points;
                    if (TextUtils.isEmpty(deductCloudAccount)){
                        deductCloudAccount =  "0";
                    }
                    if (listBean.name.length() > 6) {
                        strs[i] = listBean.name.substring(0, 5);
                    } else {
                        strs[i] = listBean.name;
                    }
                    Glide.with(context).load(Ips.PHPURL + listBean.code).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                            bitmaps.add(bitmap);
                            if (bitmaps.size() == 6) {
                                rotatePan.setStr(strs);
                                rotatePan.setImages(bitmaps);
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        noticeView.onStart();
        if (prizeResult != null && prizeResult.type.equals("2")) {
            LuckPanDialog dialog = new LuckPanDialog(context, prizeResult, prizeList.get(pos).name);
             dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        noticeView.onStop();
    }

    public void rotation(View view) {
        if (!TextUtils.isEmpty(caches.get("php_token")) && !caches.get("php_token").equals("null")) {
            Logger.d(deductCloudAccount);
            if(userInfoEntity.data.userVo.cloudAccount>=Double.parseDouble(deductCloudAccount)){
                OkHttpUtils.get().url(Urls.PRIZERESULT + caches.get("php_token")).build().execute(new prizeResultCallBack());
            }else {
                showdialog("积分不足，请获取积分之后再试！");
            }
        } else {
            startActivity(new Intent(context, LoginActivity.class));
        }
    }

    @Override
    public void endAnimation(int position) {
        goBtn.setEnabled(true);
        luckPanLayout.setDelayTime(100);
        LuckPanDialog dialog = new LuckPanDialog(context, prizeResult, prizeList.get(pos).name);
        dialog.show();
    }
}
