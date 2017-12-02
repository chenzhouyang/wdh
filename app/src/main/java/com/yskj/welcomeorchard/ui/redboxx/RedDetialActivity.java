package com.yskj.welcomeorchard.ui.redboxx;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.AppManager;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.RedDetialAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.RedDetailsEntity;
import com.yskj.welcomeorchard.fragment.FragmentInterface;
import com.yskj.welcomeorchard.utils.JSONFormat;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.yskj.welcomeorchard.utils.MessgeUtil;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.CircleImageView;
import com.yskj.welcomeorchard.widget.DividerGridItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/2/9.
 */

public class RedDetialActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.reddetial_avatar)
    CircleImageView reddetialAvatar;
    @Bind(R.id.reddetial_name)
    TextView reddetialName;
    @Bind(R.id.reddetial_remark)
    TextView reddetialRemark;
    @Bind(R.id.reddetial_message)
    TextView reddetialMessage;
    @Bind(R.id.reddetial_nolistview)
    XRecyclerView reddetialNolistview;
    @Bind(R.id.reddetial_open)
    TextView reddetialOpen;
    private String redId;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private RedDetailsEntity reddetialentity;
    private RedDetialAdapter adapter;
    private ArrayList<RedDetailsEntity.DataBean.ReceiverListBean> receiverlist = new ArrayList<>();
    private String openAmount;
    private FragmentInterface.OnGetUrlListener onGetUrlListener;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddetial);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        redId = getIntent().getStringExtra("redId");
        openAmount = getIntent().getStringExtra("openAmount");
        if (openAmount==null||openAmount.equals("")){
            return;
        }
        OkHttpUtils.get().url(Urls.REDCRED)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("redLogId", redId)
                .addParams("openAmount",openAmount)
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
                Logger.json(response);
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    reddetialentity = new Gson().fromJson(response, new TypeToken<RedDetailsEntity>() {}.getType());
                    if(reddetialentity.data.redType == 0){
                        txtTitle.setText("销售红包");
                    }else if (reddetialentity.data.redType == 1) {
                        txtTitle.setText("绩效红包 ");
                    } else if (reddetialentity.data.redType == 2) {
                        txtTitle.setText("消费红包");
                    }else if (reddetialentity.data.redType == 3) {
                        txtTitle.setText("分享红包");
                    }
                    reddetialOpen.setText("拆"+ StringUtils.getStringtodouble(reddetialentity.data.openAmount)+"元");
                    reddetialMessage.setText("共" + reddetialentity.data.totalCount+ "份红包");
                    reddetialRemark.setText(reddetialentity.data.blessWord);
                    ArrayList<RedDetailsEntity.DataBean.ReceiverListBean> receiverListBean = reddetialentity.data.receiverList;
                    if(receiverListBean!=null&&receiverListBean.size()!=0){
                        receiverlist.addAll(reddetialentity.data.receiverList);
                        adapter = new RedDetialAdapter(context, receiverlist);
                        layoutManager = new GridLayoutManager(context, 1);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        reddetialNolistview.setLayoutManager(layoutManager);
                        reddetialNolistview.setHasFixedSize(true);
                        reddetialNolistview.setPullRefreshEnabled(false);
                        reddetialNolistview.addItemDecoration(new DividerGridItemDecoration(context, 15, R.drawable.divider));
                        reddetialNolistview.setAdapter(adapter);
                    }
                }else {
                    showToast(MessgeUtil.geterr_code(code));
                }
            }
        });

    }


    @OnClick(R.id.img_back)
    public void onClick() {
        AppManager.getInstance().killActivity(RedDetialActivity.class);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AppManager.getInstance().killActivity(RedDetialActivity.class);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
