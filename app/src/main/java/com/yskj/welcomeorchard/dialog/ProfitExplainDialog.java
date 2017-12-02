package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;

/**
 * 创建日期 2017/3/27on 17:05.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ProfitExplainDialog extends Dialog {
    private TextView tvTitle;
    private TextView tvMessage;
    private ImageView imgDel;

    private Context context;
    private String title, message;

    public ProfitExplainDialog(Context context,String title,String message) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.message = message;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_profit_explain);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        imgDel = (ImageView) findViewById(R.id.img_del);

        tvTitle.setText(title);
        tvMessage.setText(message);
        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
