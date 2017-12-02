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
 * Created by YSKJ-02 on 2017/2/9.
 */

public class ExplainActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.explain_web_view)
    BridgeWebView explainWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ButterKnife.bind(this);
        txtTitle.setText("唯多惠说明");
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}
