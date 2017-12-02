package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.ui.approve.ApproveActivity;
import com.yskj.welcomeorchard.ui.deposit.BankListActivity;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class ApproveDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private TextView message;
    private String diamessage;
    private String type;

    public ApproveDialog(Context context, String diamessage,String type) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.diamessage = diamessage;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_give_up);
        initView();
    }

    private void initView() {
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnSure = (Button) findViewById(R.id.btn_sure);
        message = (TextView) findViewById(R.id.message);
        message.setText(diamessage);
        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                context.startActivity(new Intent(context, ApproveActivity.class).putExtra("type",type).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_cancle:
                if (type.equals("1")){
                    context.startActivity(new Intent(context, BankListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                dismiss();
                break;
        }
    }
}
