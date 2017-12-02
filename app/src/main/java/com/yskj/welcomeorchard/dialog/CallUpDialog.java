package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class CallUpDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private TextView message;
    private String diamessage;

    public CallUpDialog(Context context, String diamessage) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.diamessage = diamessage;

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
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "4006379527");
                intent.setData(data);
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
                context.startActivity(intent);
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
