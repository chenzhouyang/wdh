package com.yskj.welcomeorchard.ui.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.dialog.QuitDialog;
import com.yskj.welcomeorchard.ui.address.AddressActivity;
import com.yskj.welcomeorchard.ui.approve.ApproveActivity;
import com.yskj.welcomeorchard.ui.verify.VerifyActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YSKJ-02 on 2017/1/13.
 * 设置界面
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.setting_release)
    TextView settingRelease;
    @Bind(R.id.setting_about_ll)
    LinearLayout settingAboutLl;
    @Bind(R.id.setting_login_ll)
    LinearLayout settingLoginLl;
    @Bind(R.id.setting_pay_ll)
    LinearLayout settingPayLl;
    @Bind(R.id.setting_location_ll)
    LinearLayout settingLocationLl;
    @Bind(R.id.setting_autonym_ll)
    LinearLayout settingAutonymLl;
    @Bind(R.id.setting_cancel)
    LinearLayout settingCancel;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        txtTitle.setText("设置");
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            settingRelease.setText("版本号"+packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.img_back, R.id.setting_about_ll, R.id.setting_login_ll, R.id.setting_pay_ll, R.id.setting_location_ll, R.id.setting_autonym_ll, R.id.setting_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.setting_about_ll:
                break;
            case R.id.setting_login_ll:
                startActivity(new Intent(context, VerifyActivity.class).putExtra("type","2"));
                break;
            case R.id.setting_pay_ll:
                caches.put("type","setting");
                startActivity(new Intent(context, VerifyActivity.class)
                        .putExtra("type","6").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.setting_location_ll:
                startActivity(new Intent(context, AddressActivity.class));
                break;
            case R.id.setting_autonym_ll:
                startActivity(new Intent(context, ApproveActivity.class).putExtra("type","0"));
                break;
            case R.id.setting_cancel:
                QuitDialog dialog = new QuitDialog(context,"是否注销？");
                dialog.show();
                break;
        }
    }
}
