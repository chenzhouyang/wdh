package com.yskj.welcomeorchard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.yskj.welcomeorchard.R;
import com.yskj.welcomeorchard.adapter.AdverMainDialogGVAdapter;
import com.yskj.welcomeorchard.entity.AllFragmentHeadViewEntity;
import com.yskj.welcomeorchard.ui.advertising.AdvertisedHasSendActivity;
import com.yskj.welcomeorchard.ui.advertising.AdvertisingDraftActivity;
import com.yskj.welcomeorchard.ui.advertising.AdvertisingHasOpenActivity;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class AdverMainDialog extends Dialog implements View.OnClickListener {
    private Context context;
    GridView gridView;
    private TextView abolish;
    public AdverMainDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertising_pop);
        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        abolish = (TextView) findViewById(R.id.abolish);
        abolish.setOnClickListener(this);
        ArrayList<AllFragmentHeadViewEntity> allFragmentHeadViewEntityArrayList = new ArrayList<>();
        AllFragmentHeadViewEntity allFragmentHeadViewEntity = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity.setName("已发布广告");
        allFragmentHeadViewEntity.setIconId(R.mipmap.transmit);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity);
        AllFragmentHeadViewEntity allFragmentHeadViewEntity1 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity1.setName("草稿");
        allFragmentHeadViewEntity1.setIconId(R.mipmap.draught);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity1);
        AllFragmentHeadViewEntity allFragmentHeadViewEntity4 = new AllFragmentHeadViewEntity();
        allFragmentHeadViewEntity4.setName("已领取红包");
        allFragmentHeadViewEntity4.setIconId(R.mipmap.draw);
        allFragmentHeadViewEntityArrayList.add(allFragmentHeadViewEntity4);
        AdverMainDialogGVAdapter allFragmentHeadGridViewAdapter = new AdverMainDialogGVAdapter(context, allFragmentHeadViewEntityArrayList);
        gridView.setAdapter(allFragmentHeadGridViewAdapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        context.startActivity(new Intent(context, AdvertisedHasSendActivity.class));
//                        Intent intent = new Intent(context, AdvertisingOpenActivity.class);
//
//                        intent.putExtra("adRedId", "337");
//                        intent.putExtra("adRedTemplate", "/App/Ad/ad_red_pagesShow/rid/18");
//                        intent.putExtra("latitude","34.095111");
//                        intent.putExtra("longitude","113.858224");
//                        context.startActivity(intent);
                        break;
                    case 1:
                        context.startActivity(new Intent(context, AdvertisingDraftActivity.class));
                        break;
                    case 2:
                        context.startActivity(new Intent(context, AdvertisingHasOpenActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.abolish:
                dismiss();
                break;

        }
    }

}
