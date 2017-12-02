package com.yskj.welcomeorchard.ui.supply;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.dialog.BountyDialog;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 陈宙洋
 * 2017/8/4.
 * 描述：返还红包领取activity
 */

public class BountyActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.bounty_image)
    ImageView bountyImage;
    private Object drawBounty;
    private String orderid;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounty);
        ButterKnife.bind(this);
        orderid = getIntent().getStringExtra("orderid");
    }

    @OnClick({R.id.img_back, R.id.bounty_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.bounty_image:
                getDrawBounty();
                break;
        }
    }

    public void getDrawBounty() {
        OkHttpUtils.get().url(Urls.BOUNTS)
                .addHeader("Authorization",caches.get("access_token"))
                .addParams("orderId",orderid).build().execute(new StringCallback() {
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
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                if(map!=null){
                    int code = (int) map.get("code");
                    if(code == 0){
                     /*   //领取成功之后应该是个弹出框的形式
                        BountyDialog dialog = new BountyDialog(context);
                        dialog.show();*/
                    }
                }
            }
        });
    }
}
