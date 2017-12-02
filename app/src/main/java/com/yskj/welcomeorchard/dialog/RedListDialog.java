package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.utils.LoadingCaches;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class RedListDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private TextView message;
    private String diamessage;
    private LoadingCaches caches = LoadingCaches.getInstance();

    public RedListDialog(Context context, String diamessage) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.diamessage = diamessage;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_redlist_up);
        initView();
    }

    private void initView() {
        btnSure = (Button) findViewById(R.id.btn_sure);
        message = (TextView) findViewById(R.id.message);
        message.setText(diamessage);
        btnSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                break;
        }
    }
}
