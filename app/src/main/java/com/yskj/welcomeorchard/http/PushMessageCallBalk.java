package com.yskj.welcomeorchard.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.adapter.PushMessageAdapter;
import com.yskj.welcomeorchard.entity.PushMessageEntity;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/18.
 */

public class PushMessageCallBalk extends Callback<PushMessageEntity> {
    private Context context;
    private NoScrollListView poputMessageNolistview;
    public PushMessageCallBalk(Context context,NoScrollListView poputMessageNolistview){
        super();
        this.context = context;
        this.poputMessageNolistview = poputMessageNolistview;
    }
    @Override
    public PushMessageEntity parseNetworkResponse(Response response, int id) throws Exception {
        String s = response.body().string();
        PushMessageEntity pushMessageEntity = new Gson().fromJson(s,new TypeToken<PushMessageEntity>(){}.getType());
        return pushMessageEntity;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(PushMessageEntity response, int id) {
        if (response.error_code==000){
            ArrayList<PushMessageEntity.HistoryInfoBean> historyInfoBean = response.historyInfo;
            PushMessageAdapter adapter = new PushMessageAdapter(context,historyInfoBean);
            poputMessageNolistview.setAdapter(adapter);
        }
    }
}
