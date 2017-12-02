package com.yskj.welcomeorchard.ui.performance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.MoneyInfoEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.ui.income.IncomeActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.Level;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/1/15.
 * 业务管理
 */

public class PerformanceActivity extends BaseActivity {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.mlin_portrait)
    CircleImageView mlinPortrait;
    @Bind(R.id.mlin_title_appellation)
    TextView mlinTitleAppellation;
    @Bind(R.id.mlin_title_nickname)
    TextView mlinTitleNickname;
    @Bind(R.id.perfor_invite_LL)
    LinearLayout perforInviteLL;
    @Bind(R.id.perfor_income_ll)
    LinearLayout perforIncomeLl;
    @Bind(R.id.perfor_nickname)
    TextView perforNickname;
    @Bind(R.id.perfor_appellation)
    TextView perforAppellation;
    @Bind(R.id.perfor_phone)
    TextView perforPhone;
    @Bind(R.id.perform_Mmoney)
    TextView performMmoney;
    @Bind(R.id.perfor_pfit)
    TextView perforPfit;
    @Bind(R.id.performa_image)
    CircleImageView performaImage;
    @Bind(R.id.performance_ll)
    LinearLayout performanceLl;
    private Bitmap qrCodeBitmap;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    private String avatar, avatarimage;
    private MoneyInfoEntity moneyInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        ButterKnife.bind(this);
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        avatarimage = userInfoEntity.data.userVo.avatar;//个人头像
        txtTitle.setText("分享查询");
        mlinTitleAppellation.setTextColor(this.getResources().getColor(R.color.black));
        intiview();
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
                    performMmoney.setText(StringUtils.formatCount(moneyInfoEntity.data.maccount));
                    perforPfit.setText(StringUtils.formatCount(moneyInfoEntity.data.totalMAccount));
                }

            }
        });
    }
    private void intiview() {
        //我的培训师信息
        UserInfoEntity.DataBean.SpreaderVoBean spreaderVo = userInfoEntity.data.spreaderVo;
        if (spreaderVo != null && spreaderVo.avatar != null) {
            avatar = spreaderVo.avatar;//培训师头像
            if (!avatar.equals("0")) {
                GlideImage.loadImage(context,performaImage,avatar,R.mipmap.default_image);
            }
            perforNickname.setText(spreaderVo.nickname);
            perforAppellation.setText(Level.geterr_code(spreaderVo.level));
            perforPhone.setText(spreaderVo.mobile + "");
        }else {
            performanceLl.setVisibility(View.GONE);
        }
        mlinTitleNickname.setVisibility(View.GONE);
        mlinTitleNickname.setText(Level.geterr_code(userInfoEntity.data.userVo.level));
        mlinTitleAppellation.setText(userInfoEntity.data.userVo.nickName);
        if (!avatarimage.equals("0")) {
            GlideImage.loadImage(context,mlinPortrait,avatarimage,R.mipmap.default_image);
        }
        perforIncomeLl.setVisibility(View.GONE);
    }

    @OnClick({R.id.perfor_invite_LL, R.id.perfor_income_ll
            , R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.perfor_invite_LL:
                startActivity(new Intent(context, IncomeActivity.class).putExtra("income", "0"));
                break;
            case R.id.perfor_income_ll:
                startActivity(new Intent(context, IncomeActivity.class).putExtra("income", "1"));
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

}
