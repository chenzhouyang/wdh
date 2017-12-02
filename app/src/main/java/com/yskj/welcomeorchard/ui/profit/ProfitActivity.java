package com.yskj.welcomeorchard.ui.profit;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.ProfitExplainDialog;
import com.yskj.welcomeorchard.entity.ProfitEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.NumberFormat;
import com.yskj.welcomeorchard.widget.WaveProgressView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/1/13
 * 分红界面
 */

public class ProfitActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.wave_chuangye)
    WaveProgressView chuangYeWave;
    @Bind(R.id.wave_legou)
    WaveProgressView leGouWave;
    @Bind(R.id.wave_manager)
    WaveProgressView managerWave;
    @Bind(R.id.tv_leGou_count)
    TextView tvLeGouCount;
    @Bind(R.id.tv_changYe_count)
    TextView tvChangYeCount;
    @Bind(R.id.tv_manager_count)
    TextView tvManagerCount;

    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ProfitEntity profitEntity;

    public static final String LEGOU = "\u3000\u3000   一天内累计消费1万，你将进入乐购宝进行每月分红，综合收入等于购买值时停止分红。";
    public static final String CHUANGYE = "\u3000\u3000 一星期内累计消费产生分润3万，你将进入创业宝进行每月分红，分红的总金额为不限，终身享受分红。";
    public static final String MANAGER = "\u3000\u3000  成为渠道商，并拥有六个渠道商部门你将进入董事宝进行每季度分红，分红的总金额不限，享受终身世袭制分红。";

    private ProfitExplainDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        ButterKnife.bind(this);
        initView();

        setProfit();
        initWave();
    }

    private void initView() {
        txtTitle.setText("分红");
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        imageRight.setVisibility(View.INVISIBLE);
    }

    private void initWave() {
        chuangYeWave.setCurrent(50, "创");
        chuangYeWave.setWaveColor("#e95d48");
        chuangYeWave.setText("#e64f4d", 70);

        leGouWave.setCurrent(50, "乐");
        leGouWave.setWaveColor("#e95d48");
        leGouWave.setText("#e64f4d", 70);

        managerWave.setCurrent(50, "董");
        managerWave.setWaveColor("#e95d48");
        managerWave.setText("#e64f4d", 70);
    }

    private void setProfit() {
        OkHttpUtils.get().url(Urls.REDPRIZE)
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
                profitEntity = new Gson().fromJson(response, new TypeToken<ProfitEntity>() {
                }.getType());
                if (profitEntity.code == 0) {
                    tvLeGouCount.setText(Html.fromHtml("共计金额：<font color=\"#CC0000\">￥" + String.format("%.2f", NumberFormat.convertToDouble(profitEntity.data.tesco.totalAmount, 0.00)) + "</font>"));
                    tvChangYeCount.setText(Html.fromHtml("共计金额：<font color=\"#CC0000\">￥" + String.format("%.2f", NumberFormat.convertToDouble(profitEntity.data.business.totalAmount, 0.00)) + "</font>"));
                    tvManagerCount.setText(Html.fromHtml("共计金额：<font color=\"#CC0000\">￥" + String.format("%.2f", NumberFormat.convertToDouble(profitEntity.data.director.totalAmount, 0.00)) + "</font>"));
                } else {
                    showToast(MessgeUtil.geterr_code(profitEntity.code));
                }
            }
        });

    }


    @OnClick({R.id.img_back, R.id.image_right, R.id.tv_leGou, R.id.tv_changYe, R.id.tv_manager})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                AppManager.getInstance().killActivity(ProfitActivity.class);
                break;
            case R.id.image_right:
                break;
            case R.id.tv_leGou:
                dialog = new ProfitExplainDialog(ProfitActivity.this, "乐购宝", LEGOU);
                dialog.show();
                break;
            case R.id.tv_changYe:
                dialog = new ProfitExplainDialog(ProfitActivity.this, "创业宝", CHUANGYE);
                dialog.show();
                break;
            case R.id.tv_manager:
                dialog = new ProfitExplainDialog(ProfitActivity.this, "董事宝", MANAGER);
                dialog.show();
                break;
        }
    }
}
