package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.SourceParAdapter;
import com.yskj.welcomeorchard.config.Urls;
import com.yskj.welcomeorchard.entity.LocalServerEntity;
import com.yskj.welcomeorchard.entity.LocalServerNumEntity;
import com.yskj.welcomeorchard.entity.ParticularsEntitiy;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.ui.supply.SourceParticularsActivity;
import com.yskj.welcomeorchard.utils.GlideImage;
import com.yskj.welcomeorchard.utils.StringUtils;
import com.yskj.welcomeorchard.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建日期 2017/5/9on 15:53.
 * 描述：
 * 作者：
 */

public class StoreSupplyDialog extends Dialog implements View.OnClickListener , SourceParAdapter.CheckInterface,SourceParAdapter.ModifyCountInterface{
    private Context context;
    private ImageView imgView_close,dialog_image;
    private TextView dialog_name,dialog_allamount,tv_put_true;
    private NoScrollListView lv_commodity_property;
    private String goodsid;
    private  ParticularsEntitiy commodityEntity;
    private int millId;
    private String goodsname,imageurl,shopname;
    private List<ParticularsEntitiy.DataBean.ParameterListBean> standardlist = new ArrayList<>();
    private SourceParAdapter sourceParAdapter;
    private LocalServerEntity localServerEntity;
    private LocalServerNumEntity localServerNumEntity;
    private double allAmount;
    private boolean isCheckd = false;
    private List<LocalServerEntity> mListGoods;

