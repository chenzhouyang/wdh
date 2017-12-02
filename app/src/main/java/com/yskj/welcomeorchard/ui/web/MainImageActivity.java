package com.yskj.welcomeorchard.ui.web;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class MainImageActivity extends BaseActivity {
    @Bind(R.id.mainimage_web)
    BridgeWebView mainimageWeb;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_detailed1_layout);
        ButterKnife.bind(this);
        txtTitle.setText("详情");
        mainimageWeb.getSettings().setDomStorageEnabled(true);
        mainimageWeb.loadUrl(getIntent().getStringExtra("url"));
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}
