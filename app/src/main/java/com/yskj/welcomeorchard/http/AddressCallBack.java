package com.yskj.welcomeorchard.http;

import android.content.Context;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.welcomeorchard.adapter.AddressAdapter;
import com.yskj.welcomeorchard.entity.AddressEntity;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/14.
 */

public class AddressCallBack extends Callback<AddressEntity> {
    private Context context;
    private ListView addressLv;
    private Handler handler;
    private ArrayList<AddressEntity.AddressListBean> addressEntityList = new ArrayList<>();
    public AddressCallBack(Context context,ListView addressLv,Handler handler){
        this.addressLv = addressLv;
        this.context = context;
        this.handler =handler;
    }
    @Override
    public AddressEntity parseNetworkResponse(Response response, int id) throws Exception {
        String s = response.body().string();
        AddressEntity addressentitylist = new Gson().fromJson(s,new TypeToken<AddressEntity>(){}.getType());
        return addressentitylist;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(AddressEntity response, int id) {
        addressEntityList = response.addressList;
        if(addressEntityList!=null&&addressEntityList.size()!=0){
            AddressAdapter adapter = new AddressAdapter(context,addressEntityList,handler);
            addressLv.setAdapter(adapter);
        }else {
            Toast.makeText(context,"您还未添加收货地址",Toast.LENGTH_SHORT).show();
        }

    }
}
