package com.yskj.welcomeorchard.ui.advertising;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.ChooseSampleAdapter;
import com.yskj.welcomeorchard.adapter.ChooseVersionItemAdapter;
import com.yskj.welcomeorchard.base.BaseActivity;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.ChooseSampleEntity;
import com.yskj.welcomeorchard.entity.ChooseVersionItemEntity;
import com.yskj.welcomeorchard.entity.UserInfoEntity;
import com.yskj.welcomeorchard.login.LoginActivity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/5/15on 10:55.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ChooseVersionItemActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.gridView)
    GridView gridView;


    private String cid;

    private int uid;
    private UserInfoEntity userInfoEntity;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String type;
    private String rid;
    private String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_version_item);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        imgBack.setOnClickListener(this);

        type = getIntent().getStringExtra("type");
        cid = getIntent().getStringExtra("cid");

        if (caches.get("access_token").equals("null")) {
            startActivity(new Intent(ChooseVersionItemActivity.this, LoginActivity.class));
            return;
        } else {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
            uid = userInfoEntity.data.userVo.id;
        }
        //sample 样例  template 模板
        switch (type){
            case "sample":
                txtTitle.setText("选择样例");
                OkHttpUtils.get().url(Urls.ADSIMPLELIST+cid).build().execute(new chooseSampleCallBack());
                break;
            case "template":
                txtTitle.setText("选择页面");
                rid = getIntent().getStringExtra("rid");
                sort = getIntent().getStringExtra("sort");
                OkHttpUtils.get().url(Urls.ADTEMPLATELIUST + cid).build().execute(new chooseTemplateCallBack());
                break;
        }
    }

    //样例
    private class chooseSampleCallBack extends Callback<ChooseSampleEntity>{

        @Override
        public ChooseSampleEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            ChooseSampleEntity chooseSampleEntity = new Gson().fromJson(s, new TypeToken<ChooseSampleEntity>() {}.getType());
            return chooseSampleEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(final ChooseSampleEntity response, int id) {
            if (response.errorCode.equals("000")) {
                ChooseSampleAdapter adapter = new ChooseSampleAdapter(response.adrList, context);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        finish();
                        Intent intent = new Intent(context, VersionWebActivity.class);
                        intent.putExtra("url", Urls.ADVERSIONWEB + "/rid/" + response.adrList.get(position).adRedId + "/uid/" + uid);
                        Logger.d(Urls.ADVERSIONWEB + "/rid/" + response.adrList.get(position).adRedId + "/uid/" + uid);
                        intent.putExtra("cid", response.adrList.get(position).classId);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    //模板
    private class chooseTemplateCallBack extends Callback<ChooseVersionItemEntity> {

        @Override
        public ChooseVersionItemEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            ChooseVersionItemEntity chooseVersionEntity = new Gson().fromJson(s, new TypeToken<ChooseVersionItemEntity>() {}.getType());
            return chooseVersionEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
        }

        @Override
        public void onResponse(final ChooseVersionItemEntity response, int id) {
            if (response.errorCode.equals("000")) {
                ChooseVersionItemAdapter adapter = new ChooseVersionItemAdapter(response.adrTlist, context);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        finish();
                        Intent intent = new Intent(context, VersionWebActivity.class);
                        intent.putExtra("url", Urls.ADEDITREDPAGES + "/tid/" + response.adrTlist.get(position).templateId + "/uid/" + uid+"/rid/"+rid+"/sort/"+sort);
                        intent.putExtra("cid",cid);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
