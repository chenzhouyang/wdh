package com.yskj.welcomeorchard.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.DistributionAdapter;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.DistriTionEntity;
import com.yskj.welcomeorchard.utils.LoadingCaches;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 创建日期 2017/5/9on 15:53.
 * 描述：
 * 作者：
 */

public class DistributionDialog extends Dialog implements View.OnClickListener ,DistributionAdapter.ModifyCountInterface{
    private ImageView imgDel,imgDes;
    private Context context;
    private ListView distribution_lv;
    private String orderid;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private DistriTionEntity distriTionEntity;
    private DistributionAdapter distributionAdapter;
    private List<DistriTionEntity.DataBean> dataBeanList = new ArrayList<>();
    public DistributionDialog(Context context,String orderid) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.orderid = orderid;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_distribution);
        initView();
        getDistri();
    }

    private void initView() {
        distribution_lv = (ListView) findViewById(R.id.distribution_lv);
        imgDel = (ImageView) findViewById(R.id.img_del);
        imgDel.setOnClickListener(this);
        distributionAdapter = new DistributionAdapter(context,dataBeanList);
        distributionAdapter.setModifyCountInterface(DistributionDialog.this);
        distribution_lv.setAdapter(distributionAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                break;
            case R.id.img_del:
                dismiss();
                break;
        }
    }
    /**
     * 获取分销商品
     */
    private void getDistri(){
        OkHttpUtils.get().url(Urls.DISTRITION)
                .addHeader("Authorization",caches.get("access_token"))
                .addParams("orderId",orderid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                distriTionEntity = new Gson().fromJson(response,DistriTionEntity.class);
                if(distriTionEntity.code == 0){
                    dataBeanList.addAll(distriTionEntity.data);
                    distributionAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 增加
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param stock       库存
     */
    @Override
    public void doIncrease(int position, View showCountView, int stock) {
        DistriTionEntity.DataBean distriTionEntity = dataBeanList.get(position);
        int count = distriTionEntity.getNum();
        count++;
        if(count>stock){
            Toast.makeText(context,"超出库存",Toast.LENGTH_LONG).show();
        }else {
            distriTionEntity.setNum(count);
            ((TextView) showCountView).setText(count + "");
            distributionAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 减少
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     */
    @Override
    public void doDecrease(int position, View showCountView) {
        DistriTionEntity.DataBean distriTionEntity = dataBeanList.get(position);
        int count = distriTionEntity.getNum();
        count--;
        if(count==1){
            return;
        }
        distriTionEntity.setNum(count);
        ((TextView) showCountView).setText(count + "");
        distributionAdapter.notifyDataSetChanged();
    }
}