    public StoreSupplyDialog(Context context,String goodsid,String imageurl,String shopname) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.goodsid = goodsid;
        this.imageurl = imageurl;
        this.shopname = shopname;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_select_layout);
        initView();
        getDate();
    }

    private void initView() {
        imgView_close = (ImageView) findViewById(R.id.imgView_close);
        dialog_image = (ImageView) findViewById(R.id.dialog_image);
        dialog_name = (TextView) findViewById(R.id.dialog_name);
        dialog_allamount = (TextView) findViewById(R.id.dialog_allamount);
        lv_commodity_property = (NoScrollListView) findViewById(R.id.lv_commodity_property);
        tv_put_true = (TextView) findViewById(R.id.tv_put_true);
        imgView_close.setOnClickListener(this);
        tv_put_true.setOnClickListener(this);

    }
    private void getDate(){
        OkHttpUtils.get().url(Urls.BOUNTSDETIAL)
                .addParams("goodsId",goodsid).build().execute(new CommodityCallBack());
    }
    /**
     * 获取数据进行实例化
     */
    private class CommodityCallBack extends Callback<ParticularsEntitiy> {

        @Override
        public ParticularsEntitiy parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            commodityEntity = new Gson().fromJson(s, new TypeToken<ParticularsEntitiy>() {
            }.getType());
            return commodityEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(ParticularsEntitiy response, int id) {
            if (response.code == 0) {
                //添加轮播图
                millId = response.data.millId;
                goodsname = response.data.name;
                GlideImage.loadImage(context, dialog_image, imageurl, R.mipmap.img_error);
                dialog_name.setText(goodsname);
                standardlist.addAll(response.data.parameterList);

                sourceParAdapter = new SourceParAdapter(context, standardlist);
                sourceParAdapter.setCheckInterface(StoreSupplyDialog.this);//更改选中状态
                sourceParAdapter.setModifyCountInterface(StoreSupplyDialog.this);//更新数量
                lv_commodity_property.setAdapter(sourceParAdapter);
            }
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgView_close:
                for (int i = 0; i < standardlist.size(); i++) {
                    if(standardlist.get(i).isGroupSelected()){
                        DataSupport.deleteAll(LocalServerNumEntity.class,"parameterid=?",standardlist.get(i).id+"");
                    }
                }
                mListGoods = DataSupport.findAll(LocalServerEntity.class);
                for (LocalServerEntity item : mListGoods) {
                    List<LocalServerNumEntity> list = DataSupport.where("shopid=?",item.getShopid()+"").find(LocalServerNumEntity.class);
                    if(list.size()<=0){
                        DataSupport.deleteAll(LocalServerEntity.class,"shopid=?",item.getShopid()+"");
                    }
                }
                dismiss();
                break;
            case R.id.tv_put_true:
                for (int i = 0; i < standardlist.size(); i++) {
                    if(standardlist.get(i).isGroupSelected()){
                        isCheckd = true;
                    }
                }
                if(!isCheckd){
                    Toast.makeText(context,"您还没有选择商品规格哦",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"添加购物车成功",Toast.LENGTH_LONG).show();
                    dismiss();
                }
                break;

        }
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        allAmount= 0.00;
        ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = standardlist.get(position);
        standardlist.get(position).setGroupSelected(isChecked);
        for (int i = 0; i < standardlist.size(); i++) {
            if(standardlist.get(i).isGroupSelected()){
                allAmount+=standardlist.get(i).price*standardlist.get(i).getNum();
            }
        }
        dialog_allamount.setText(Html.fromHtml("共:<font color = \"#fa2f5a\">¥"+StringUtils.getStringtodouble(allAmount)+"</font>"));
        sourceParAdapter.notifyDataSetChanged();
        if(parameterListBean.isGroupSelected()){
            List<LocalServerNumEntity> shoppingDetailEntityList = DataSupport.where("parameterid = ?", parameterListBean.id+"").find(LocalServerNumEntity.class);
            List<LocalServerEntity> localServerEntityList = DataSupport.where("shopid = ?", millId+"").find(LocalServerEntity.class);
            if(localServerEntityList==null||localServerEntityList.size()==0){
                localServerEntity = new LocalServerEntity();
                localServerEntity.setGoodsid(goodsid);
                localServerEntity.setParameterid(parameterListBean.id);
                localServerEntity.setShopid(millId+"");
                localServerEntity.setShopname(shopname);
                localServerEntity.setEditing(false);
                localServerEntity.setGroupSelected(false);
                localServerEntity.save();
            }
            if(shoppingDetailEntityList==null||shoppingDetailEntityList.size()==0){
                localServerNumEntity = new LocalServerNumEntity();
                localServerNumEntity.setLocalServerEntity(localServerEntity);
                localServerNumEntity.setCount(parameterListBean.getNum());
                localServerNumEntity.setParameterid(parameterListBean.id);
                localServerNumEntity.setParametername(parameterListBean.name);
                localServerNumEntity.setShopid(millId+"");
                localServerNumEntity.setGoodsimage(imageurl);
                localServerNumEntity.setMaccount(parameterListBean.mAccount);
                localServerNumEntity.setGoodsid(goodsid);
                localServerNumEntity.setEditing(false);
                localServerNumEntity.setChildSelected(false);
                localServerNumEntity.setPrice(parameterListBean.price);
                localServerNumEntity.setGoodsname(goodsname);
                localServerNumEntity.save();
            }else {
                int currount = 0;
                for (LocalServerNumEntity item : shoppingDetailEntityList) {
                    if(item.getParameterid() == parameterListBean.id){
                        currount = item.getCount();
                    }
                }
                LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
                localServerUpdateNumEntity.setCount(parameterListBean.getNum()+currount);
                localServerUpdateNumEntity.updateAll("parameterid = ?",parameterListBean.id+"");
            }
        }else {

            DataSupport.deleteAll(LocalServerNumEntity.class,"parameterid = ?",parameterListBean.id+"");
            mListGoods = DataSupport.findAll(LocalServerEntity.class);
            for (LocalServerEntity item : mListGoods) {
                List<LocalServerNumEntity> list = DataSupport.where("shopid=?",item.getShopid()+"").find(LocalServerNumEntity.class);
                if(list.size()<=0){
                    DataSupport.deleteAll(LocalServerEntity.class,"shopid=?",item.getShopid()+"");
                }
            }
        }
    }

    /**
     * 增加数量
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param stock       库存
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, int stock, boolean isChecked) {
        allAmount = 0.00;
        ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = standardlist.get(position);
        int currentCount = parameterListBean.getNum();
        currentCount++;
        if(currentCount>stock){
            Toast.makeText(context,"数量超出库存",Toast.LENGTH_LONG).show();
        }else {
            parameterListBean.setNum(currentCount);
            ((TextView) showCountView).setText(currentCount + "");
            sourceParAdapter.notifyDataSetChanged();
            if(parameterListBean.isGroupSelected()){
                LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
                localServerUpdateNumEntity.setCount(parameterListBean.getNum());
                localServerUpdateNumEntity.updateAll("parameterid = ?",parameterListBean.id+"");
            }
        }
        if(parameterListBean.isGroupSelected()){
            for (int i = 0; i < standardlist.size(); i++) {
                if(standardlist.get(i).isGroupSelected()){
                    allAmount+=standardlist.get(i).price*standardlist.get(i).getNum();
                }
            }
            dialog_allamount.setText(Html.fromHtml("共:<font color = \"#fa2f5a\">¥"+StringUtils.getStringtodouble(allAmount)+"</font>"));
        }

    }

    /**
     * 减少数量
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        allAmount = 0.00;
        ParticularsEntitiy.DataBean.ParameterListBean parameterListBean = standardlist.get(position);
        int currentCount = parameterListBean.getNum();
        if(currentCount == 1){
            return;
        }
        currentCount--;
        parameterListBean.setNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        sourceParAdapter.notifyDataSetChanged();
        if(parameterListBean.isGroupSelected()){
            LocalServerNumEntity localServerUpdateNumEntity = new LocalServerNumEntity();
            localServerUpdateNumEntity.setCount(currentCount);
            localServerUpdateNumEntity.updateAll("parameterid = ?",parameterListBean.id+"");
        }
        if(parameterListBean.isGroupSelected()){
            for (int i = 0; i < standardlist.size(); i++) {
                if(standardlist.get(i).isGroupSelected()){
                    allAmount+=standardlist.get(i).price*standardlist.get(i).getNum();
                }
            }
            dialog_allamount.setText(Html.fromHtml("共:<font color = \"#fa2f5a\">¥"+StringUtils.getStringtodouble(allAmount)+"</font>"));
        }
    }
}
