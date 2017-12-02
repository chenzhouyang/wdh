package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.login.LoginActivity;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class HintLoginDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private TextView message;

    public HintLoginDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hint_dialog);
        initView();
    }

    private void initView() {
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnSure = (Button) findViewById(R.id.btn_sure);
        message = (TextView) findViewById(R.id.message);
        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                context.startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
