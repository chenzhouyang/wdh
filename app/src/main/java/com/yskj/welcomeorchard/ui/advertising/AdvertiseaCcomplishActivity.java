package com.yskj.welcomeorchard.ui.advertising;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by YSKJ-02 on 2017/5/4.
 */

public class AdvertiseaCcomplishActivity extends BaseActivity {
    @Bind(R.id.accomplish_achieve)
    TextView accomplishAchieve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomplish);
        ButterKnife.bind(this);
        accomplishAchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().killActivity(ChooseVersionActivity.class);
                AppManager.getInstance().killActivity(VersionWebActivity.class);
                AppManager.getInstance().killActivity(SendAdvertisingActivity.class);
                AppManager.getInstance().killActivity(AdvertisingDraftActivity.class);
                AppManager.getInstance().killActivity(AdvertiseaCcomplishActivity.class);
            }
        });
    }

}
