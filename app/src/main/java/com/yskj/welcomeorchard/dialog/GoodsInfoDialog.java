package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.utils.GlideImage;

/**
 * 创建日期 2017/5/9on 15:53.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class GoodsInfoDialog  extends Dialog implements View.OnClickListener {
    private ImageView imgDel,imgDes;
    private TextView tvName,tvPrice;
    private Button btnSure;
    private Context context;

    private String name,price,imgUrl,goodsid;

    public GoodsInfoDialog(Context context,String name,String price,String imgUrl,String goodsid) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.goodsid = goodsid;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goods_info);
        initView();
    }

    private void initView() {
        imgDel = (ImageView) findViewById(R.id.img_del);
        imgDes = (ImageView) findViewById(R.id.img_des);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        btnSure = (Button) findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(this);
        imgDel.setOnClickListener(this);

        tvName.setText(name);
        tvPrice.setText(price);
        GlideImage.loadImage(context,imgDes,imgUrl,R.mipmap.img_error);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                Intent intent = new Intent(context, CommodityDetailsActiviy.class);
                intent.putExtra("goodid",goodsid);
                context.startActivity(intent);
                break;
            case R.id.img_del:
                dismiss();
                break;
        }
    }
}
