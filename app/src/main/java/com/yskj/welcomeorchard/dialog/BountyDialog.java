package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.ui.commoditydetails.CommodityDetailsActiviy;
import com.yskj.welcomeorchard.utils.GlideImage;

import static com.yskj.welcomeorchard.ui.address.AddressActivity.SHUAXIN;

/**
 * 创建日期 2017/5/9on 15:53.
 * 描述：
 * 作者：
 */

public class BountyDialog extends Dialog implements View.OnClickListener {
    private ImageView imgDel,imgDes;
    private Context context;
    private Handler handler;
    private String amount;
    private TextView bounty_amount;
    public BountyDialog(Context context,Handler handler,String amount) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.handler = handler;
        this.amount = amount;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bounty);
        initView();
    }

    private void initView() {
        imgDel = (ImageView) findViewById(R.id.img_del);
        imgDel.setOnClickListener(this);
        bounty_amount = (TextView) findViewById(R.id.bounty_amount);
        bounty_amount.setText(amount+"元返现红包");
        Message message =new Message();
        message.what=SHUAXIN;
        handler.handleMessage(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_del:
                dismiss();
                break;
        }
    }
}
