package com.yskj.welcomeorchard.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.ui.verify.OrchardPasswordActivity;
import com.yskj.welcomeorchard.ui.verify.VerifyActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： chen
 * 时间：2017/9/19.
 * 描述：修改支付密码过度界面
 */

public class OverPaymentActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.wdh_paypassword)
    TextView wdhPaypassword;
    @Bind(R.id.orcard_paypassword)
    TextView orcardPaypassword;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overpayment);
        ButterKnife.bind(this);
        txtTitle.setText("修改支付密码");
    }

    @OnClick({R.id.img_back, R.id.wdh_paypassword, R.id.orcard_paypassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //返回
                finish();
                break;
            case R.id.wdh_paypassword:
                //唯多惠
                caches.put("type","setting");
                startActivity(new Intent(context, VerifyActivity.class)
                        .putExtra("type","6").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.orcard_paypassword:
                //果果乐
                startActivity(new Intent(context, OrchardPasswordActivity.class));
                break;
        }
    }
}
